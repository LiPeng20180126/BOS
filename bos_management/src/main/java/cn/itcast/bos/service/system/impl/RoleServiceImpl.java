package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
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

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PermissionRepository permissionRepository;

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

    // 查询所有角色的方法
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    // 添加角色的方法
    @Override
    public void save(Role role, String[] permissionIds, String menuIds) {
        // 关联权限
        if (permissionIds != null) {
            for (String permissionId : permissionIds) {
                Permission permission = permissionRepository.findOne(Integer.parseInt(permissionId));
                role.getPermissions().add(permission);
            }
        }

        // 关联菜单
        if (StringUtils.isNoneBlank(menuIds)) {
            String[] menuIdStr = menuIds.split("-");
            for (String menuId : menuIdStr) {
                Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
                role.getMenus().add(menu);
            }
        }

        // 保存角色
        roleRepository.save(role);
    }

}
