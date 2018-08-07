package cn.itcast.bos.service.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;

/**
 * @description:菜单的Service接口
 */
public interface MenuService {

    // 查询菜单所有的方法
    public List<Menu> findAll();

    // 添加菜单的方法
    public void save(Menu menu);

    // 显示当前用户菜单的方法
    public List<Menu> findByUser(User user);

}
