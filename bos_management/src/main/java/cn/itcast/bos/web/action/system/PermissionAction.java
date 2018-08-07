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

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description:权限的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PermissionAction extends BaseAction<Permission> {
    // 注入Service对象
    @Autowired
    private PermissionService permissionService;

    // 查询所有权限的方法
    @Action(value = "permission_list", results = { @Result(name = "success", type = "json") })
    public String list() {
        // 调用业务层查询数据
        List<Permission> permissions = permissionService.findAll();
        // 压入值栈返回数据
        ActionContext.getContext().getValueStack().push(permissions);
        return SUCCESS;
    }

    // 添加权限的方法
    @Action(value = "permission_save", results = {
            @Result(name = "success", type = "redirect", location = "./pages/system/permission.html") })
    public String save() {
        // 调用业务层保存数据
        permissionService.save(model);
        return SUCCESS;
    }
}
