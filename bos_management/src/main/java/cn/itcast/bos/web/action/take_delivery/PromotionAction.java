package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
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
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description:促销信息的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion> {
	// 注入Service对象
	@Autowired
	private PromotionService promotionService;

	// 属性驱动接收上传文件参数
	private File titleImgFile;
	private String titleImgFileFileName;

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	// 添加促销信息方法
	@Action(value = "promotion_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
	public String save() throws IOException {
		// 宣传图上传，在数据表中保存宣传图路径
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");// 绝对路径
		String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/"; // 相对路径
		// 生成随机图片名
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		String randomFileName = uuid + ext;
		// 图片上传保存（绝对路径）
		File destFile = new File(savePath + "/" + randomFileName);
		FileUtils.copyFile(titleImgFile, destFile);

		// 将保存图片的相对路径保存到数据表中
		model.setTitleImg(saveUrl + randomFileName);
		// 调用业务层保存数据
		promotionService.save(model);
		return SUCCESS;
	}

	// 分页查询促销信息方法
	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 封装分页查询的对象
		Pageable pageable = new PageRequest(page - 1, rows);
		// 调用业务层查询数据
		Page<Promotion> pageData = promotionService.findPageData(pageable);
		// 将分页查询数据结果压入值栈中
		pushPageDataToValueStack(pageData);

		return SUCCESS;
	}
}
