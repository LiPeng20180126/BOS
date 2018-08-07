package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.User;

/**
 * @description:后台用户的Service接口
 */
public interface UserService {

    // 根据用户名查询用户信息
    public User findByUsername(String username);

    // 查询所有用户方法
    public List<User> findAll();

    // 保存用户的方法
    public void save(User user, String[] roleIds);

}
