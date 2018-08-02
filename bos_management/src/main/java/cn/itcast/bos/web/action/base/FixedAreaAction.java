package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.base.common.BaseAction;
import cn.itcast.crm.contant.Contants;
import cn.itcast.crm.domain.Customer;

/**
 * @description:定区的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class FixedAreaAction extends BaseAction<FixedArea> {

    // 注入Service对象
    @Autowired
    private FixedAreaService fixedAreaService;

    // 添加定区的方法
    @Action(value = "fixedArea_save", results = {
            @Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
    public String save() {
        fixedAreaService.save(model);
        return SUCCESS;
    }

    // 条件分页查询定区方法
    @Action(value = "fixedArea_pageQuery", results = { @Result(name = "success", type = "json") })
    public String pageQuery() {
        // 封装pageable对象
        Pageable pageable = new PageRequest(page - 1, rows);
        // 封装specification对象
        Specification<FixedArea> specification = new Specification<FixedArea>() {

            @Override
            public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                // 根据定区编码进行单表查询
                if (StringUtils.isNotBlank(model.getId())) {
                    Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
                    list.add(p1);
                }
                // 根据所属单位进行单表查询
                if (StringUtils.isNotBlank(model.getCompany())) {
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
                    list.add(p2);
                }

                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        // 调用业务层查询数据，返回page对象
        Page<FixedArea> pageData = fixedAreaService.findPageData(pageable, specification);

        // 将分页查询结果压入值栈顶部
        pushPageDataToValueStack(pageData);

        return SUCCESS;
    }

    // 查询未关联客户的方法
    @Action(value = "fixedArea_findNoAssociationCustomer", results = { @Result(name = "success", type = "json") })
    public String findNoAssociationCustomer() {
        // 使用WebClient调用webService接口
        Collection<? extends Customer> collection =
                WebClient
                        .create(Contants.CRM_MANAGEMENT_URL
                                + "/crm_management/services/customerService/noassociationcustomer")
                        .accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);

        // 将数据压入值栈顶部
        ActionContext.getContext().getValueStack().push(collection);

        return SUCCESS;
    }

    // 查询关联指定定区客户的方法
    @Action(value = "fixedArea_findAssociationFixedAreaCustomer", results = {
            @Result(name = "success", type = "json") })
    public String findAssociationFixedAreaCustomer() {
        // 使用WebClient调用webService接口
        Collection<? extends Customer> collection = WebClient
                .create(Contants.CRM_MANAGEMENT_URL + "/crm_management/services/customerService/associationcustomer/"
                        + model.getId())
                .accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).getCollection(Customer.class);

        // 将数据压入值栈顶部
        ActionContext.getContext().getValueStack().push(collection);

        return SUCCESS;
    }

    // 属性驱动接收关联客户参数
    private String[] customerIds;

    public void setCustomerIds(String[] customerIds) {
        this.customerIds = customerIds;
    }

    // 关联客户到指定定区的方法
    @Action(value = "fixedArea_associationCustomerToFixedArea", results = {
            @Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
    public String associationCustomerToFixedArea() {
        // 将关联的客户id拼接成指定格式的字符串
        String customerIdStr = StringUtils.join(customerIds, "-");
        // 使用WebClient调用webService接口
        WebClient.create(Contants.CRM_MANAGEMENT_URL
                + "/crm_management/services/customerService/customerassociationtofixedarea?fixedAreaId=" + model.getId()
                + "&customerIdStr=" + customerIdStr).put(null);

        return SUCCESS;
    }

    // 属性驱动接收指定定区关联快递员参数
    private Integer courierId;
    private Integer takeTimeId;

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    // 指定定区关联快递员的方法
    @Action(value = "fixedArea_associationCourierToFixedArea", results = {
            @Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
    public String associationCourierToFixedArea() {
        // 调用业务层,定区关联快递员
        fixedAreaService.associationCourierToFixedArea(model, courierId, takeTimeId);
        return SUCCESS;
    }
}
