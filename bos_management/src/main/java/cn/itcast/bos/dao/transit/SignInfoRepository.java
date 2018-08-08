package cn.itcast.bos.dao.transit;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.transit.SignInfo;
/**
 * @description: 签收信息的Dao接口
 */
public interface SignInfoRepository extends JpaRepository<SignInfo, Integer>{

}
