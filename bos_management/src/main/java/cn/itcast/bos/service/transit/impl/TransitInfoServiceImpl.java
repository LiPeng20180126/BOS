package cn.itcast.bos.service.transit.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.transit.TransitInfoService;

/**
 * @description: 运输配送信息的Service实现
 */
@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService {
    // 注入Dao对象
    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Autowired
    private WayBillRepository wayBillRepository;

    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    // 开始中转配送的方法
    @Override
    public void createTransits(String wayBillIds) {
        if (StringUtils.isNoneBlank(wayBillIds)) {
            for (String wayBillId : wayBillIds.split("-")) {
                WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillId));
                // 判断运单状态是否为待发货
                if (wayBill.getSignStatus() == 1) {
                    // 待发货
                    // 生成TransitInfo信息
                    TransitInfo transitInfo = new TransitInfo();
                    transitInfo.setWayBill(wayBill); // 关联运单
                    transitInfo.setStatus("出入库中转");
                    transitInfoRepository.save(transitInfo);

                    // 更改运单状态
                    wayBill.setSignStatus(2); // 配送中
                    // 更改索引库运单状态
                    wayBillIndexRepository.save(wayBill);
                }
            }
        }
    }

    // 分页查询运输配送信息
    @Override
    public Page<TransitInfo> findByPageData(Pageable pageable) {
        return transitInfoRepository.findAll(pageable);
    }

}
