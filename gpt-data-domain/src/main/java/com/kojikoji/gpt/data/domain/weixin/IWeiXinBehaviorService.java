package com.kojikoji.gpt.data.domain.weixin;

import com.kojikoji.gpt.data.domain.weixin.model.entity.UserBehaviorMessageEntity;

public interface IWeiXinBehaviorService {
    String acceptUserBehavior(UserBehaviorMessageEntity userBehaviorMessageEntity);
}
