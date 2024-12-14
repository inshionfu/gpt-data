package com.kojikoji.gpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ChatGPTModel
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/12 14:18
 * @Version
 */

@Getter
@AllArgsConstructor
public enum GPTModel {
    CHATGLM_TURBO("chatglm_turbo", "适用于对知识量、推理能力、创造力要求较高的场景"),
    GLM_3_5_TURBO("glm-3-turbo", "适用于对知识量、推理能力、创造力要求较高的场景"),
    GLM_4("glm-4", "适用于复杂的对话交互和深度内容创作设计的场景"),
    GLM_4V("glm-4v", "根据输入的自然语言指令和图像信息完成任务，推荐使用 SSE 或同步调用方式请求接口"),
    GLM_4_Plus("glm-4-plus", "高智能旗舰: 性能全面提升，长文本和复杂任务能力显著增强"),
    GLM_4_0520("glm-4-0520", "高智能模型：适用于处理高度复杂和多样化的任务"),
    GLM_4_Lng("glm-4-long", "超长输入：专为处理超长文本和记忆型任务设计"),
    GLM_4_AirX("glm-4-airx", "极速推理：具有超快的推理速度和强大的推理效果"),
    GLM_4_Air("glm-4-air", "高性价比：推理能力和价格之间最平衡的模型"),
    GLM_4_FlashX("glm-4-flashx", "高速低价：Flash增强版本，超快推理速度。"),
    GLM_4_Flash("glm-4-flash", "免费调用：智谱AI首个免费API，零成本调用大模型。"),
    GLM_4_AllTools("glm-4-alltools", "Agent模型：自主规划和执行复杂任务"),
    COGVIEW_3("cogview-3", "根据用户的文字描述生成图像,使用同步调用方式请求接口");
    ;
    private final String code;
    public final String info;
}
