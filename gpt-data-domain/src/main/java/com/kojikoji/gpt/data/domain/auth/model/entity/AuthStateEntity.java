package com.kojikoji.gpt.data.domain.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AuthStateEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/15 16:07
 * @Version
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthStateEntity {
    private String code;
    private String info;
    private String openId;
    private String token;
}
