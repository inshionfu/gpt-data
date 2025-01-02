package com.kojikoji.gpt.data.domain.order.model.entity;

import com.kojikoji.gpt.data.types.enums.OpenAIProductEnableModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName ProductEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/19 21:06
 * @Version
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    private Integer productId;
    private String productName;
    private String productDesc;
    private Integer quota;
    private BigDecimal price;
    private OpenAIProductEnableModel enable;

    public boolean isAvailable() {
        return OpenAIProductEnableModel.OPEN.equals(enable);
    }
}
