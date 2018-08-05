package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ExceptionMappings;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.xmlbeans.impl.common.IdentityConstraint.IdState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CourierService;

/**
 * @description:快递员的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
@SuppressWarnings("all")
@ExceptionMappings({    
    @ExceptionMapping(exception = "org.apache.shiro.authz.UnauthorizedException", 
            result = "unauthorized")    
}) 
@Results( { @Result(name = "unauthorized", location = "/unauthorized.html") }) 
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
	// 模型驱动封装页面传递数据
	private Courier courier = new Courier();

	@Override
	public Courier getModel() {
		return courier;
	}

	// 注入Service对象
	@Autowired
	private CourierService courierService;

	// 添加快递员信息方法
	@Action(value = "courier_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/courier.html") })
	public String save() {
		courierService.save(courier);
		return SUCCESS;
	}

	// 属性驱动接收分页参数
	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	// 分页查询快递员信息方法
	@Action(value = "courier_pageSearch", results = { @Result(name = "success", type = "json") })
	public String pageSearch() {
		// 封装Pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);

		// sql: where name like "%张%" and id=1
		// 对象属性 关系 属性值
		// 封装条件查询Specification对象
		Specification<Courier> specification = new Specification<Courier>() {

			@Override
			// Root 用于获取属性字段，CriteriaQuery可以用于简单条件查询，CriteriaBuilder 用于构造复杂条件查询
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 定义存放Predicate对象的list集合
				List<Predicate> list = new ArrayList<Predicate>();

				// 根据工号进行单表查询
				if (StringUtils.isNotBlank(courier.getCourierNum())) {
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					list.add(p1);
				}
				// 根据所属单位进行单表查询
				if (StringUtils.isNoneBlank(courier.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), "%" + courier.getCompany() + "%");
					list.add(p2);
				}
				// 根据类型进行单表查询
				if (StringUtils.isNoneBlank(courier.getType())) {
					Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
					list.add(p3);
				}
				// 根据收派标准进行多表查询
				Join<Courier, Standard> standardRoot = root.join("standard", JoinType.INNER);
				if (courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())) {
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class),
							"%" + courier.getStandard().getName() + "%");
					list.add(p4);
				}

				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		// 调用业务层查询数据,返回Page对象
		Page<Courier> pageData = courierService.findPageData(specification, pageable);

		// 将返回page对象转换datagrid需要的total和rows
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		// 将map数据转换成json压入值栈顶部中
		ActionContext.getContext().getValueStack().push(map);

		return SUCCESS;
	}

	// 属性驱动接收作废ids值
	private String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}

	// 批量作废快递员信息方法
	@Action(value = "courier_delBatch", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/courier.html") })
	public String delBatch() {
		String[] idArray = ids.split("-");
		courierService.delBatch(idArray);

		return SUCCESS;
	}

	// 批量还原快递员信息方法
	@Action(value = "courier_restoreBatch", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/courier.html") })
	public String restoreBatch() {
		String[] idArray = ids.split("-");
		courierService.restoreBatch(idArray);

		return SUCCESS;
	}

	// 查询所有未关联的快递员方法
	@Action(value = "courier_findNoassociation", results = { @Result(name = "success", type = "json") })
	public String findNoassociation() {
		// 调用业务层查询数据
		List<Courier> couriers = courierService.findNoassociation();
		// 将查询的快递员信息压入值栈顶部
		ActionContext.getContext().getValueStack().push(couriers);
		return SUCCESS;
	}
}
