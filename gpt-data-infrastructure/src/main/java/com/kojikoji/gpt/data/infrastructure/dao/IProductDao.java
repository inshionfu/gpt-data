package com.kojikoji.gpt.data.infrastructure.dao;

import com.kojikoji.gpt.data.infrastructure.po.ProductPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IProductDao {

    ProductPO queryProduct(Integer productId);

    List<ProductPO> queryProducts();
}
