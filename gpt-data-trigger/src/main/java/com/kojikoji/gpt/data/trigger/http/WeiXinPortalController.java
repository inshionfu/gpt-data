package com.kojikoji.gpt.data.trigger.http;

import com.kojikoji.gpt.data.domain.weixin.IWeiXinBehaviorService;
import com.kojikoji.gpt.data.domain.weixin.IWeiXinValidateService;
import com.kojikoji.gpt.data.domain.weixin.model.entity.MessageTextEntity;
import com.kojikoji.gpt.data.domain.weixin.model.entity.UserBehaviorMessageEntity;
import com.kojikoji.gpt.data.types.sdk.weixin.XmlUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.Resource;
import java.util.Date;
import java.util.EventListener;

/**
 * @ClassName WeiXinPortalController
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/15 17:34
 * @Version
 */

@Slf4j
@RestController
@RequestMapping("/api/${app.config.api-version}/wx/portal/{appid}")
public class WeiXinPortalController {
    @Value("${wx.config.originalid}")
    private String originalId;

    @Resource
    private IWeiXinBehaviorService weiXinBehaviorService;

    @Resource
    private IWeiXinValidateService weiXinValidateService;

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String validate(@PathVariable String appid,
                           @RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {
        try {
            log.info("微信公众号验签信息{} 开始 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr);
            if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
                throw new IllegalArgumentException("请求参数非法，请核实!");
            }
            boolean check = weiXinValidateService.checkSign(signature, timestamp, nonce);
            log.info("微信公众号验签信息{} 完成 check: {}", appid, check);
            if (!check) {
                return null;
            }
            return echostr;
        } catch (Exception e) {
            log.error("微信公众号验签信息{} 失败[{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr, e);
            return null;
        }
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        try {
            log.info("接收微信公众号信息请求 {} 开始 {}", openid, requestBody);

            // 消息转换
            MessageTextEntity message = XmlUtil.xmlToBean(requestBody, MessageTextEntity.class);

            // 构建请求实体
            UserBehaviorMessageEntity entity = UserBehaviorMessageEntity.builder()
                    .openId(openid)
                    .fromUserName(message.getFromUserName())
                    .msgType(message.getMsgType())
                    .content(StringUtils.isBlank(message.getContent()) ? null : message.getContent().trim())
                    .event(message.getEvent())
                    .createTime(new Date(Long.parseLong(message.getCreateTime()) * 1000L))
                    .build();

            // 消息受理
            String result = weiXinBehaviorService.acceptUserBehavior(entity);
            log.info("接收微信公众号请求 {} 完成 {}", openid, result);
            return result;
        } catch (Exception e) {
            log.error("接收微信公众号信息请求 {} 失败 {}", openid, requestBody, e);
            return "";
        }
    }
}
