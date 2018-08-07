package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;

/**
 * @description:权限的Service接口
 */
public interface PermissionService {

    // 查询登录用户的权限
    public List<Permission> findByUser(User user);

    // 查询所有权限的方法
    public List<Permission> findAll();

    // 添加权限的方法
    public void save(Permission permission);

}
