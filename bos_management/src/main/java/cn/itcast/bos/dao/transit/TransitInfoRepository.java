package cn.itcast.bos.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.transit.TransitInfo;
/**
 * @description: 运输配送信息的Dao接口
 */
public interface TransitInfoRepository extends JpaRepository<TransitInfo, Integer> {

}
