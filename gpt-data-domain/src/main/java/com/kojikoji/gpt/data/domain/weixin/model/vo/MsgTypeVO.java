package com.kojikoji.gpt.data.domain.weixin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName MsgTypeVO
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/15 17:02
 * @Version
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MsgTypeVO {

    EVENT("event","事件消息"),
    TEXT("text","文本消息");

    private String code;
    private String desc;
}
