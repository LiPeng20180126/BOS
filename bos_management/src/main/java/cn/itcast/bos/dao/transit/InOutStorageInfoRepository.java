package cn.itcast.bos.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.transit.InOutStorageInfo;
/**
 * @description: 出入库信息的Dao接口
 */
public interface InOutStorageInfoRepository extends JpaRepository<InOutStorageInfo, Integer> {

}
