package com.kojikoji.gpt.data.domain.order.service;

import com.kojikoji.gpt.data.domain.order.model.entity.PayOrderEntity;
import com.kojikoji.gpt.data.domain.order.model.entity.ProductEntity;
import com.kojikoji.gpt.data.domain.order.model.entity.ShopCartEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IOrderService {

    /**
     * 用户下单，通过购物车信息，返回下单后的支付单
     */
    PayOrderEntity createOrder(ShopCartEntity shopCartEntity);

    /**
     * 用户支付完成，修改订单状态
     */
    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime);

    /**
     * 用户下单完成，发货
     * @param orderId
     */
    void deliverGoods(String orderId);

    List<ProductEntity> getProducts();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);

    List<String> queryReplenishmentOrder();

    List<String> queryNoPayNotifyOrder();
}
