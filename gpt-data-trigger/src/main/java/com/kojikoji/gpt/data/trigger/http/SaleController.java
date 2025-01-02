package com.kojikoji.gpt.data.trigger.http;

import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.google.common.eventbus.EventBus;
import com.kojikoji.gpt.data.domain.auth.service.IAuthService;
import com.kojikoji.gpt.data.domain.order.model.entity.PayOrderEntity;
import com.kojikoji.gpt.data.domain.order.model.entity.ProductEntity;
import com.kojikoji.gpt.data.domain.order.model.entity.ShopCartEntity;
import com.kojikoji.gpt.data.domain.order.service.IOrderService;
import com.kojikoji.gpt.data.trigger.http.dto.SaleProductDTO;
import com.kojikoji.gpt.data.types.common.Constants;
import com.kojikoji.gpt.data.types.model.Response;
import com.kojikoji.gpt.data.types.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName SaleController
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/28 11:28
 * @Version
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sale/")
public class SaleController {

    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;

    @Resource
    private IOrderService orderService;

    @Resource
    private IAuthService authService;

    @Resource
    private EventBus eventBus;

    @GetMapping("product_list")
    public Response<List<SaleProductDTO>> queryProductList(@RequestHeader("Authorization") String token) {
        String openid = "";
        try {
            // 1.token校验
            boolean auth = authService.checkToken(token);
            if (!auth) {
                log.info("非法token {}", token);
                return Response.<List<SaleProductDTO>>builder()
                        .code(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .info(Constants.ResponseCode.TOKEN_ERROR.getInfo())
                        .build();
            }
            // 3.获取结果
            List<ProductEntity> products = orderService.getProducts();
            List<SaleProductDTO> productDTOList = products.stream()
                    .map(entity -> SaleProductDTO.builder()
                            .productId(entity.getProductId())
                            .productName(entity.getProductName())
                            .productDesc(entity.getProductDesc())
                            .quota(entity.getQuota())
                            .price(entity.getPrice())
                            .build())
                    .collect(Collectors.toList());
            return Response.<List<SaleProductDTO>>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(productDTOList)
                    .build();
        } catch (Exception e) {
            log.info("创建订单, 异常 openId {} ", openid, e);
            return Response.<List<SaleProductDTO>>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @PostMapping("create_order")
    public Response<String> createOrder(@RequestHeader("Authorization") String token, @RequestParam Integer productId) {
        String openid = "";
        try {
            log.info("创建订单, 请求开始, productId: {} token {}", productId, token);
            // 1.token校验
            boolean auth = authService.checkToken(token);
            if (!auth) {
                log.info("非法token {}", token);
                return Response.<String>builder()
                        .code(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .info(Constants.ResponseCode.TOKEN_ERROR.getInfo())
                        .build();
            }
            // 2.token解析
            openid = authService.openid(token);
            if (StringUtils.isEmpty(openid)) {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.TOKEN_ERROR.getCode())
                        .info(Constants.ResponseCode.TOKEN_ERROR.getInfo())
                        .build();
            }
            log.info("创建订单开始, openId {}, productId: {}", openid, productId);

            // 3.创建订单
            ShopCartEntity shopCart = ShopCartEntity.builder()
                    .openid(openid)
                    .productId(productId)
                    .build();
            PayOrderEntity order = orderService.createOrder(shopCart);
            log.info("创建订单完成, openId {}, productId: {} url: {}", openid, productId, order.getPayUrl());
            return Response.<String>builder()
                    .data(order.getPayUrl())
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .build();
        } catch (Exception e) {
            log.info("创建订单, 异常 openId {}, productId: {} ", openid, productId, e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @PostMapping("pay_notify")
    public String payNotify(HttpServletRequest request) {
        try {
            log.info("支付回调，消息接收 {}", request.getParameter("trade_status"));
            if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
                Map<String, String[]> requestParams = request.getParameterMap();
                Map<String, String> params = requestParams.keySet().stream()
                        .collect(Collectors.toMap(
                                k -> k,
                                request::getParameter,
                                (a, b) -> a
                        ));
                String tradeNo = params.get("out_trade_no");
                String gmtPayment = params.get("gmt_payment");
                String alipayTradeNo = params.get("trade_no");
                String sign = params.get("sign");
                String totalAmount = params.get("total_amount");
                String content = AlipaySignature.getSignCheckContentV1(params);

                boolean signature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8");
                if (signature) {
                    log.info("支付回调，交易名称 {}", params.get("subject"));
                    log.info("支付回调，交易状态 {}", params.get("trade_status"));
                    log.info("支付回调，支付宝交易凭证号 {}", params.get("trade_no"));
                    log.info("支付回调，商户订单号 {}", params.get("out_trade_no"));
                    log.info("支付回调，交易金额 {}", params.get("total_amount"));
                    log.info("支付回调，买家在支付宝唯一id {}", params.get("buyer_id"));
                    log.info("支付回调，买家付款时间 {}", params.get("gmt_payment"));
                    log.info("支付回调，买家付款金额 {}", params.get("buyer_pay_amount"));
                    log.info("支付回调，支付回调，更新订单 {}", tradeNo);
                    // 更新订单已支付
                    orderService.changeOrderPaySuccess(tradeNo, alipayTradeNo, new BigDecimal(totalAmount), DateUtils.parseDate(gmtPayment));
                    // 推送消息【仿MQ】
                    eventBus.post(tradeNo);
                }
                return "success";
            }
            log.error("回调失败，trade_status {}", request.getParameter("trade_status"));
            return "false";
        } catch (Exception e) {
            log.error("支付回调，处理失败", e);
            return "false";
        }
    }
}
