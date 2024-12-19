package com.kojikoji.gpt.data.domain.openai.service.rule.factory;

import com.kojikoji.gpt.data.domain.openai.annotation.LogicStrategy;
import com.kojikoji.gpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kojikoji.gpt.data.domain.openai.service.rule.ILogicFilter;
import jdk.internal.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName DefaultLogicFactory
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/16 16:09
 * @Version
 */

@Service
public class DefaultLogicFactory {

    public Map<String, ILogicFilter<UserAccountQuotaEntity>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<UserAccountQuotaEntity>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (Objects.nonNull(strategy)) {
                logicFilterMap.put(strategy.logicModel().getCode(), logic);
            }
        });
    }

    public Map<String, ILogicFilter<UserAccountQuotaEntity>> openLogicFilter() {
        return logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {
        NULL("NULL", "放行不用过滤"),
        ACCESS_LIMIT("ACCESS_LIMIT", "访问次数过滤"),
        SENSITIVE_WORD("SENSITIVE_WORD", "敏感词过滤"),
        ACCOUNT_STATUS("ACCOUNT_STATUS", "账户状态过滤"),
        MODEL_TYPE("MODEL_TYPE", "模型类型过滤"),
        USER_QUOTA("USER_QUOTA", "用户余额过滤"),
        ;
        private String code;
        private String info;
    }
}
