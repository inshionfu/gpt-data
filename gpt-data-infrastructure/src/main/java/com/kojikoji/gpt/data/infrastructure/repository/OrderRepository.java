package com.kojikoji.gpt.data.infrastructure.repository;

import com.alibaba.fastjson.JSON;
import com.kojikoji.gpt.data.domain.openai.model.vo.UserAccountStatusVO;
import com.kojikoji.gpt.data.domain.order.model.VO.PayStatusVO;
import com.kojikoji.gpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import com.kojikoji.gpt.data.domain.order.model.entity.*;
import com.kojikoji.gpt.data.domain.order.repository.IOrderRepository;
import com.kojikoji.gpt.data.domain.order.service.OrderService;
import com.kojikoji.gpt.data.infrastructure.dao.IOrderDao;
import com.kojikoji.gpt.data.infrastructure.dao.IProductDao;
import com.kojikoji.gpt.data.infrastructure.dao.IUserAccountDao;
import com.kojikoji.gpt.data.infrastructure.po.OrderPO;
import com.kojikoji.gpt.data.infrastructure.po.OrderPO.OrderPOBuilder;
import com.kojikoji.gpt.data.infrastructure.po.ProductPO;
import com.kojikoji.gpt.data.infrastructure.po.UserAccountPO;
import com.kojikoji.gpt.data.types.enums.OpenAIProductEnableModel;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @ClassName OrderRepository
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/28 16:20
 * @Version
 */
@Slf4j
@Repository
public class OrderRepository implements IOrderRepository {

    @Resource
    private IOrderDao orderDao;
    
    @Resource
    private IProductDao productDao;

    @Resource
    private IUserAccountDao userAccountDao;

    @Override
    public UnpaidOrderEntity queryUnpaidOrder(ShopCartEntity shopCartEntity) {
        String openid = shopCartEntity.getOpenid();
        Integer productId = shopCartEntity.getProductId();
        OrderPO req = OrderPO.builder()
                .openid(openid)
                .productId(productId)
                .build();
        OrderPO orderPO = orderDao.queryUnpaidOrder(req);
        log.info("OrderPO: {}", orderPO);
        if (Objects.isNull(orderPO)) {
            return null;
        }

        return UnpaidOrderEntity.builder()
                .openid(shopCartEntity.getOpenid())
                .totalAmount(orderPO.getTotalAmount())
                .productName(orderPO.getProductName())
                .payUrl(orderPO.getPayUrl())
                .payStatus(PayStatusVO.of(orderPO.getPayStatus()))
                .build();
    }

    @Override
    public ProductEntity queryProduct(Integer productId) {
       ProductPO productPO = productDao.queryProduct(productId);
       if (Objects.isNull(productPO)) {
            return null;
       }
       return ProductEntity.builder()
                .productId(productPO.getProductId())
                .productName(productPO.getProductName())
                .productDesc(productPO.getProductDesc())
                .quota(productPO.getQuota())
                .price(productPO.getPrice())
                .enable(OpenAIProductEnableModel.of(productPO.getIsEnabled()))
                .build();
    }

    @Override
    public List<ProductEntity> queryProducts() {
        List<ProductPO> productPOList = productDao.queryProducts();
        List<ProductEntity> productEntities = productPOList.stream()
                .map(po -> ProductEntity.builder()
                        .productId(po.getProductId())
                        .productName(po.getProductName())
                        .productDesc(po.getProductDesc())
                        .quota(po.getQuota())
                        .price(po.getPrice())
                        .build())
                .collect(Collectors.toList());

        return productEntities;
    }

    @Override
    public void saveOrder(CreateOrderAggregate createOrderAggregate) {
        String openid = createOrderAggregate.getOpenid();
        OrderEntity order = createOrderAggregate.getOrder();
        if (Objects.isNull(order)) {
            log.info("error save order, empty order, openid {}", openid);
            return;
        }
        ProductEntity product = createOrderAggregate.getProduct();
        if (Objects.isNull(product)) {
            log.info("error save order, empty product, openid {}", openid);
            return;
        }
        OrderPO orderPo = OrderPO.builder()
            .openid(openid)
            .productId(product.getProductId())
            .productName(product.getProductName())
            .productQuota(product.getQuota())
            .orderId(order.getOrderId())
            .orderTime(order.getOrderTime())
            .orderStatus(order.getOrderStatus().getCode())
            .totalAmount(order.getTotalAmount())
            .payType(order.getPayTypeVO().getCode())
            .payStatus(PayStatusVO.WAIT.getCode())
            .build();
        
        orderDao.saveOrder(orderPo);
    }

    @Override
    public void updateOrderPayInfo(PayOrderEntity payOrderEntity) {
        OrderPO po = OrderPO.builder()
                .openid(payOrderEntity.getOpenid())
                .orderId(payOrderEntity.getOrderId())
                .payUrl(payOrderEntity.getPayUrl())
                .payStatus(payOrderEntity.getPayStatus().getCode())
                .build();
        log.info("po: {}", JSON.toJSONString(po));
        orderDao.updateOrderPayInfo(po);
    }

    @Override
    public void changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime) {
        OrderPO po = OrderPO.builder()
                .orderId(orderId)
                .payStatus(PayStatusVO.SUCCESS.getCode())
                .transactionId(transactionId)
                .payAmount(totalAmount)
                .payTime(payTime)
                .build();
        orderDao.changeOrderPaySuccess(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deliverGoods(String orderId) {
        // 1.获取订单信息
        OrderPO order = orderDao.queryOrder(orderId);
        if (Objects.isNull(order)) {
            throw new RuntimeException("invalid orderId " + orderId);
        }

        // 2.修改发货状态
        int updateCount = orderDao.changeOrderDeliverStatus(orderId);
        if (1 != updateCount) {
            throw new RuntimeException("updateOrderStatusDeliverGoodsCount update count is not equal 1");
        }

        // 3.发货
        // 3.1 查询用户账户信息
        UserAccountPO userAccount = userAccountDao.queryUserAccount(orderId);
        UserAccountPO plusAccountReq = UserAccountPO.builder()
                .openid(order.getOpenid())
                .build();
        if (Objects.nonNull(userAccount)) {
            plusAccountReq.setSurplusQuota(userAccount.getSurplusQuota() + order.getProductQuota());
            plusAccountReq.setTotalQuota(userAccount.getTotalQuota() + order.getProductQuota());
            int plusCount = userAccountDao.addAccountQuota(plusAccountReq);
            if (1 != plusCount) {
                throw new RuntimeException("addAccountQuota update count is not equal 1");
            }
        } else {
            plusAccountReq.setSurplusQuota(order.getProductQuota());
            plusAccountReq.setTotalQuota(order.getProductQuota());
            plusAccountReq.setModelTypes("glm-4-flash");
            plusAccountReq.setStatus(UserAccountStatusVO.AVAILABLE.getCode());
            userAccountDao.insertAccount(plusAccountReq);
        }

    }
}
