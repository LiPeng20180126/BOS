package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

/**
 * @description:快递员的Dao接口 有条件查询时需再继承JpaSpecificationExecutor接口
 */
public interface CourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {

	// 作废快递员信息方法（使用自定义命名规则）
	@Query("update Courier set deltag='1' where id=?")
	@Modifying
	public void updateDeltag(Integer id);

	// 还原快递员信息方法（使用自定义命名规则）
	@Query("update Courier set deltag=null where id=?")
	@Modifying
	public void restoreDeltag(Integer id);

}
