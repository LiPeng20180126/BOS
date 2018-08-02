package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;

/**
 * @description:区域的Service实现
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {

	// 注入Dao对象
	@Autowired
	private AreaRepository areaRepository;

	// 批量数据的区域导入方法
	@Override
	public void save(List<Area> areas) {
		areaRepository.save(areas);
	}

	// 带条件分页查询区域的方法
	@Override
	public Page<Area> findPageData(Specification<Area> specification, Pageable pageable) {
		return areaRepository.findAll(specification, pageable);
	}

	// 添加区域方法
	@Override
	public void add(Area area) {
		areaRepository.save(area);
	}

	// 查询所有区域的方法
	@Override
	public List<Area> findAll() {
		return areaRepository.findAll();
	}

	// 通过省市区查询区域的方法
    @Override
    public Area findByProvinceAndCityAndDistrict(String province, String city, String district) {
        return areaRepository.findByProvinceAndCityAndDistrict(province,city,district);
    }

}
