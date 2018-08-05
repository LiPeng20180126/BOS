package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.User;

/**
 * @description:后台用户的Service接口
 */
public interface UserService {
    
    // 根据用户名查询用户信息
    public User findByUsername(String username);

}
