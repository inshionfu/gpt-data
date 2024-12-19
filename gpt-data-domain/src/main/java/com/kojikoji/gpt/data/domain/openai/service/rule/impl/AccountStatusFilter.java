package com.kojikoji.gpt.data.domain.openai.service.rule.impl;

import com.kojikoji.gpt.data.domain.openai.annotation.LogicStrategy;
import com.kojikoji.gpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kojikoji.gpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.kojikoji.gpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kojikoji.gpt.data.domain.openai.model.vo.LogicCheckTypeVO;
import com.kojikoji.gpt.data.domain.openai.model.vo.UserAccountStatusVO;
import com.kojikoji.gpt.data.domain.openai.service.rule.ILogicFilter;
import com.kojikoji.gpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

/**
 * @ClassName AccountStatisFilter
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/18 19:49
 * @Version
 */
@Slf4j
@Component
@LogicStrategy(logicModel = DefaultLogicFactory.LogicModel.ACCOUNT_STATUS)
public class AccountStatusFilter implements ILogicFilter<UserAccountQuotaEntity> {
    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountQuotaEntity data) throws Exception {
        log.info("过滤-账户状态：原因: {}", data.getUserAccountStatusVO());
        // 账户可用，直接放行
        if (UserAccountStatusVO.AVAILABLE.equals(data.getUserAccountStatusVO())) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }

        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .info("您的账户已冻结，暂时不可使用，可以联系客户解冻账户")
                .type(LogicCheckTypeVO.REFUSE)
                .build();
    }
}
