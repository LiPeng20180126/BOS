package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description:后台用户的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class UserAction extends BaseAction<User> {
    // 注入Service对象
    @Autowired
    private UserService userService;

    // 后台用户登录的方法
    @Action(value = "user_login", results = { @Result(name = "success", type = "redirect", location = "index.html"),
            @Result(name = "login", type = "redirect", location = "login.html") })
    public String login() {
        // 基于shiro实现登录
        Subject subject = SecurityUtils.getSubject();
        // 用户名和密码信息
        AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
        try {
            subject.login(token);
            // 登录成功
            return SUCCESS;
        } catch (UnknownAccountException e) {
            // 登录失败，用户名不存在
            e.printStackTrace();
            this.addActionError("用户名不存在！");
            return LOGIN;
        } catch (IncorrectCredentialsException e) {
            // 登录失败，密码输入错误
            e.printStackTrace();
            this.addActionError("密码输入错误！");
            return LOGIN;
        }
    }

    // 后台用户注销的方法
    @Action(value = "user_logout", results = { @Result(name = "success", type = "redirect", location = "login.html"), })
    public String logout() {
        // 基于shiro实现注销
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return SUCCESS;
    }

    // 查询所有用户方法
    @Action(value = "user_list", results = { @Result(name = "success", type = "json") })
    public String list() {
        // 调用业务层查询数据
        List<User> users = userService.findAll();
        // 压入值栈返回数据
        ActionContext.getContext().getValueStack().push(users);
        return SUCCESS;
    }

    // 属性驱动接收角色参数
    private String[] roleIds;

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    // 保存用户的方法
    @Action(value = "user_save", results = {
            @Result(name = "success", type = "redirect", location = "./pages/system/userlist.html"), })
    public String save() {
        // 调用业务层保存数据
        userService.save(model,roleIds);
        return SUCCESS;
    }

}
