package cn.itcast.bos.web.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.service.transit.SignInfoService;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description: 签收信息的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class SignInfoAction extends BaseAction<SignInfo> {
    @Autowired
    private SignInfoService signInfoService;

    // 属性驱动接收transitInfoId参数
    private String transitInfoId;

    public void setTransitInfoId(String transitInfoId) {
        this.transitInfoId = transitInfoId;
    }

    // 添加签收信息方法
    @Action(value = "sign_save", results = {
            @Result(name = "success", type = "redirect", location = "./pages/transit/transitinfo.html") })
    public String save() {
        // 调用业务层保存数据
        signInfoService.save(model, transitInfoId);
        return SUCCESS;
    }
}
