package com.kojikoji.gpt.data.domain.openai.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MesageEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/14 20:00
 * @Version
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptEntity {
    private String role;
    private String content;
}
