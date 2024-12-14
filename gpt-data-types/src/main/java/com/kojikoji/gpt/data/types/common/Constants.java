package com.kojikoji.gpt.data.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName Constants
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 14:15
 * @Version
 */

public class Constants {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum ResponseCode {
        SUCCESS("0000", "成功"),
        UN_ERROR("0001", "未知失败"),
        ILLEGAL_PARAMETER("0002", "非法参数"),
        TOKEN_ERROR("0003", "权限拦截");

        private String code;
        private String info;

    }
}
