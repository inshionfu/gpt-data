package com.kojikoji.gpt.data.domain.order.model.entity;

import com.kojikoji.gpt.data.domain.order.model.VO.PayStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName PayOrderEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/19 19:34
 * @Version
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderEntity {
    /* 用户id */
    private String openid;
    /* 订单id */
    private String orderId;
    /* 支付地址：创建支付后，获得的url地址 */
    private String payUrl;
    /* 支付状态：0-等待支付 1-支付完成 2-支付失败 3-放弃 */
    private PayStatusVO payStatus;
}
