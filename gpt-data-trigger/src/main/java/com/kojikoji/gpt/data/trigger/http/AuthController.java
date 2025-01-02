package com.kojikoji.gpt.data.trigger.http;

import com.alibaba.fastjson.JSON;
import com.kojikoji.gpt.data.domain.auth.service.IAuthService;
import com.kojikoji.gpt.data.domain.auth.model.entity.AuthStateEntity;
import com.kojikoji.gpt.data.domain.auth.model.vo.AuthTypeVO;
import com.kojikoji.gpt.data.domain.weixin.IWeiXinBehaviorService;
import com.kojikoji.gpt.data.domain.weixin.model.entity.MessageTextEntity;
import com.kojikoji.gpt.data.domain.weixin.model.entity.UserBehaviorMessageEntity;
import com.kojikoji.gpt.data.domain.weixin.model.vo.MsgTypeVO;
import com.kojikoji.gpt.data.types.common.Constants;
import com.kojikoji.gpt.data.types.model.Response;
import com.kojikoji.gpt.data.types.sdk.weixin.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.kojikoji.gpt.data.types.common.Constants.ResponseCode.TOKEN_ERROR;

/**
 * @ClassName AuthController
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/16 11:30
 * @Version
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/${app.config.api-version}/auth/")
public class AuthController {

    @Resource
    private IAuthService authService;

    @Resource
    private IWeiXinBehaviorService weiXinBehaviorService;

    /**
     *  测试接口，生成登录验证码
     */
    @RequestMapping(value = "gen/code", method = RequestMethod.POST)
    public Response<String> genCode(@RequestParam String openid) {
        log.info("验证码生成，开始, openid: {}", openid);
        UserBehaviorMessageEntity userBehaviorMessage = UserBehaviorMessageEntity.builder()
                .openId(openid)
                .msgType(MsgTypeVO.TEXT.getCode())
                .content("403")
                .build();

        String xml = weiXinBehaviorService.acceptUserBehavior(userBehaviorMessage);
        MessageTextEntity messageText = XmlUtil.xmlToBean(xml, MessageTextEntity.class);
        log.info("请求结束, openid: {} 结果 {}", openid, messageText.getContent());
        return Response.<String>builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getInfo())
                .data("验证码" + messageText.getContent())
                .build();
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Response<String> doLogin(@RequestParam String code) {
        log.info("鉴权登录校验开始，验证码：{}", code);
        try {
            AuthStateEntity authStateEntity = authService.doLogin(code);
            log.info("鉴权登录校验完成，验证码：{} 结果：{}", code, JSON.toJSONString(authStateEntity));
            // 拦截，鉴权失败
            if (!StringUtils.equals(AuthTypeVO.A0000.getCode(), authStateEntity.getCode())) {
                log.info("鉴权登录校验失败，验证码：{} 结果{}", code, JSON.toJSONString(authStateEntity));
                return Response.<String>builder()
                        .code(TOKEN_ERROR.getCode())
                        .info(TOKEN_ERROR.getInfo())
                        .build();
            }

            // 放行，鉴权成功
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(authStateEntity.getToken())
                    .build();
        } catch (Exception e) {
            log.error("鉴权登录校验失败，验证码: {}", code);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

}
