package cn.itcast.bos.service.take_delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import cn.itcast.bos.domain.take_delivery.Order;

/**
 * @description:订单的Service接口
 */
public interface OrderService {

    // 保存订单方法
    @POST
    @Path("/order/save")
    @Consumes({ "application/xml", "application/json" })
    public void saveOrder(Order order);

    // 根据订单号查询订单方法
    public Order findByOrderNum(String orderNum);
}
