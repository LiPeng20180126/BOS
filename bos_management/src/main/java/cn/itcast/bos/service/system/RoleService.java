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

}
