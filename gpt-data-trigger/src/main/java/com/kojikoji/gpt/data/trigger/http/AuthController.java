package com.kojikoji.gpt.data.trigger.http;

import com.alibaba.fastjson.JSON;
import com.kojikoji.gpt.data.domain.auth.IAuthService;
import com.kojikoji.gpt.data.domain.auth.model.entity.AuthStateEntity;
import com.kojikoji.gpt.data.domain.auth.model.vo.AuthTypeVO;
import com.kojikoji.gpt.data.types.common.Constants;
import com.kojikoji.gpt.data.types.model.Response;
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
