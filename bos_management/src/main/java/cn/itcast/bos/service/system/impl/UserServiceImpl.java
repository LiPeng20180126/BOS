package cn.itcast.bos.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.UserRepository;
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

    // 根据用户名查询用户信息
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
