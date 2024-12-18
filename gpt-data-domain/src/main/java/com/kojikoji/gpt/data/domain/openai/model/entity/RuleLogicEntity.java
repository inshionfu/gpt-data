package com.kojikoji.gpt.data.domain.openai.model.entity;

import com.kojikoji.gpt.data.domain.openai.model.vo.LogicCheckTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName RuleLogicEntity
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/16 15:54
 * @Version
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleLogicEntity<T> {
    private LogicCheckTypeVO type;
    private String info;
    private T data;
}
