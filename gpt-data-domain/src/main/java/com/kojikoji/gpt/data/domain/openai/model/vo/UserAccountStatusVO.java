package com.kojikoji.gpt.data.domain.openai.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName UserAccountStatusVO
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/18 19:58
 * @Version
 */
@Getter
@AllArgsConstructor
public enum UserAccountStatusVO {
    AVAILABLE(0, "可用"),
    FREEZE(1, "冻结"),
    ;

    private final Integer code;
    private final String info;

    public static UserAccountStatusVO get(Integer code){
        switch (code){
            case 0:
                return UserAccountStatusVO.AVAILABLE;
            case 1:
                return UserAccountStatusVO.FREEZE;
            default:
                return UserAccountStatusVO.AVAILABLE;
        }
    }
}
