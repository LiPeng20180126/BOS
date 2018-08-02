package cn.itcast.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.take_delivery.Order;

/**
 * @description:订单的Dao接口
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // 根据订单号查询订单方法
    public Order findByOrderNum(String orderNum);

}
