package cn.itcast.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.system.Permission;


/**
 * @description:权限的Dao接口
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    // 查询登录用户的权限
    @Query("from Permission p inner join fetch p.roles r inner join fetch r.users u where u.id=?")
    public List<Permission> findByUser(Integer id);

}
