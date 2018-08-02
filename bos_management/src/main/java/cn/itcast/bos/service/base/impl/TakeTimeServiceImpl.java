package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;

/**
 * @description:收派时间管理的Service实现
 */
@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {

	// 注入Dao对象
	@Autowired
	private TakeTimeRepository takeTimeRepository;

	// 查询所有收派时间方法
	@Override
	public List<TakeTime> findAll() {
		return takeTimeRepository.findAll();
	}

}
