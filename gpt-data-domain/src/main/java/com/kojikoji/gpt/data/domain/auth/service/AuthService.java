package com.kojikoji.gpt.data.domain.auth.service;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.kojikoji.gpt.data.domain.auth.model.entity.AuthStateEntity;
import com.kojikoji.gpt.data.domain.auth.model.vo.AuthTypeVO;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName AuthService
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/15 16:39
 * @Version
 */
@Service
public class AuthService extends AbstractAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Resource
    private Cache<String, String> codeCache;

    @Override
    protected AuthStateEntity checkCode(String code) {
        // 获取验证码校验
        String openId = codeCache.getIfPresent(code);
        if (StringUtils.isBlank(openId)) {
            logger.info("鉴权，用户输入的验证码不存在: {}", code);
            return AuthStateEntity.builder()
                    .code(AuthTypeVO.A0001.getCode())
                    .info(AuthTypeVO.A0001.getInfo())
                    .build();
        }

        // 移除缓存Key值
        codeCache.invalidate(openId);
        codeCache.invalidate(code);

        // 验证码校验成功
        return AuthStateEntity.builder()
                .code(AuthTypeVO.A0000.getCode())
                .info(AuthTypeVO.A0000.getInfo())
                .openId(openId)
                .build();
    }

    @Override
    public boolean checkToken(String token) {
        return isVerify(token);
    }

    @Override
    public String openid(String token) {
        Claims claims = decode(token);
        for (String k : claims.keySet()) {
            logger.info("k: {}, v: {}", k, claims.get(k));
        }
        logger.info("claims: {} size: {}", JSON.toJSONString(claims), claims.size());
        return claims.get("openId").toString();
    }
}
