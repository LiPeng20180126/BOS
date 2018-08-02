package cn.itcast.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.take_delivery.WayBill;

/**
 * @description:运单的Dao接口
 */
public interface WayBillRepository extends JpaRepository<WayBill, Integer> {

    // 根据运单号查询运单方法
    public WayBill findByWayBillNum(String wayBillNum);

    // 通过运单号查询id
    @Query("select id from WayBill where wayBillNum=?")
    public Integer findIdByWayBillNum(String wayBillNum);

}
