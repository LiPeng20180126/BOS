package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description:区域的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends BaseAction<Area> {

	// 注入Service对象
	@Autowired
	private AreaService areaService;

	// 添加区域方法
	@Action(value = "area_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/area.html") })
	public String save() {
		areaService.add(model);
		return SUCCESS;
	}

	// 属性驱动接收文件上传参数
	private File file;
	private String fileFileName;

	public void setFile(File file) {
		this.file = file;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	// 批量数据的区域导入方法
	@Action(value = "area_batchImport")
	public String batchImport() throws IOException {
		List<Area> areas = new ArrayList<Area>();

		// 使用POI技术编写解析代码逻辑
		if (fileFileName.endsWith(".xls")) {
			// 上传文件是以.xls结尾，使用POI中HSSFWorkbook
			// 获取解析Excel文件对象
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
			// 读取一个sheet
			HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
			// 读取sheet中每一行
			for (Row row : sheet) {
				// 一行数据对应一个区域对象
				if (row.getRowNum() == 0) {
					// 第一行跳过
					continue;
				}
				if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
					// 跳过空行
					continue;
				}
				// 区域赋值
				Area area = new Area();
				area.setId(row.getCell(0).getStringCellValue());
				area.setProvince(row.getCell(1).getStringCellValue());
				area.setCity(row.getCell(2).getStringCellValue());
				area.setDistrict(row.getCell(3).getStringCellValue());
				area.setPostcode(row.getCell(4).getStringCellValue());
				// 基于pinyin4j生成城市简码和编码
				String province = area.getProvince();
				String city = area.getCity();
				String district = area.getDistrict();
				province = province.substring(0, province.length() - 1);
				city = city.substring(0, city.length() - 1);
				district = district.substring(0, district.length() - 1);
				// 获取简码
				String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
				StringBuffer sb = new StringBuffer();
				for (String hearStr : headArray) {
					sb.append(hearStr);
				}
				area.setShortcode(sb.toString());
				// 获取城市编码
				String citycode = PinYin4jUtils.hanziToPinyin(city, "");
				area.setCitycode(citycode);

				// 将区域添加到集合中
				areas.add(area);
			}
		} else if (fileFileName.endsWith(".xlsx")) {
			// 上传文件是以.xlsx结尾,使用POI中XSSFWorkbook
			// 加载解析Excel文件对象
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
			// 读取一个sheet
			XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
			// 读取sheet中每一行数据
			for (Row row : sheet) {
				// 一行数据对应一个对象
				if (row.getRowNum() == 0) {
					// 第一行跳过
					continue;
				}
				if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
					// 空行跳过
					continue;
				}
				// 区域Area赋值
				Area area = new Area();
				area.setId(row.getCell(0).getStringCellValue());
				area.setProvince(row.getCell(1).getStringCellValue());
				area.setCity(row.getCell(2).getStringCellValue());
				area.setDistrict(row.getCell(3).getStringCellValue());
				area.setPostcode(row.getCell(4).getStringCellValue());
				// 基于pinyin4j生成城市简码和编码
				String province = area.getProvince();
				String city = area.getCity();
				String district = area.getDistrict();
				province = province.substring(0, province.length() - 1);
				city = city.substring(0, city.length() - 1);
				district = district.substring(0, district.length() - 1);
				// 生成简码
				String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
				StringBuffer sb = new StringBuffer();
				for (String headStr : headArray) {
					sb.append(headStr);
				}
				area.setShortcode(sb.toString());
				// 生成城市编码
				String citycode = PinYin4jUtils.hanziToPinyin(city, "");
				area.setCitycode(citycode);

				// 将区域赋值到集合中
				areas.add(area);
			}
		}
		// 调用业务层批量保存数据
		areaService.save(areas);
		return NONE;
	}

	// 带条件分页查询区域的方法
	@Action(value = "area_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 封装pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);
		// 封装Specification对象
		Specification<Area> specification = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				// 根据省份进行单表查询
				if (StringUtils.isNotBlank(model.getProvince())) {
					Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
					list.add(p1);
				}
				// 根据城市进行单表查询
				if (StringUtils.isNotBlank(model.getCity())) {
					Predicate p2 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
					list.add(p2);
				}
				// 根据区域进行单表查询
				if (StringUtils.isNotBlank(model.getDistrict())) {
					Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
					list.add(p3);
				}

				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		// 调用业务层查询，返回Page对象
		Page<Area> pageData = areaService.findPageData(specification, pageable);

		// 调用将分页查询数据结果压入值栈的方法
		pushPageDataToValueStack(pageData);

		return SUCCESS;
	}

	// 查询所有区域的方法
	@Action(value = "area_finAll", results = { @Result(name = "success", type = "json") })
	public String finAll() {
		// 调用业务层查询数据
		List<Area> areas = areaService.findAll();
		// 将数据压入值栈顶部
		ActionContext.getContext().getValueStack().push(areas);
		return SUCCESS;
	}
}
