package com.kojikoji.gpt.data.domain.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.kojikoji.gpt.data.domain.auth.model.entity.AuthStateEntity;
import com.kojikoji.gpt.data.domain.auth.model.vo.AuthTypeVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.util.*;

/**
 * @ClassName AbstractAuthService
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/15 16:10
 * @Version
 */
@Slf4j
public abstract class AbstractAuthService implements IAuthService {

    private static final String defaultBase64EncodedSecretKey = "A2A^dasd";
    private final String base64EncodedSecretKey = Base64.encodeBase64String(defaultBase64EncodedSecretKey.getBytes());
    private final Algorithm algorithm = Algorithm.HMAC256(Base64.decodeBase64(Base64.encodeBase64String(defaultBase64EncodedSecretKey.getBytes())));

    @Override
    public AuthStateEntity doLogin(String code) {
        // 1.不是四位有效数字，返回验证码无效
        if (!code.matches("^\\d{4}$")) {
            log.info("鉴权，用户输入的验证码无效 {}", code);
            return AuthStateEntity.builder()
                    .code(AuthTypeVO.A0002.getCode())
                    .info(AuthTypeVO.A0001.getInfo())
                    .build();
        }

        // 2.校验判断，未成功直接返回
        AuthStateEntity authStateEntity = this.checkCode(code);
        if (!authStateEntity.getCode().equals(AuthTypeVO.A0000.getCode())) {
            log.info("鉴权，失败 {}", authStateEntity.getCode());
            return authStateEntity;
        }

        // 3.获取 Token 并返回
        Map<String, Object> claim = new HashMap<>();
        claim.put("openId", authStateEntity.getOpenId());
        String token = encode(authStateEntity.getOpenId(), 7 * 24 * 60 * 60 * 1000, claim);
        authStateEntity.setToken(token);

        return authStateEntity;
    }

    protected abstract AuthStateEntity checkCode(String code);

    /**
     *  构建jwt字符串
     *  包括三部分
     *  1. header
     *  - 字符串类型，一般是"JWT"
     *  - 加密算法，"HS256"或者其他
     *  一般都是固定的没有什么变化
     *  2. payload
     *  四个标准字段
     *  iat：签发时间，jwt什么时候生成的
     *  jti：JWT的唯一标识
     *  iss：签发人，一般是username或者userId
     *  exp：过期时间
     */
    protected String encode(String issuer, long ttlMillis, Map<String ,Object> claims) {
        // iss - 签发人，ttl - 签发时间，claim-存储的一些非隐私信息
        if (Objects.isNull(claims)) {
            claims = new HashMap<>();
        }
        long currMillis = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                // 载荷
                .setClaims(claims)
                // 唯一标识
                .setId(UUID.randomUUID().toString())
                // 签发时间
                .setIssuedAt(new Date(currMillis))
                // 签发人
                .setSubject(issuer)
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey);
        if (ttlMillis >= 0) {
            long expTime = currMillis + ttlMillis;
            Date expDate = new Date(expTime);
            builder.setExpiration(expDate);
        }
        return builder.compact();
    }

    // 获取载荷所有键值对
    protected Claims decode(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(base64EncodedSecretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    // 判断jwtToken是否合法
    protected boolean isVerify(String jwtToken) {
        try {
            // 判断合法的标准，1.头部和荷载部分未被篡改 2.没有过期
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(jwtToken);
            return true;
        } catch (Exception e) {
            log.error("jwt isVerify err", e);
            return false;
        }
    }
}
