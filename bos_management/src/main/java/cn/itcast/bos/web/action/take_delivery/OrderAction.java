package cn.itcast.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description:订单的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderAction extends BaseAction<Order> {
    // 注入Service对象
    @Autowired
    private OrderService orderService;

    // 根据订单号查询订单方法
    @Action(value = "order_findByOrderNum", results = { @Result(name = "success", type = "json") })
    public String findByOrderNum() {
        // 调用业务层查询数据
        Order order = orderService.findByOrderNum(model.getOrderNum());
        Map<String, Object> result = new HashMap<>();
        if (order == null) {
            // 订单不存在
            result.put("success", false);
            result.put("msg", "该订单不存在，请核对订单号");
        } else {
            // 订单已经存在，判断订单是否已经生成运单
            if (order.getWayBill() != null && order.getWayBill().getWayBillNum() != null) {
                // 已生成运单
                result.put("success", false);
                result.put("msg", "该订单已经生成了运单");
            } else {
                // 未生成运单
                result.put("success", true);
                result.put("orderData", order);
            }
        }
        // 压入值栈返回
        ServletActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
}
