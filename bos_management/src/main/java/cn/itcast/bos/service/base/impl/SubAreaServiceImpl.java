package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.SubAreaRepository;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;

/**
 * @description:分区的Service实现
 */
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

	// 注入Dao对象
	@Autowired
	private SubAreaRepository subAreaRepository;

	// 添加分区的方法
	@Override
	public void save(SubArea subArea) {
		subAreaRepository.save(subArea);
	}

	// 分页条件查询方法
	@Override
	public Page<SubArea> findPageData(Pageable pageable, Specification<SubArea> specification) {
		return subAreaRepository.findAll(specification, pageable);
	}

	// 批量导入分区的方法
	@Override
	public void batchSave(List<SubArea> subAreas) {
		subAreaRepository.save(subAreas);
	}

	// 查询所有分区的数据
	@Override
	public List<SubArea> findAll() {
		return subAreaRepository.findAll();
	}

}
