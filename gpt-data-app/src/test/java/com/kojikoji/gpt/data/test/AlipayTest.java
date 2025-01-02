package com.kojikoji.gpt.data.test;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.AttributeSet;

/**
 * @ClassName AlipayTest
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/28 11:15
 * @Version
 */

@Slf4j
public class AlipayTest {

    public static final String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    public static final String appId = "2021000142698654";
    public static final String merchantPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCXIpv2vDcRhvbwvLElVq2LZG+tLfSFMZwX3DMGWWrdLeISzAiBMkJhtKBgmzDVlUd0/fE5S8vKaF7BfpaDdDLdQvHenELhhgh2ygZsgjxuNZNQE/bU5SqYO+hgDRv6BrlyWw4vsftytF6ecsDBuRDOOIT28jiQAA07TL6XkI2H3F8qxPTiq+7XXcUqxFGJKfTpxAYfX0Fs1NOFdkxScJw8C2t0ZZFOARaLAb2ePa5DohZ3hrW03rKgxhlmXmyAXWFeCKb0QT1w6uSm/GqsPonY/brGuUeeH5x1lIHIRCffmS2W08MW2ZBg5Wtuzr7ImyyJ6J54EVLw/frgWVAS7xU/AgMBAAECggEADF60zTkSeMjHJWUMSrYRdjs+OLpuLQimcf1/36Ep8fhzbH5uo8RtUnnqNn39PT3+n0C5oarmfyaTutqVjyTPlPSJv2KixrqlrrNSW0c9kvenmwJHTZ2EIvmkR1hLVed+/jEdtCGpJvZEWEukIzb2fSp7F4szvfWDH7tkThtyug/awmw1sJPD8ntl3A2ngoBKRl9qThEVbRzOmBbfdB8q2S/TDuwyufT2aV6AIXh7yc6J9LvlFQVxZMbHTk3nwSS8OY+EdIPI0fwrQ4XY6BbyjcRwYzSO6fwk2FowR47/qU71CpXuz4SWuqNq/uiEIV+x2utiulusa5bSbjVcVKO2oQKBgQDouS4Hg99Hb7X8ac9mUBb+HhgnNX489nejtrZJC+rOOiJ8iQdD3i1xg3Qxx3eeb+SGWxjQDqnkwXwoNR0jHhC1V4CYfaXemBRJuovYtBvpRay8YRyoC7Ki7XL6MibxBuL6sUpOG+fMNn5lizlp04nAbus5svlyEOHo+YtReaYZhQKBgQCmQGLONzaUZffyZYmnEDsEOPpMnJe8MkvUIbj4ogfbvEs8fEx4SXhgMYIFQROy8HSmSlkxZMwTEFeu5cpyeS81LLlnbBiJ6BAytKSqgNQKTRzGSeLGNCo2+ocPPArswwPIrmpF8vSFxwfgK5Oo3TavXoKQmB3azT8ZfQ4EOvos8wKBgEiRlfzZ7kIEX6c6B2ZMCx9qmvV/BMJVyLDQRL86bBmqnx5eTFmpalo/N3heTrqlAYmz1mTqbafbfa+8AD33HhauFnd2lsZ2fb0P4pPMdfejtl13msv7+dQ8XuINyX7XOugZaKY6pWlUhM19QIXzZL0Q2WDAonAjO46+YeciG91RAoGATTgKrI2opDTYfZX74cUAJG3ylDMHpzoVTmp7Z8Catc03hNiXDCfT6ZoVMiuP4sUc6UAPPYn7377veyTwa0suShyD4S2sGCHZpDOTaD5+PmqiutcGpdkE4pRqtwDEckQCNewNIIBAYwKW/eA/rWdSRxI2wsJk24QJkuUq5XwfR1ECgYEAqsXAth0EGX/MWQjmYHvXRWV0ENa/jFPfKqLWWRyjl0h/Vim0wgaUwAcgiHtHCYn2HViDSryJGvPJ495vxcOKwROkMH7+t3cftiQfXCbrYwy8VVvkyvbV6lBhc+iY4HPqtbv4WdajR7BVT9XJ1b4WsEhrjsQhUJgPK513S0BeDCU=";
    public static final String format = "json";
    public static final String charset = "utf-8";
    public static final String aliPayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7RBAS5HXPYj4vhRhpPCcRtRGCHU1YJD9gFVw6Rw+eEVelHRFrQHWCC/Bem48IOKr7S8jzDij40XWFzPyCTrtJgf8jyavERxq6s+D3pIVkXLXihooRCNmOIeV1EYfT281vtyYzChBIHxvojpRmgr9xWUxjGIm+TnHCCwXGH6PCEamrT507snuQm4D+UoOZVl+OnURtBTeYI46+m7rmHYgDtjmAqvPm1AvZcaAvprdfB7ZS14xfztKH2kPJzy9N4NPxgOV2n9rUDoePdFscMBp3pTdsnvMsnQo+Fy0Zrdzuc38DSUvUZ9h+79313ubREOS4s4OzPgFnWNXqysiBXSCsQIDAQAB";
    public static final String signType = "RSA2";

    // 「沙箱环境」服务器异步通知回调地址
    public static String notify_url = "https://xfg.natapp.cn/api/v1/alipay/alipay_notify_url";
    // 「沙箱环境」页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "https://inshion.site";

    private AlipayClient alipayClient;

    @Before
    public void init() {
        this.alipayClient = new DefaultAlipayClient(gatewayUrl, appId, merchantPrivateKey, format, charset, aliPayPublicKey, signType);
    }

    @Test
    public void testAlipay() throws JSONException, AlipayApiException {
        AlipayTradePagePayRequest req = new AlipayTradePagePayRequest();
        req.setNotifyUrl(notify_url);
        req.setReturnUrl(return_url);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", "inshion82AAA34556273"); // 订单号
        bizContent.put("total_amount", "0.01"); // 订单总金额
        bizContent.put("subject", "测试商品");  // 支付的名称
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");   // 固定配置
        req.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(req).getBody();
        log.info("测试结果 {}", form);
    }

}
