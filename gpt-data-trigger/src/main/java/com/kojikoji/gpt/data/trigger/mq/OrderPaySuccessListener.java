package com.kojikoji.gpt.data.trigger.mq;

import com.google.common.eventbus.Subscribe;
import com.kojikoji.gpt.data.domain.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName OrderPaySuccessListener
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/28 15:18
 * @Version
 */
@Slf4j
@Component
public class OrderPaySuccessListener {

    @Resource
    private IOrderService orderService;

    @Subscribe
    public void handleEvent(String orderId) {
        try {
            log.error("支付完成，发货并记录，开始，订单：{}", orderId);
            orderService.deliverGoods(orderId);
        } catch (Exception e) {
            log.error("支付完成，发货并记录，失败，订单：{}", orderId, e);
        }
    }
}
