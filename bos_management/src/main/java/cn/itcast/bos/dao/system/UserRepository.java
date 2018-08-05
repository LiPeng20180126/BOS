package cn.itcast.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.system.User;

/**
 * @description:后台用户的Dao接口
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    // 根据用户名查询用户信息
    public User findByUsername(String username);

}
