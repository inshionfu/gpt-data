package com.kojikoji.gpt.data.domain.openai.service.rule.impl;

import com.google.common.cache.Cache;
import com.kojikoji.gpt.data.domain.openai.annotation.LogicStrategy;
import com.kojikoji.gpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kojikoji.gpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.kojikoji.gpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kojikoji.gpt.data.domain.openai.model.vo.LogicCheckTypeVO;
import com.kojikoji.gpt.data.domain.openai.service.rule.ILogicFilter;
import com.kojikoji.gpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.common.value.qual.BottomVal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName AccessLimitFilter
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/16 16:07
 * @Version
 */

@Slf4j
@Component
@LogicStrategy(logicModel = DefaultLogicFactory.LogicModel.ACCESS_LIMIT)
public class AccessLimitFilter implements ILogicFilter<UserAccountQuotaEntity> {

    @Value("${app.config.limit-count:10}")
    private Integer limitCount;

    @Value("${app.config.white-list}")
    private String whiteListStr;

    @Resource
    private Cache<String, Integer> visitCache;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountQuotaEntity data) throws Exception {
        // 1.白名单用户直接放行
        if (chatProcess.isWhiteList(whiteListStr)) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }
        String openid = chatProcess.getOpenid();

        // 2.个人账户不为空，不做系统访问次数拦截
        if (Objects.isNull(data)) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }
        // 访问次数判断
        int visitCount = visitCache.get(openid, () -> 0);
        if (visitCount < limitCount) {
            visitCache.put(openid, visitCount + 1);
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }
        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .info("您今日的免费" + limitCount + "次，已耗尽")
                .type(LogicCheckTypeVO.REFUSE)
                .build();
    }
}
