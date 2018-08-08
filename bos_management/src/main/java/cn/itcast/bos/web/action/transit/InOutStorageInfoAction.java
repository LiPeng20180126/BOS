package cn.itcast.bos.web.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.service.transit.InOutStorageInfoService;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description: 出入库信息的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class InOutStorageInfoAction extends BaseAction<InOutStorageInfo> {
    // 注入Service对象
    @Autowired
    private InOutStorageInfoService inOutStorageInfoService;

    // 属性驱动接收transitInfoId参数
    private String transitInfoId;

    public void setTransitInfoId(String transitInfoId) {
        this.transitInfoId = transitInfoId;
    }

    // 添加出入库信息方法
    @Action(value = "inoutStore_save", results = {
            @Result(name = "success", type = "redirect", location = "./pages/transit/transitinfo.html") })
    public String save() {
        // 调用业务层保存数据
        inOutStorageInfoService.save(model,transitInfoId);
        return SUCCESS;
    }
}
