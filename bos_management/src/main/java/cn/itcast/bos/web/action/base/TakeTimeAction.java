package cn.itcast.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description:收派时间管理的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class TakeTimeAction extends BaseAction<TakeTime> {

	// 注入Service对象
	@Autowired
	private TakeTimeService takeTimeService;

	// 查询所有收派时间方法
	@Action(value = "taketime_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		// 调用业务层查询数据
		List<TakeTime> takeTimes = takeTimeService.findAll();
		// 将查询的收派时间数据压入值栈顶部
		ActionContext.getContext().getValueStack().push(takeTimes);
		
		return SUCCESS;
	}
}
