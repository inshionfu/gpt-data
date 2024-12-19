package com.kojikoji.gpt.data.infrastructure.repository;

import com.kojikoji.gpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kojikoji.gpt.data.domain.openai.model.vo.UserAccountStatusVO;
import com.kojikoji.gpt.data.domain.openai.repository.IOpenAiRepository;
import com.kojikoji.gpt.data.infrastructure.dao.IUserAccountDao;
import com.kojikoji.gpt.data.infrastructure.po.UserAccountPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName OpenAiRepository
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/18 20:37
 * @Version
 */
@Repository
public class OpenAiRepository implements IOpenAiRepository {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiRepository.class);
    @Resource
    private IUserAccountDao userAccountDao;

    @Override
    public int subAccountQuota(String openid) {
        return userAccountDao.subAccountQuota(openid);
    }

    @Override
    public UserAccountQuotaEntity queryUserAccount(String openid) {
        logger.info("queryUserAccount openid: {}", openid);
        UserAccountPO userAccountPO = userAccountDao.queryUserAccount(openid);
        if (Objects.isNull(userAccountPO)) {
            return null;
        }
        return UserAccountQuotaEntity.builder()
                .openid(userAccountPO.getOpenid())
                .totalQuota(userAccountPO.getTotalQuota())
                .surplusQuota(userAccountPO.getSurplusQuota())
                .userAccountStatusVO(UserAccountStatusVO.get(userAccountPO.getStatus()))
                .allowModelTypeList(UserAccountQuotaEntity.genModelTypes(userAccountPO.getModelTypes()))
                .build();
    }
}
