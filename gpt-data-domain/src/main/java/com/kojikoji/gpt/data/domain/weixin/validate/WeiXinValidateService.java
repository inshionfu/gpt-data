package com.kojikoji.gpt.data.domain.weixin.validate;

import com.kojikoji.gpt.data.domain.weixin.IWeiXinValidateService;
import com.kojikoji.gpt.data.types.sdk.weixin.SignatureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @ClassName WeiXinValidateService
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/15 16:57
 * @Version
 */
@Service
public class WeiXinValidateService implements IWeiXinValidateService {

    @Value("${wx.config.token}")
    private String token;

    @Override
    public boolean checkSign(String signature, String timestamp, String nonce) {
        return SignatureUtil.check(token, signature, timestamp, nonce);
    }
}
