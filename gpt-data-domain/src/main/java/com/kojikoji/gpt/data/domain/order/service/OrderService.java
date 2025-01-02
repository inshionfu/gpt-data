package com.kojikoji.gpt.data.domain.order.service;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.kojikoji.gpt.data.domain.order.model.VO.OrderStatusVO;
import com.kojikoji.gpt.data.domain.order.model.VO.PayStatusVO;
import com.kojikoji.gpt.data.domain.order.model.VO.PayTypeVO;
import com.kojikoji.gpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import com.kojikoji.gpt.data.domain.order.model.entity.OrderEntity;
import com.kojikoji.gpt.data.domain.order.model.entity.PayOrderEntity;
import com.kojikoji.gpt.data.domain.order.model.entity.ProductEntity;

@Slf4j
@Service
public class OrderService extends AbstractOrderService {

    @Resource
    private AlipayClient alipayClient;
    @Value("${alipay.return_url}")
    private String returnUrl;
    @Value("${alipay.notify_url}")
    private String notifyUrl;

    @Override
    protected OrderEntity doSaveOrder(String openid, ProductEntity productEntity) {
        OrderEntity orderEntity = OrderEntity.builder()
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .orderTime(new Date())
                    .orderStatus(OrderStatusVO.CREATE)
                    .totalAmount(productEntity.getPrice())
                    .payTypeVO(PayTypeVO.ZFB_SANDBOX)
                    .build();
        CreateOrderAggregate createOrderAggregate = CreateOrderAggregate.builder()
            .openid(openid)
            .product(productEntity)
            .order(orderEntity)
            .build();
        orderRepository.saveOrder(createOrderAggregate);
        return orderEntity;
    }

    @Override
    protected PayOrderEntity doPrePayOrder(String openid, String orderId, String productName, BigDecimal totalAmount) throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        log.info("notify: {} return: {}", notifyUrl, returnUrl);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("total_amount", totalAmount.toString());
        bizContent.put("subject", productName);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toJSONString());

        String form = alipayClient.pageExecute(request).getBody();
        PayOrderEntity payOrderEntity = PayOrderEntity.builder()
                .openid(openid)
                .orderId(orderId)
                .payUrl(form)
                .payStatus(PayStatusVO.WAIT)
                .build();

        log.info("生成支付单 {}", JSON.toJSONString(payOrderEntity));
        orderRepository.updateOrderPayInfo(payOrderEntity);
        return payOrderEntity;
    }

    @Override
    public void changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime) {
        orderRepository.changeOrderPaySuccess(orderId, transactionId, totalAmount, payTime);
    }

    @Override
    public void deliverGoods(String orderId) {
        orderRepository.deliverGoods(orderId);
    }

    @Override
    public List<ProductEntity> getProducts() {
        return orderRepository.queryProducts();
    }

}
