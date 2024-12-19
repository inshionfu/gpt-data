package com.kojikoji.gpt.data.test.repository;

import com.alibaba.fastjson.JSON;
import com.kojikoji.gpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kojikoji.gpt.data.infrastructure.repository.OpenAiRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @ClassName TestUserAccountRepository
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/19 14:26
 * @Version
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserAccountRepository {
    @Resource
    private OpenAiRepository openAiRepository;

    @Test
    public void testQuery() {
        String openid = "oQ-436BQJ6KhjNzKDHVWMBva8wug";
        UserAccountQuotaEntity userAccountQuotaEntity = openAiRepository.queryUserAccount(openid);
        log.info(JSON.toJSONString(userAccountQuotaEntity));
    }
}
