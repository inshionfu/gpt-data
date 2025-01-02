package com.kojikoji.gpt.data.domain.order.model.aggregates;

import com.kojikoji.gpt.data.domain.order.model.entity.OrderEntity;
import com.kojikoji.gpt.data.domain.order.model.entity.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {
    private String openid;
    private ProductEntity product;
    private OrderEntity order;
}
