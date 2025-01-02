package com.kojikoji.gpt.data.domain.order.model.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName PayTypeVO
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/19 21:20
 * @Version
 */
@Getter
@AllArgsConstructor
public enum PayTypeVO {
    WEIXIN_NATIVE(0, "微信Native支付"),
    ZFB_SANDBOX(1, "支付宝沙箱支付")
    ;

    private final Integer code;
    private final String desc;

    public static PayTypeVO of(Integer code) {
        switch (code) {
            case 0:
                return PayTypeVO.WEIXIN_NATIVE;
            default:
                return PayTypeVO.WEIXIN_NATIVE;
        }
    }
}
