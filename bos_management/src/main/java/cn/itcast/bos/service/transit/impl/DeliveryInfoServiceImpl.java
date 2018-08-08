package cn.itcast.bos.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.transit.DeliveryInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.DeliveryInfoService;

/**
 * @description: 配送信息的Service实现
 */
@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService {
    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;

    // 添加配送信息方法
    @Override
    public void save(DeliveryInfo deliveryInfo, String transitInfoId) {
        // 保存配送信息
        deliveryInfoRepository.save(deliveryInfo);
        // 关联配送信息到运输配送对象中
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
        transitInfo.setDeliveryInfo(deliveryInfo);
        // 更改状态
        transitInfo.setStatus("开始配送");
    }

}
