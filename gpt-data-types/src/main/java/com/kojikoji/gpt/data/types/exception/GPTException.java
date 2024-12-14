package com.kojikoji.gpt.data.types.exception;

/**
 * @ClassName ChatGPTException
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 14:45
 * @Version
 */

public class GPTException extends RuntimeException {

    /**
     * 异常码
     */
    private String code;

    /**
     * 异常信息
     */
    private String message;

    public GPTException(String code) {
        this.code = code;
    }

    public GPTException(String code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public GPTException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public GPTException(String code, String message, Throwable cause) {
        this.code = code;
        this.message = message;
        super.initCause(cause);
    }
}
