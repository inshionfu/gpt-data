package com.kojikoji.gpt.data.domain.order.service;

import com.alipay.api.AlipayApiException;
import com.kojikoji.gpt.data.domain.order.model.VO.PayStatusVO;
import com.kojikoji.gpt.data.domain.order.model.entity.*;
import com.kojikoji.gpt.data.domain.order.repository.IOrderRepository;
import com.kojikoji.gpt.data.types.common.Constants;
import com.kojikoji.gpt.data.types.exception.GPTException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @ClassName AbstractOrderService
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/19 20:38
 * @Version
 */

@Slf4j
public abstract class AbstractOrderService implements IOrderService {

    @Resource
    protected IOrderRepository orderRepository;

    @Override
    public PayOrderEntity createOrder(ShopCartEntity shopCartEntity) {
        try {
            // 0.基础变量
            String openid = shopCartEntity.getOpenid();
            Integer productId = shopCartEntity.getProductId();
            // 1.查询未支付的订单，存在直接返回
            UnpaidOrderEntity unpaidOrderEntity = orderRepository.queryUnpaidOrder(shopCartEntity);
            if (Objects.nonNull(unpaidOrderEntity)) {
                if (unpaidOrderEntity.getPayStatus().equals(PayStatusVO.WAIT)) {
                    log.info("创建订单-存在，已生成支付宝，返回 openid: {} orderId: {} payUrl: {}", openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getPayUrl());
                    // 已生成url，直接返回
                    return PayOrderEntity.builder()
                            .orderId(unpaidOrderEntity.getOrderId())
                            .payUrl(unpaidOrderEntity.getPayUrl())
                            .payStatus(unpaidOrderEntity.getPayStatus())
                            .openid(openid)
                            .build();
                } else if (Objects.isNull(unpaidOrderEntity.getPayUrl())) {
                    log.info("创建订单-存在，未生成微信支付，返回 openid: {} orderId: {} proName: {}", openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getProductName());
                    PayOrderEntity payOrderEntity = this.doPrePayOrder(openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getProductName(), unpaidOrderEntity.getTotalAmount());
                    log.info("创建订单-存在，已生成微信支付，返回 openid: {} orderId: {} proName: {} payUrl: {}", openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getProductName(), unpaidOrderEntity.getPayUrl());
                    return payOrderEntity;
                }
            }

            // 2.查询商品
            ProductEntity product = orderRepository.queryProduct(productId);
            if (Objects.isNull(product) || !product.isAvailable()) {
                throw new GPTException(Constants.ResponseCode.ORDER_PRODUCT_ERR.getCode(), Constants.ResponseCode.ORDER_PRODUCT_ERR.getInfo());
            }

            // 3.保存订单
            OrderEntity orderEntity = this.doSaveOrder(openid, product);
            log.info("订单已保存 openid: {} orderId: {}", openid, orderEntity.getOrderId());

            // 4. 创建支付
            PayOrderEntity payOrderEntity = this.doPrePayOrder(openid, orderEntity.getOrderId(), product.getProductName(), orderEntity.getTotalAmount());
            log.info("创建订单-完成，生成支付单。openid: {} orderId: {} payUrl: {}", openid, orderEntity.getOrderId(), payOrderEntity.getPayUrl());
            return payOrderEntity;
        } catch (Exception e) {
            log.error("创建订单，异常，返回 openid: {} productId: {}", shopCartEntity.getOpenid(), shopCartEntity.getProductId(), e);
            throw new GPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }
    }

    protected abstract PayOrderEntity doPrePayOrder(String openid, String orderId, String productName, BigDecimal amountTotal) throws AlipayApiException;

    protected abstract OrderEntity doSaveOrder(String openid, ProductEntity productEntity);
}
