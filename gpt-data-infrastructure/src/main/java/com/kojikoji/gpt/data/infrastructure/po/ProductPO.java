package com.kojikoji.gpt.data.infrastructure.po;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPO {
    /* 自增ID */
    private Long id;
    /* 商品ID */
    private Integer productId;
    /* 商品名称 */
    private String productName;
    /* 商品描述 */
    private String productDesc;
    /* 额度次数 */
    private Integer quota;
    /* 商品价格 */
    private BigDecimal price;
    /* 商品排序 */
    private Integer sort;
    /* 是否有效：0-无效 1-有效 */
    private Integer isEnabled;
    /* 创建时间 */
    private Date createTime;
    /* 更新时间 */
    private Date updateTime;
}
