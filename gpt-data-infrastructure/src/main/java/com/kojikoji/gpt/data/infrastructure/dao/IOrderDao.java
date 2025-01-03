package com.kojikoji.gpt.data.infrastructure.dao;

import com.kojikoji.gpt.data.infrastructure.po.OrderPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName IOrderDao
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/28 16:20
 * @Version
 */

@Mapper
public interface IOrderDao {

    OrderPO queryUnpaidOrder(OrderPO req);

    void saveOrder(OrderPO orderPO);

    void updateOrderPayInfo(OrderPO po);

    void changeOrderPaySuccess(OrderPO po);

    OrderPO queryOrder(String orderId);

    int changeOrderDeliverStatus(String orderId);

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);

    List<String> queryReplenishmentOrder();
}
