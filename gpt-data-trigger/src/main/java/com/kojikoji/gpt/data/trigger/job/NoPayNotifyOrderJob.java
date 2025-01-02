package com.kojikoji.gpt.data.trigger.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName NoPayNotifyOrderJob
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2025/1/2 21:00
 * @Version
 */
@Component
public class NoPayNotifyOrderJob {

    @XxlJob("demoJobHandler")
    public void demoJobHandler() throws Exception {
        XxlJobHelper.log("XXL-JOB, Hello World!");

        for (int i = 0; i < 5; ++i) {
            XxlJobHelper.log("beat at " + i);
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
