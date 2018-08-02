package cn.itcast.fore.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.contant.Contants;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.fore.web.action.common.BaseAction;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @description:促销信息的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
@SuppressWarnings("all")
public class PromotionAction extends BaseAction<Promotion> {

    // 分页查询促销信息的方法
    @Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
    public String pageQuery() {
        // 基于webService获取bos_managent中活动列表信息
        PageBean<Promotion> pageBean =
                WebClient
                        .create(Contants.BOS_MANAGEMENT_URL
                                + "/bos_management/services/promotionService/promotion?page=" + page + "&rows=" + rows)
                        .accept(MediaType.APPLICATION_JSON).get(PageBean.class);
        // 压入值栈返回数据
        ActionContext.getContext().getValueStack().push(pageBean);
        return SUCCESS;
    }

    // 显示促销信息的内容方法
    @Action(value = "promotion_detail")
    public String detail() throws Exception {
        // 判断id对应的HTML是否存在，如果存在直接返回
        String htmlRealPath = ServletActionContext.getServletContext().getRealPath("/freemarker");
        File htmlFile = new File(htmlRealPath + "/" + model.getId() + ".html");
        if (!htmlFile.exists()) {
            // 不存在查询数据库，使用freemarker创建html页面
            // 配置对象， 指定freemarker的版本，配置模板目录位置
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
            configuration.setDirectoryForTemplateLoading(
                    new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/freemarker_templates")));
            // 获取模板对象
            Template template = configuration.getTemplate("promotion_detail.ftl");
            // 获取动态数据
            Promotion promotion = WebClient.create(Contants.BOS_MANAGEMENT_URL
                    + "/bos_management/services/promotionService/promotion/" + model.getId())
                    .accept(MediaType.APPLICATION_JSON).get(Promotion.class);
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("promotion", promotion);

            // 合并输出
            template.process(parameterMap, new OutputStreamWriter(new FileOutputStream(htmlFile), "utf-8"));

        }
        // 存在直接将文件返回
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());

        return NONE;
    }

}
