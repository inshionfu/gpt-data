package com.kojikoji.gpt.data.domain.openai.repository;

import com.kojikoji.gpt.data.domain.openai.model.entity.UserAccountQuotaEntity;

public interface IOpenAiRepository {

    int subAccountQuota(String openid);

    UserAccountQuotaEntity queryUserAccount(String openid);
}
