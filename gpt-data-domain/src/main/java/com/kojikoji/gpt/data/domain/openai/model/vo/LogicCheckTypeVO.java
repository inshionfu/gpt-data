package com.kojikoji.gpt.data.domain.openai.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName LogicCheckTypeVO
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/16 15:55
 * @Version
 */

@Getter
@AllArgsConstructor
public enum LogicCheckTypeVO {
    SUCCESS("0000", "校验通过"),
    REFUSE("0001", "校验拒绝"),
    ;

    private final String code;
    private final String info;

}
