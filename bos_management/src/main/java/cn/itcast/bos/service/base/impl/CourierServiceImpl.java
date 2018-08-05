package cn.itcast.bos.service.base.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

/**
 * @description:快递员的Service实现
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	// 注入Dao对象
	@Autowired
	private CourierRepository courierRepository;

	// 添加快递员信息方法
	@Override
	@RequiresPermissions("courier:add")
	public void save(Courier courier) {
		courierRepository.save(courier);
	}

	// 分页查询快递员信息方法
	@Override
	public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable) {
		return courierRepository.findAll(specification, pageable);
	}

	// 批量作废快递员信息方法
	@Override
	public void delBatch(String[] idArray) {
		for (String idStr : idArray) {
			Integer id = Integer.parseInt(idStr);
			courierRepository.updateDeltag(id);
		}
	}

	// 批量还原快递员信息方法
	@Override
	public void restoreBatch(String[] idArray) {
		for (String idStr : idArray) {
			Integer id = Integer.parseInt(idStr);
			courierRepository.restoreDeltag(id);
		}

	}

	// 查询所有未关联的快递员方法(关联定区集合为空以及未作废)
	@Override
	public List<Courier> findNoassociation() {
		// 封装specification
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 判断集合是否为空
				Predicate p1 = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				// 判断对象是否为空
				Predicate p2 = cb.isNull(root.get("deltag").as(Character.class));

				return cb.and(p1, p2);
			}
		};

		return courierRepository.findAll(specification);
	}

}
