package cn.itcast.bos.web.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

/**
 * @description:收派标准的Action
 */

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
@SuppressWarnings("all")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {
	// 模型驱动封装页面传递数据
	private Standard standard = new Standard();

	@Override
	public Standard getModel() {
		return standard;
	}

	// 注入Service对象
	@Autowired
	private StandardService standardService;

	// 添加收派标准信息保存方法
	@Action(value = "standard_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/standard.html") })
	public String save() {
		standardService.save(standard);
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

	// 收派标准分页查询方法
	@Action(value = "standard_pageSearch", results = { @Result(name = "success", type = "json") })
	public String pageSearch() {
		// 封装Pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);
		// 调用业务层查询数据，返回Page对象
		Page<Standard> pageData = standardService.findPageData(pageable);

		// 将返回page对象转换datagrid需要的total和rows
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		// 将map转换为json数据压入值栈顶部
		ActionContext.getContext().getValueStack().push(map);

		return SUCCESS;
	}

	// 查询所有收派标准信息方法
	@Action(value = "standard_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		// 调用业务层查询数据
		List<Standard> list = standardService.findAll();
		// 将list转换为json数据压入值栈顶部
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
}
