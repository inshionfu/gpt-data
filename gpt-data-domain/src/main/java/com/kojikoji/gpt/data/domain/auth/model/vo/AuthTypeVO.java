package com.kojikoji.gpt.data.domain.auth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName AuthTypeVO
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/15 16:16
 * @Version
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AuthTypeVO {
    A0000("0000", "验证成功"),
    A0001("0001", "验证码不存在"),
    A0002("0002", "验证码无效");

    private String code;
    private String info;
}
