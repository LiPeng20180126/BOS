package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.SubArea;

/**
 * @description:分区的Service接口
 */
public interface SubAreaService {
	// 添加分区的方法
	public void save(SubArea subArea);

	// 分页条件查询方法
	public Page<SubArea> findPageData(Pageable pageable, Specification<SubArea> specification);

	// 批量导入分区的方法
	public void batchSave(List<SubArea> subAreas);

	// 查询所有分区的数据
	public List<SubArea> findAll();

}
