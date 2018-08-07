package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;

/**
 * @description:菜单的Service实现
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    // 注入Dao接口
    @Autowired
    private MenuRepository menuRepository;

    // 查询菜单所有的方法
    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    // 添加菜单的方法
    @Override
    public void save(Menu menu) {
        // 防止用户没有选择父菜单项
        if (menu.getParentMenu() != null && menu.getParentMenu().getId() == null) {
            menu.setParentMenu(null);
        }
        menuRepository.save(menu);
    }

    // 显示当前用户菜单的方法
    @Override
    public List<Menu> findByUser(User user) {
        // 针对admin用户显示所有菜单
        if(user.getUsername().equals("admin")){
            return menuRepository.findAll();
        }else{
            return menuRepository.findByUser(user.getId());
        }
    }

}
