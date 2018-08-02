package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.SubArea;
/**
 * @description:分区的Dao接口
 */
public interface SubAreaRepository extends JpaRepository<SubArea, Integer>,JpaSpecificationExecutor<SubArea> {

}
