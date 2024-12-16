package com.kojikoji.gpt.data.domain.auth;

import com.kojikoji.gpt.data.domain.auth.model.entity.AuthStateEntity;

public interface IAuthService {

    AuthStateEntity doLogin(String code);

    boolean checkToken(String token);
}
