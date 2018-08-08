package cn.itcast.bos.service.transit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.transit.InOutStorageInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.InOutStorageInfoService;

/**
 * @description: 出入库信息的Service实现
 */
@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService {
    // 注入Dao对象
    @Autowired
    private InOutStorageInfoRepository inOutStorageInfoRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;

    // 添加出入库信息方法
    @Override
    public void save(InOutStorageInfo inOutStorageInfo, String transitInfoId) {
        // 保存出入库信息
        inOutStorageInfoRepository.save(inOutStorageInfo);
        // 关联出入库信息到运输配送对象
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
        transitInfo.getInOutStorageInfos().add(inOutStorageInfo);
        // 修改状态
        if(inOutStorageInfo.getOperation().equals("到达网点")){
            transitInfo.setStatus("到达网点");
            // 更新网点地址，显示配送路径
            transitInfo.setOutletAddress(inOutStorageInfo.getAddress());
        }
    }

}
