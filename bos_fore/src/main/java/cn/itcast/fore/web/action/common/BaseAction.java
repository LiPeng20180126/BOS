package cn.itcast.fore.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


/**
 * @description:抽取 Action的公共代码 ，简化开发 【抽取模型驱动和分页查询部分】
 */
@SuppressWarnings("all")
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	// 模型驱动封装页面传递数据
	protected T model;

	@Override
	public T getModel() {
		return model;
	}

	// 构造器完成model实例化
	public BaseAction() {
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		// 获取类型第一个泛型参数
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// 属性驱动接收分页参数
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	// 将分页查询数据结果压入值栈中
	protected void pushPageDataToValueStack(Page<T> pageData) {
		// 将Page对象数据转成datagrid需要的total和rows
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		// 转成json压入值栈顶部
		ActionContext.getContext().getValueStack().push(result);
	}
}
