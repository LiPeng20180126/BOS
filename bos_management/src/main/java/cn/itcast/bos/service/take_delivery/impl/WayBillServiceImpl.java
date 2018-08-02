package cn.itcast.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;

/**
 * @description:运单的Service实现
 */
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
    // 注入Dao对象
    @Autowired
    private WayBillRepository wayBillRepository;

    // 快速运单录入保存方法
    @Override
    public void quickSave(WayBill wayBill) {
        wayBillRepository.save(wayBill);
    }

    // 快速运单录入分页查询方法
    @Override
    public Page<WayBill> findPageData(Pageable pageable) {
        return wayBillRepository.findAll(pageable);
    }

    // 根据运单号查询运单方法
    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }

    // 运单录入保存方法,注意运单号id
    @Override
    public void save(WayBill wayBill) {
        // 判断运单号是否存在
        Integer id = wayBillRepository.findIdByWayBillNum(wayBill.getWayBillNum());
        if (id != null) {
            wayBill.setId(id);
        }
        wayBillRepository.save(wayBill);
    }

}
