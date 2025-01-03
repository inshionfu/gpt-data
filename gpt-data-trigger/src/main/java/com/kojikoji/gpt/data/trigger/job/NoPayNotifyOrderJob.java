package com.kojikoji.gpt.data.trigger.job;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayAcquireCloseRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.google.common.eventbus.EventBus;
import com.kojikoji.gpt.data.domain.order.service.IOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName NoPayNotifyOrderJob
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2025/1/2 21:00
 * @Version
 */
@Slf4j
@Component
public class NoPayNotifyOrderJob {

    @Resource
    private IOrderService orderService;

    @Resource
    public AlipayClient alipayClient;

    @Resource
    private EventBus eventBus;

    /**
     * 回调异常补偿任务
     */
    @XxlJob("noPayNotifyOrder")
    public void noPayNotifyOrder() {
        try {
            if (Objects.isNull(orderService)) {
                log.info("定时任务，订单支付状态更新。应用未配置支付渠道，任务不执行");
                return;
            }

            List<String> orderIds = orderService.queryNoPayNotifyOrder();
            if (orderIds.isEmpty()) {
                log.info("定时任务，回调异常补偿，不存在支付未回调任务");
                return;
            }

            for (String orderId : orderIds) {
                AlipayTradeQueryRequest req = new AlipayTradeQueryRequest();
                AlipayTradeQueryModel model = new AlipayTradeQueryModel();
                model.setOutTradeNo(orderId);
                req.setBizModel(model);
                AlipayTradeQueryResponse resp = alipayClient.execute(req);
                if (!resp.getTradeStatus().equals("TRADE_SUCCESS")) {
                    log.info("定时任务，订单未支付成功 orderId {}", orderId);
                    continue;
                }

                String transactionId = resp.getAlipayStoreId();
                String payAmount = resp.getTotalAmount();
                BigDecimal totalAmount = new BigDecimal(payAmount);

                Date sendPayDate = resp.getSendPayDate();
                boolean success = orderService.changeOrderPaySuccess(orderId, transactionId, totalAmount, sendPayDate);
                if (success) {
                    log.info("定时任务, 变更订单成功 orderId {}", orderId);
                    eventBus.post(orderId);
                }
            }
        } catch (Exception e) {
            log.error("定时任务，回调补偿失败 ", e);
        }

    }

    /**
     * 测试
     * @throws Exception
     */
    @XxlJob("demoJobHandler")
    public void demoJobHandler() throws Exception {
        XxlJobHelper.log("XXL-JOB, Hello World!");

        for (int i = 0; i < 5; ++i) {
            XxlJobHelper.log("beat at " + i);
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
