package com.kojikoji.gpt.data.domain.weixin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.Date;

/**
 * @ClassName UserBehaviorMessageEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/15 17:02
 * @Version
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBehaviorMessageEntity {
    private String openId;
    private String fromUserName;
    private String msgType;
    private String content;
    private String event;
    private Date createTime;
}
