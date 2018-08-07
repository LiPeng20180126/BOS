package cn.itcast.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.system.Menu;

/**
 * @description:菜单的Dao接口
 */
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    // 显示当前用户菜单的方法
    @Query("select distinct m from Menu m inner join fetch m.roles r inner join fetch r.users u where u.id=? order by m.priority")
    public List<Menu> findByUser(Integer id);

}
