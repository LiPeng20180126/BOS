package cn.itcast.fore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.contant.Contants;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.crm.domain.Customer;
import cn.itcast.fore.web.action.common.BaseAction;

/**
 * @description:订单的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderAction extends BaseAction<Order> {

    // 属性驱动接收省市区参数
    private String sendAreaInfo; // 寄件人省市区
    private String recAreaInfo; // 收件人省市区

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }

    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }

    // 保存订单方法
    @Action(value = "order_save", results = { @Result(name = "success", type = "redirect", location = "./index.html"),
            @Result(name = "login", type = "redirect", location = "./login.html") })
    public String save() {
        // 关联当前客户id
        Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
        if (customer == null) {
            // 客户未登录
            return LOGIN;
        }
        // 客户登录保存客户id
        model.setCustomer_id(customer.getId());
        
        // 封装寄件人的Area对象
        Area sendArea = new Area();
        String[] sendAreaData = sendAreaInfo.split("/");
        sendArea.setProvince(sendAreaData[0]);
        sendArea.setCity(sendAreaData[1]);
        sendArea.setDistrict(sendAreaData[2]);
        model.setSendArea(sendArea);
        
        // 封装收件人的Area对象
        Area recArea = new Area();
        String[] recAreaData = recAreaInfo.split("/");
        recArea.setProvince(recAreaData[0]);
        recArea.setCity(recAreaData[1]);
        recArea.setDistrict(recAreaData[2]);
        model.setRecArea(recArea);

        // 基于webService,调用bos_management系统保存订单信息
        WebClient.create(Contants.BOS_MANAGEMENT_URL + "/bos_management/services/orderService/order/save")
                .type(MediaType.APPLICATION_JSON).post(model);

        return SUCCESS;
    }

}
