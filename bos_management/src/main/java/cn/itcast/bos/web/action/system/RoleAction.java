package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description:角色的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class RoleAction extends BaseAction<Role> {
    // 注入Service对象
    @Autowired
    private RoleService roleService;

    // 查询所有角色的方法
    @Action(value = "role_list", results = { @Result(name = "success", type = "json") })
    public String list() {
        // 调用业务层查询数据
        List<Role> roles = roleService.findAll();
        // 压入值栈返回数据
        ActionContext.getContext().getValueStack().push(roles);
        return SUCCESS;
    }

    // 属性驱动接收权限和菜单的参数
    private String[] permissionIds;
    private String menuIds;

    public void setPermissionIds(String[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    // 添加角色的方法
    @Action(value = "role_save", results = {
            @Result(name = "success", type = "redirect", location = "./pages/system/role.html") })
    public String save() {
        // 调用业务层保存数据
        roleService.save(model,permissionIds,menuIds);
        return SUCCESS;
    }
}
