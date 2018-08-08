package cn.itcast.bos.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.transit.DeliveryInfo;
/**
 * @description: 配送信息的Dao接口
 */
public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Integer>{

}
