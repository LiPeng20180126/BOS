package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

/**
 * @description:客户的Dao接口
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    // 查询所有未关联客户方法
    public List<Customer> findByFixedAreaIdIsNull();

    // 查询已经关联到指定定区的客户方法
    public List<Customer> findByFixedAreaId(String fixedAreaId);

    // 将客户关联到定区方法
    @Query("update Customer set fixedAreaId = ? where id = ?")
    @Modifying
    public void updateFixedAreaId(String fixedAreaId, Integer id);

    // 解除关联动作
    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
    @Modifying
    public void clearFixedAreaId(String fixedAreaId);

    // 通过号码查询客户信息
    public Customer findByTelephone(String telephone);

    // 通过号码修改客户绑定邮箱类型
    @Query("update Customer set type=1 where telephone=?")
    @Modifying
    public void updateType(String telephone);

    // 客户登录的方法
    public Customer findByEmailAndPassword(String email, String password);

    // 根据客户地区查询定区编码方法
    @Query("select fixedAreaId from Customer where address=?")
    public String findFixedAreaIdByAddress(String address);
}
