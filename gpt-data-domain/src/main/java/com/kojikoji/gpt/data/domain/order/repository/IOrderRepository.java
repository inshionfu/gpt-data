package com.kojikoji.gpt.data.domain.order.repository;

import com.kojikoji.gpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import com.kojikoji.gpt.data.domain.order.model.entity.PayOrderEntity;
import com.kojikoji.gpt.data.domain.order.model.entity.ProductEntity;
import com.kojikoji.gpt.data.domain.order.model.entity.ShopCartEntity;
import com.kojikoji.gpt.data.domain.order.model.entity.UnpaidOrderEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IOrderRepository {

    UnpaidOrderEntity queryUnpaidOrder(ShopCartEntity shopCart);

    ProductEntity queryProduct(Integer productId);

    List<ProductEntity> queryProducts();

    void saveOrder(CreateOrderAggregate createOrderAggregate);

    void updateOrderPayInfo(PayOrderEntity payOrderEntity);

    void changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime);

    void deliverGoods(String orderId);
}
