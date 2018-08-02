package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;

/**
 * @description:定区的Service实现
 */
@Transactional
@Service
public class FixedAreaServiceImpl implements FixedAreaService {
	// 注入Dao对象
	@Autowired
	private FixedAreaRepository fixedAreaRepository;

	@Autowired
	private CourierRepository courierRepository;

	@Autowired
	private TakeTimeRepository takeTimeRepository;

	// 添加定区的方法
	@Override
	public void save(FixedArea fixedArea) {
		fixedAreaRepository.save(fixedArea);
	}

	// 条件分页查询定区方法
	@Override
	public Page<FixedArea> findPageData(Pageable pageable, Specification<FixedArea> specification) {
		return fixedAreaRepository.findAll(specification, pageable);
	}

	// 指定定区关联快递员的方法
	@Override
	public void associationCourierToFixedArea(FixedArea fixedArea, Integer courierId, Integer takeTimeId) {
		// 根据id查询对象
		FixedArea persistFixedArea = fixedAreaRepository.findOne(fixedArea.getId());
		Courier courier = courierRepository.findOne(courierId);
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		
		// 添加关联(谁有维护谁搞关系，外键一定有值)
		// 将快递员关联到定区上
		persistFixedArea.getCouriers().add(courier);
		// 将收派时间关联到快递员上
		courier.setTakeTime(takeTime);
	}

}
