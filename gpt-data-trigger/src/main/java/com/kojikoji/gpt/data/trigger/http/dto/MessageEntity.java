package com.kojikoji.gpt.data.trigger.http.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MessageEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 14:16
 * @Version
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {

    private String role;
    private String content;
    private String name;
}
