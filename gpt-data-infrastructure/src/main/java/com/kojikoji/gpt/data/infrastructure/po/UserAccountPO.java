package com.kojikoji.gpt.data.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * @ClassName UserAccountPO
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/18 20:37
 * @Version
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountPO {
    private Long id;
    private String openid;
    private Integer totalQuota;
    private Integer surplusQuota;
    private String modelTypes;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
