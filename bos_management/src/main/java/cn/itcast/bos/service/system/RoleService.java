package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;

/**
 * @description:角色的Service接口
 */
public interface RoleService {

    // 查询登录用户的角色
    public List<Role> findByUser(User user);

    // 查询所有角色的方法
    public List<Role> findAll();

    // 添加角色的方法
    public void save(Role role, String[] permissionIds, String menuIds);

}
