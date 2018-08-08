package cn.itcast.bos.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.transit.SignInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.transit.SignInfoService;

/**
 * @description: 签收信息的Service实现
 */
@Service
@Transactional
public class SignInfoServiceImpl implements SignInfoService {
    @Autowired
    private SignInfoRepository signInfoRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    // 添加签收信息方法
    @Override
    public void save(SignInfo signInfo, String transitInfoId) {
        // 保存签收信息
        signInfoRepository.save(signInfo);
        // 关联签收信息到运输配送对象中
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
        transitInfo.setSignInfo(signInfo);
        // 更改状态
        if (signInfo.getSignType().equals("正常")) {
            // 更改运输配送的状态
            transitInfo.setStatus("正常签收");
            // 更改运单状态
            transitInfo.getWayBill().setSignStatus(3);
            // 更改索引库信息
            wayBillIndexRepository.save(transitInfo.getWayBill());
        } else {
            // 更改运输配送的状态
            transitInfo.setStatus("异常");
            // 更改运单状态
            transitInfo.getWayBill().setSignStatus(4);
            // 更改索引库信息
            wayBillIndexRepository.save(transitInfo.getWayBill());
        }

    }

}
