package com.kojikoji.gpt.data.domain.order.model.entity;

import com.kojikoji.gpt.data.domain.order.model.VO.PayStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName UnpaidOrderEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/19 19:48
 * @Version
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidOrderEntity {
    /* 微信ID */
    private String openid;
    /* 订单ID */
    private String orderId;
    /* 订单金额 */
    private BigDecimal totalAmount;
    /* 商品名称 */
    private String productName;
    /* 支付地址：url */
    private String payUrl;
    /* 支付状态 */
    private PayStatusVO payStatus;
}
