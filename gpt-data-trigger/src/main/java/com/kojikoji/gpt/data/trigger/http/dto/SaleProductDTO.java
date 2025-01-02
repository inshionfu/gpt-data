package com.kojikoji.gpt.data.trigger.http.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName SaleProductDTO
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/30 16:41
 * @Version
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleProductDTO {

    /** 商品ID */
    private Integer productId;
    /** 商品名称 */
    private String productName;
    /** 商品描述 */
    private String productDesc;
    /** 商品余额 */
    private Integer quota;
    /** 商品价格 */
    private BigDecimal price;

}
