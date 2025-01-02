package com.kojikoji.gpt.data.domain.order.model.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName PayStatusVO
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/19 19:36
 * @Version
 */

@Getter
@AllArgsConstructor
public enum PayStatusVO {

    WAIT(0, "等待支付"),
    SUCCESS(1,"支付完成"),
    FAIL(2,"支付失败"),
    ABANDON(3,"放弃支付"),
    ;

    private final Integer code;
    private final String desc;

    public static PayStatusVO of(Integer code){
        switch (code){
            case 0:
                return PayStatusVO.WAIT;
            case 1:
                return PayStatusVO.SUCCESS;
            case 2:
                return PayStatusVO.FAIL;
            case 3:
                return PayStatusVO.ABANDON;
            default:
                return PayStatusVO.WAIT;
        }
    }

}