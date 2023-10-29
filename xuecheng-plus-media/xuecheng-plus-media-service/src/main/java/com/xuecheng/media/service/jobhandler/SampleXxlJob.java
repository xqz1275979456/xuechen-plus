package com.xuecheng.media.service.jobhandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SampleXxlJob {
    private static Logger logger=LoggerFactory.getLogger(SampleXxlJob.class);

    /*
    * 1.简单任务示例（Bean模式）
    * */
    @XxlJob("demoJobHandler")
    public void demoJobHandler(){
        System.out.println("处理视频");
    }
    /*
    * 2.分片广播任务
    * */
    @XxlJob("shardingJobHandler")
    public void shardingJobHandler(){
        //分片参数
        int shardIndex = XxlJobHelper.getShardIndex();//执行器的序号从0开始
        int shardTotal = XxlJobHelper.getShardTotal();//执行器的总数
        System.out.println("shardIndex="+shardIndex+",shardTotal="+shardTotal);

    }
}
