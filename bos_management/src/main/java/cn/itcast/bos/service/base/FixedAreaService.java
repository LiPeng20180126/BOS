package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.FixedArea;

/**
 * @description:定区的Service接口
 */
public interface FixedAreaService {

	//添加定区的方法
	public void save(FixedArea fixedArea);

	// 条件分页查询定区方法
	public Page<FixedArea> findPageData(Pageable pageable, Specification<FixedArea> specification);

	// 指定定区关联快递员的方法
	public void associationCourierToFixedArea(FixedArea fixedArea, Integer courierId, Integer takeTimeId);

}
