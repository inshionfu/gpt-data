package com.kojikoji.gpt.data.domain.weixin;

public interface IWeiXinValidateService {
    boolean checkSign(String signature, String timestamp, String nonce);
}
