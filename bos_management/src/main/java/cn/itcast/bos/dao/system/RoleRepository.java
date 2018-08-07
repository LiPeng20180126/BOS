package cn.itcast.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.system.Role;

/**
 * @description:角色的Dao接口
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

    // 查询登录用户的角色
    @Query("from Role r inner join fetch r.users u where u.id=?")
    public List<Role> findByUser(Integer id);

}
