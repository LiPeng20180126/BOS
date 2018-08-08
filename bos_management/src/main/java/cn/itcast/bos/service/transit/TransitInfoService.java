package cn.itcast.bos.service.transit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.transit.TransitInfo;

/**
 * @description: 运输配送信息的Service接口
 */
public interface TransitInfoService {

    // 开始中转配送的方法
    public void createTransits(String wayBillIds);

    // 分页查询运输配送信息
    public Page<TransitInfo> findByPageData(Pageable pageable);

}
