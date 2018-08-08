package cn.itcast.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;

/**
 * @description:运单的Service接口
 */
public interface WayBillService {
    // 快速运单录入保存方法
    public void quickSave(WayBill wayBill);

    // 快速运单录入分页查询方法
    public Page<WayBill> findPageData(WayBill wayBill, Pageable pageable);

    // 根据运单号查询运单方法
    public WayBill findByWayBillNum(String wayBillNum);

    // 运单录入保存方法
    public void save(WayBill wayBill);
    
    // 更新索引库方法
    public void syncIndex();

}
