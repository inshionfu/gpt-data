package com.kojikoji.gpt.data.trigger.job;

import com.alipay.api.domain.LeaseItemSkuDTO;
import com.google.common.eventbus.EventBus;
import com.kojikoji.gpt.data.domain.order.service.IOrderService;
import com.xxl.job.core.handler.annotation.XxlJob;
import groovyjarjarpicocli.CommandLine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName OrderReplenishmentJob
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2025/1/3 10:47
 * @Version
 */

@Slf4j
@Component
public class OrderReplenishmentJob {

    @Resource
    private IOrderService orderService;

    @Resource
    private EventBus eventBus;

    /**
     * 执行订单补货，超时3分钟，已支付，待发货未发货的订单
     */
    @XxlJob("orderReplenishment")
    public void orderReplenishment() {
        try {
            List<String> orderIds = orderService.queryReplenishmentOrder();
            if (orderIds.isEmpty()) {
                log.info("执行订单补货，不存在超时未发货订单");
            }
            for (String orderId : orderIds) {
                log.info("执行订单补货，orderId {}", orderId);
                eventBus.post(orderId);
            }
        } catch (Exception e) {
            log.info("补偿发货，异常", e);
        }
    }
}
