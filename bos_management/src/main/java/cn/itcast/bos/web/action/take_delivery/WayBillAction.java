package cn.itcast.bos.web.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description:运单的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class WayBillAction extends BaseAction<WayBill> {
    // 以日志形式打印数据
    private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);

    // 注入Service对象
    @Autowired
    private WayBillService wayBillService;

    // 快速运单录入保存方法
    @Action(value = "waybill_quickSave", results = { @Result(name = "success", type = "json") })
    public String quickSave() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用业务层保存数据
            wayBillService.quickSave(model);
            // 保存订单成功
            result.put("success", true);
            result.put("msg", "保存运单成功");
            LOGGER.info("保存运单成功，运单号为：" + model.getWayBillNum());
        } catch (Exception e) {
            e.printStackTrace();
            // 保存订单失败
            result.put("success", false);
            result.put("msg", "保存运单失败");
            LOGGER.info("保存运单失败，运单号为：" + model.getWayBillNum());
        }
        // 将数据压入值栈顶部返回
        ServletActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    // 运单录入分页查询方法
    @Action(value = "waybill_pageQuery", results = { @Result(name = "success", type = "json") })
    public String pageQuery() {
        // 封装分页查询对象
        Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Sort.Order(Sort.Direction.ASC, "id")));
        // 调用业务层查询数据
        Page<WayBill> pageData = wayBillService.findPageData(model,pageable);
        // 将对象压入值栈顶部返回
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }

    // 根据运单号查询运单方法
    @Action(value = "waybill_findByWayBillNum", results = {
            @Result(name = "success", type = "json", params = { "excludeNullProperties", "true" }) })
    public String findByWayBillNum() {
        // 调用业务层查询数据
        WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
        Map<String, Object> result = new HashMap<>();
        if (wayBill == null) {
            // 运单不存在
            result.put("success", false);
            result.put("msg", "运单号不存在，此运单号可以使用");
        } else {
            // 运单存在,判断运单是否已经绑定订单
            if (wayBill.getOrder() != null && wayBill.getOrder().getOrderNum() != null) {
                // 运单已经绑定订单
                result.put("success", false);
                result.put("msg", "运单号已经绑定订单，请核对运单号");
            } else {
                // 运单未绑定订单
                result.put("success", true);
                result.put("wayBillData", wayBill);
            }
        }
        // 压入值栈返回数据
        ServletActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    // 运单录入保存方法
    @Action(value = "waybill_save", results = {
            @Result(name = "success", type = "redirect", location = "./pages/take_delivery/waybill.html") })
    public String save() {
        // 调用业务层保存数据
        wayBillService.save(model);
        return SUCCESS;
    }
}
