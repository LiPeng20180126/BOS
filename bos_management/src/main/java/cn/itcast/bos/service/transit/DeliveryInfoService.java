package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.DeliveryInfo;

/**
 * @description: 配送信息的Service接口
 */
public interface DeliveryInfoService {

    // 添加配送信息方法
    public void save(DeliveryInfo deliveryInfo, String transitInfoId);

}
