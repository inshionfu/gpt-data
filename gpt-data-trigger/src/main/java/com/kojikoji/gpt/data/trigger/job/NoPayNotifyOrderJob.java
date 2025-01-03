package com.kojikoji.gpt.data.trigger.job;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayAcquireCloseRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.kojikoji.gpt.data.domain.order.service.IOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    private AlipayClient alipayClient;
    @Value("${alipay.merchant_private_key}")
    private String getMerchantPrivateKey;

    @XxlJob("noPayNotifyOrder")
    public void noPayNotifyOrder() {
        try {
            if (Objects.isNull(orderService)) {
                log.info("定时任务，订单支付状态更新。应用未配置支付渠道，任务不执行");
                return;
            }

            List<String> orderIds = orderService.queryTimeoutCloseOrderList();
            if (orderIds.isEmpty()) {
                log.info("定时任务，超时30min订单关闭，暂无超时未支付订单 orderIds is null");
                return;
            }

            for (String orderId : orderIds) {
                boolean status = orderService.changeOrderClose(orderId);
                log.info("关单 orderId {} 状态 {}", orderId, status);
                AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
                JSONObject bizContent = new JSONObject();
                bizContent.put("out_trade_no", orderId);
                request.setBizContent(bizContent.toString());

                AlipayTradeCloseResponse resp = alipayClient.execute(request);
                if (resp.isSuccess()) {
                    log.info("超时关单，定时任务，调用成功 orderId: {} zfb_trade_id {}", orderId, resp.getTradeNo());
                } else {
                    log.info("超时关单，定时任务，调用失败 orderId: {} zfb_trade_id {}", orderId, resp.getTradeNo());
                }
            }
        } catch (Exception e) {
            log.error("定时任务，超时15分钟订单关闭失败", e);
        }

    }

    @XxlJob("demoJobHandler")
    public void demoJobHandler() throws Exception {
        XxlJobHelper.log("XXL-JOB, Hello World!");

        for (int i = 0; i < 5; ++i) {
            XxlJobHelper.log("beat at " + i);
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
