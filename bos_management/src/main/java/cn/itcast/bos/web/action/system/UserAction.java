package cn.itcast.bos.web.action.system;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description:后台用户的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class UserAction extends BaseAction<User> {

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
}
