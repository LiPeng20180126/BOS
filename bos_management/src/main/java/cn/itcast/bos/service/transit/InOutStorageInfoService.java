package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.InOutStorageInfo;

/**
 * @description: 出入库信息的Service接口
 */
public interface InOutStorageInfoService {
    // 添加出入库信息方法
    public void save(InOutStorageInfo inOutStorageInfo, String transitInfoId);

}
