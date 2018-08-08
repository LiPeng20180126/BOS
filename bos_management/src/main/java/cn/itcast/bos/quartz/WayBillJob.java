package cn.itcast.bos.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.itcast.bos.service.take_delivery.WayBillService;

@Component("wayBillJob")
public class WayBillJob implements Job {
    // 注入Service对象
    @Autowired
    private WayBillService wayBillService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("更新运单的索引库...");
        wayBillService.syncIndex();
    }

}
