package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;

/**
 * @description:权限的Service实现
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    // 注入Dao对象
    @Autowired
    private PermissionRepository permissionRepository;

    // 查询登录用户的权限
    @Override
    public List<Permission> findByUser(User user) {
        // admin具有所有权限
        if (user.getUsername().equals("admin")) {
            return permissionRepository.findAll();
        } else {
            return permissionRepository.findByUser(user.getId());
        }
    }

    // 查询所有权限的方法
    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    // 添加权限的方法
    @Override
    public void save(Permission permission) {
        permissionRepository.save(permission);
    }

}
