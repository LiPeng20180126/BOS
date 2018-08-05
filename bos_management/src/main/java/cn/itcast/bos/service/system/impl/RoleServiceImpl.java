package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.RoleService;

/**
 * @description:角色的Service实现
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    // 注入Dao对象
    @Autowired
    private RoleRepository roleRepository;

    // 查询登录用户的角色
    @Override
    public List<Role> findByUser(User user) {
        // admin具有所有角色
        if (user.getUsername().equals("admin")) {
            return roleRepository.findAll();
        } else {
            return roleRepository.findByUser(user.getId());
        }
    }

}
