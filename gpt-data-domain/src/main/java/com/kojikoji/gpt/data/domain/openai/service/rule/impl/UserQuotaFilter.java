package com.kojikoji.gpt.data.domain.openai.service.rule.impl;

import com.alibaba.fastjson.JSON;
import com.kojikoji.gpt.data.domain.openai.annotation.LogicStrategy;
import com.kojikoji.gpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kojikoji.gpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.kojikoji.gpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kojikoji.gpt.data.domain.openai.model.vo.LogicCheckTypeVO;
import com.kojikoji.gpt.data.domain.openai.repository.IOpenAiRepository;
import com.kojikoji.gpt.data.domain.openai.service.rule.ILogicFilter;
import com.kojikoji.gpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.CookieJar;
import org.dom4j.rule.Rule;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName UserQuotaFilter
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/18 20:14
 * @Version
 */

@Slf4j
@Component
@LogicStrategy(logicModel = DefaultLogicFactory.LogicModel.USER_QUOTA)
public class UserQuotaFilter implements ILogicFilter<UserAccountQuotaEntity> {
    
    @Resource
    private IOpenAiRepository openAiRepository;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountQuotaEntity data) throws Exception {
        int surplusQuota = data.getSurplusQuota();
        String openid = data.getOpenid();
        log.info("过滤-账户余额: 余额 {}", surplusQuota);
        if (surplusQuota <= 0) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.REFUSE)
                    .info("个人账户，总额度【" + data.getTotalQuota() + "】次，已耗尽！")
                    .data(chatProcess)
                    .build();
        }
        int updateCount = openAiRepository.subAccountQuota(openid);
        if (updateCount != 0) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }
        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicCheckTypeVO.REFUSE)
                .info("个人账户，总额度【" + data.getTotalQuota() + "】次，已耗尽！")
                .data(chatProcess)
                .build();
    }
}
