package com.kojikoji.gpt.data.domain.openai.model.entity;

import com.kojikoji.gpt.data.domain.openai.model.vo.UserAccountStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;

import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName UserAccountQuotaEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/18 19:55
 * @Version
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountQuotaEntity {
    /* 用户id */
    private String openid;
    /* 总量额度 */
    private Integer totalQuota;
    /* 剩余额度 */
    private Integer surplusQuota;
    /* 账户状态 */
    private UserAccountStatusVO userAccountStatusVO;
    /* 模型类型 */
    private Set<String> allowModelTypeList;

    public static Set<String> genModelTypes(String modelTypes) {
        String[] modelTypesArray = modelTypes.split(",");
        return Arrays.stream(modelTypesArray).collect(Collectors.toSet());
    }
}
