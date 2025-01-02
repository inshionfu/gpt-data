package com.kojikoji.gpt.data.domain.order.model.entity;

import com.fasterxml.jackson.databind.deser.impl.PropertyValue;
import com.kojikoji.gpt.data.domain.order.model.VO.OrderStatusVO;
import com.kojikoji.gpt.data.domain.order.model.VO.PayTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName OrderEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/19 21:18
 * @Version
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    /* 订单编号 */
    private String orderId;
    /* 下单时间 */
    private Date orderTime;
    /* 订单状态 */
    private OrderStatusVO orderStatus;
    /* 订单金额 */
    private BigDecimal totalAmount;
    /* 支付类型 */
    private PayTypeVO payTypeVO;
}
