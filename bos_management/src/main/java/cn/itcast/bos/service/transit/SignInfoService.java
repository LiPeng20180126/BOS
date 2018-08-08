package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.SignInfo;

/**
 * @description: 签收信息的Service接口
 */
public interface SignInfoService {
    // 添加签收信息方法
    public void save(SignInfo signInfo, String transitInfoId);

}
