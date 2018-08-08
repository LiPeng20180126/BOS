package cn.itcast.bos.web.action.transit;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.TransitInfoService;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description: 运输配送信息的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class TransitInfoAction extends BaseAction<TransitInfo> {
    // 注入Service对象
    @Autowired
    private TransitInfoService transitInfoService;

    // 属性驱动接收运单id参数
    private String wayBillIds;

    public void setWayBillIds(String wayBillIds) {
        this.wayBillIds = wayBillIds;
    }

    // 开始中转配送的方法
    @Action(value = "transit_create", results = { @Result(name = "success", type = "json") })
    public String create() {
        // 调用业务层保存数据
        Map<String, Object> result = new HashMap<>();
        try {
            transitInfoService.createTransits(wayBillIds);
            result.put("success", true);
            result.put("msg", "开始中转配送成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "开始中转配送失败");
        }
        // 压入值栈返回数据
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
    
    // 分页查询运输配送信息
    @Action(value="transit_pageQuery",results={@Result(name="success",type="json")})
    public String pageQuery(){
        // 封装分页查询对象
        Pageable pageable = new PageRequest(page-1, rows);
        // 调用业务层查询数据
        Page<TransitInfo> pageData = transitInfoService.findByPageData(pageable);
        // 压入值栈返回数据
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }
}
