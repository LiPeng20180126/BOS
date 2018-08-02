package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

/**
 * @description:快递员的Service接口
 */
public interface CourierService {

	// 添加快递员信息方法
	public void save(Courier courier);

	// 分页查询快递员信息方法
	public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable);

	// 批量作废快递员信息方法
	public void delBatch(String[] idArray);

	// 批量还原快递员信息方法
	public void restoreBatch(String[] idArray);

	// 查询所有未关联的快递员方法
	public List<Courier> findNoassociation();

}
