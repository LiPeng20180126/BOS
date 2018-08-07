package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.dao.system.UserRepository;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;

/**
 * @description:后台用户的Service实现
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    // 注入Dao对象
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // 根据用户名查询用户信息
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 查询所有用户方法
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // 保存用户的方法
    @Override
    public void save(User user, String[] roleIds) {
        // 关联角色
        if (StringUtils.isNoneBlank(roleIds)) {
            for (String roleId : roleIds) {
                Role role = roleRepository.findOne(Integer.parseInt(roleId));
                user.getRoles().add(role);
            }
        }

        // 保存用户
        userRepository.save(user);
    }

}
