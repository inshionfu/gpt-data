package com.kojikoji.gpt.data.domain.weixin.service;

import com.google.common.cache.Cache;
import com.kojikoji.gpt.data.domain.weixin.IWeiXinBehaviorService;
import com.kojikoji.gpt.data.domain.weixin.model.entity.MessageTextEntity;
import com.kojikoji.gpt.data.domain.weixin.model.entity.UserBehaviorMessageEntity;
import com.kojikoji.gpt.data.domain.weixin.model.vo.MsgTypeVO;
import com.kojikoji.gpt.data.types.exception.GPTException;
import com.kojikoji.gpt.data.types.sdk.weixin.XmlUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.MacSpi;

/**
 * @ClassName WeiXinBehaviorService
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/15 17:01
 * @Version
 */
@Service
public class WeiXinBehaviorService implements IWeiXinBehaviorService {

    @Value("${wx.config.originalid}")
    private String originalId;

    @Resource
    private Cache<String, String> codeCache;

    @Override
    public String acceptUserBehavior(UserBehaviorMessageEntity userBehaviorMessageEntity) {
        // Event事件类型，忽略处理
        if (MsgTypeVO.EVENT.getCode().equals(userBehaviorMessageEntity.getMsgType())) {
            return "";
        }

        if (MsgTypeVO.TEXT.getCode().equals(userBehaviorMessageEntity.getMsgType())) {
            String content = userBehaviorMessageEntity.getContent();

            // 反馈信息
            MessageTextEntity res = new MessageTextEntity();
            res.setToUserName(userBehaviorMessageEntity.getOpenId());
            res.setFromUserName(originalId);
            res.setCreateTime(String.valueOf(System.currentTimeMillis()));
            res.setMsgType("text");

            if (!StringUtils.equals(content, "403")) {
                res.setContent("请输入正确的请求");
                return XmlUtil.beanToXml(res);
            }

            // 缓存验证码
            String isExistCode = codeCache.getIfPresent(userBehaviorMessageEntity.getOpenId());

            // 判断验证码
            if (StringUtils.isBlank(isExistCode)) {
                String code = RandomStringUtils.randomNumeric(4);
                codeCache.put(code, userBehaviorMessageEntity.getOpenId());
                codeCache.put(userBehaviorMessageEntity.getOpenId(), code);
                isExistCode = code;
            }

            res.setContent(String.format("您的验证码为：%s 有效期%d分钟", isExistCode, 3));
            return XmlUtil.beanToXml(res);
        }

        throw new GPTException(userBehaviorMessageEntity.getMsgType() + " 未被处理的行为类型 Err！");
    }
}
