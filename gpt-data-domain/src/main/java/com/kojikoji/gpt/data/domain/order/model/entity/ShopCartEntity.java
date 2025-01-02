package com.kojikoji.gpt.data.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @ClassName ShopCartEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/19 19:38
 * @Version
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopCartEntity {
    /* 用户微信唯一ID */
    private String openid;
    /* 商品ID */
    private Integer productId;
}
