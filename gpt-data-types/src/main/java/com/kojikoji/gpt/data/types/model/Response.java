package com.kojikoji.gpt.data.types.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName Response
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/16 11:31
 * @Version
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {

    private String code;
    private String info;
    private T data;

}
