package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

/**
 * @description:收派标准的Service实现
 */
@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	// 注入收派标准的Repository对象
	@Autowired
	private StandardRepository standardRepository;

	// 添加收派标准信息方法
	@Override
	public void save(Standard standard) {
		standardRepository.save(standard);
	}

	// 分页查询收派标准信息方法
	@Override
	public Page<Standard> findPageData(Pageable pageable) {
		return standardRepository.findAll(pageable);
	}

	// 查询所有收派标准信息方法
	@Override
	public List<Standard> findAll() {
		return standardRepository.findAll();
	}

}
