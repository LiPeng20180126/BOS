package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description:菜单的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class MenuAction extends BaseAction<Menu> {
    // 注入Service对象
    @Autowired
    private MenuService menuService;

    // 查询菜单所有的方法
    @Action(value = "menu_list", results = { @Result(name = "success", type = "json") })
    public String list() {
        // 调用业务层查询数据
        List<Menu> menus = menuService.findAll();
        // 压入值栈返回数据
        ActionContext.getContext().getValueStack().push(menus);
        return SUCCESS;
    }

    // 添加菜单的方法
    @Action(value = "menu_save", results = {
            @Result(name = "success", type = "redirect", location = "./pages/system/menu.html") })
    public String save() {
        // 调用业务层保存数据
        menuService.save(model);
        return SUCCESS;
    }
    
    // 显示当前用户菜单的方法
    @Action(value="menu_showMenu",results={@Result(name="success",type="json")})
    public String showMenu(){
        // 获取当前用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        // 调用业务层查询数据
        List<Menu> menus = menuService.findByUser(user);
        // 压入值栈返回数据
        ActionContext.getContext().getValueStack().push(menus);
        return SUCCESS;
    }

}
