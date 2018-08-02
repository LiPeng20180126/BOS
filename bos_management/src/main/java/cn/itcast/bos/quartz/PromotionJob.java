package cn.itcast.bos.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itcast.bos.service.take_delivery.PromotionService;

@Component("promotionJob")
public class PromotionJob implements Job {
    // 注入Service对象
    @Autowired
    private PromotionService promotionService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("活动过期程序处理执行...");
        // 每天执行一次，当前时间大于promotion数据表中endDate,活动过期时修改status值为2
        promotionService.updateStatus(new Date());
    }

}
