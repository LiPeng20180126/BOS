package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.service.base.SubAreaService;
import cn.itcast.bos.utils.FileUtils;
import cn.itcast.bos.web.action.base.common.BaseAction;

/**
 * @description:分区的Action
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class SubAreaAction extends BaseAction<SubArea> {

    // 注入service对象
    @Autowired
    private SubAreaService subAreaService;

    @Autowired
    private AreaService areaService;

    // 添加分区的方法
    @Action(value = "subArea_save", results = {
            @Result(name = "success", type = "redirect", location = "./pages/base/sub_area.html") })
    public String save() {
        subAreaService.save(model);
        return SUCCESS;
    }

    // 分页条件查询方法
    @Action(value = "subArea_pageQuery", results = { @Result(name = "success", type = "json") })
    public String pageQuery() {
        // 封装Pageable对象
        Pageable pageable = new PageRequest(page - 1, rows);
        // 封装Specification对象
        Specification<SubArea> specification = new Specification<SubArea>() {
            @Override
            public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                // 根据关键字进行单表查询
                if (StringUtils.isNotBlank(model.getKeyWords())) {
                    Predicate p1 = cb.like(root.get("keyWords").as(String.class), "%" + model.getKeyWords() + "%");
                    list.add(p1);
                }
                // 根据省份进行多表查询
                Join<SubArea, Area> areaRoot = root.join("area", JoinType.INNER);
                if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getProvince())) {
                    Predicate p2 = cb.like(areaRoot.get("province").as(String.class),
                            "%" + model.getArea().getProvince() + "%");
                    list.add(p2);
                }
                // 根据城市进行多表查询
                if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getCity())) {
                    Predicate p3 =
                            cb.like(areaRoot.get("city").as(String.class), "%" + model.getArea().getCity() + "%");
                    list.add(p3);
                }
                // 根据区（县）进行多表查询
                if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getDistrict())) {
                    Predicate p4 = cb.like(areaRoot.get("district").as(String.class),
                            "%" + model.getArea().getDistrict() + "%");
                    list.add(p4);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        // 调用业务层查询数据
        Page<SubArea> pageData = subAreaService.findPageData(pageable, specification);
        // 将分页查询数据结果压入值栈中
        pushPageDataToValueStack(pageData);

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

    // 批量导入分区的方法
    @Action(value = "subArea_batchImport")
    public String batchImport() throws IOException {
        List<SubArea> subAreas = new ArrayList<SubArea>();
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
                // 分区赋值
                SubArea subArea = new SubArea();
                subArea.setSubAreaNum(row.getCell(0).getStringCellValue());
                Area area = areaService.findByProvinceAndCityAndDistrict(row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(), row.getCell(3).getStringCellValue());
                subArea.setArea(area);
                subArea.setKeyWords(row.getCell(4).getStringCellValue());
                subArea.setStartNum(row.getCell(5).getStringCellValue());
                subArea.setEndNum(row.getCell(6).getStringCellValue());
                subArea.setSingle(row.getCell(7).getStringCellValue().charAt(0));
                subArea.setAssistKeyWords(row.getCell(8).getStringCellValue());

                // 将分区添加到集合中
                subAreas.add(subArea);
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
                // 分区赋值
                SubArea subArea = new SubArea();
                subArea.setSubAreaNum(row.getCell(0).getStringCellValue());
                Area area = areaService.findByProvinceAndCityAndDistrict(row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(), row.getCell(3).getStringCellValue());
                subArea.setArea(area);
                subArea.setKeyWords(row.getCell(4).getStringCellValue());
                subArea.setStartNum(row.getCell(5).getStringCellValue());
                subArea.setEndNum(row.getCell(6).getStringCellValue());
                subArea.setSingle(row.getCell(7).getStringCellValue().charAt(0));
                subArea.setAssistKeyWords(row.getCell(8).getStringCellValue());

                // 将分区添加到集合中
                subAreas.add(subArea);
            }
        }
        // 调用业务层批量保存数据
        subAreaService.batchSave(subAreas);
        return NONE;
    }

    // 批量导出分区方法
    @Action(value = "subArea_batchExport")
    public String batchExport() throws IOException {
        // 查询所有分区的数据
        List<SubArea> subAreas = subAreaService.findAll();

        // 使用POI技术将数据写到Excel
        // 在内存中创建一个Excel文件
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        // 创建一个sheet
        XSSFSheet sheet = xssfWorkbook.createSheet("分区数据");
        // 创建标题行
        XSSFRow headRow = sheet.createRow(0);
        headRow.createCell(0).setCellValue("分区编号");
        headRow.createCell(1).setCellValue("省");
        headRow.createCell(2).setCellValue("市");
        headRow.createCell(3).setCellValue("区");
        headRow.createCell(4).setCellValue("关键字");
        headRow.createCell(5).setCellValue("起始号");
        headRow.createCell(6).setCellValue("终止号");
        headRow.createCell(7).setCellValue("单双号");
        headRow.createCell(8).setCellValue("辅助关键字");
        for (SubArea subArea : subAreas) {
            XSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
            dataRow.createCell(0).setCellValue(subArea.getSubAreaNum());
            dataRow.createCell(1).setCellValue(subArea.getArea().getProvince());
            dataRow.createCell(2).setCellValue(subArea.getArea().getCity());
            dataRow.createCell(3).setCellValue(subArea.getArea().getDistrict());
            dataRow.createCell(4).setCellValue(subArea.getKeyWords());
            dataRow.createCell(5).setCellValue(subArea.getStartNum());
            dataRow.createCell(6).setCellValue(subArea.getEndNum());
            dataRow.createCell(7).setCellValue(subArea.getSingle().toString());
            dataRow.createCell(8).setCellValue(subArea.getAssistKeyWords());
        }

        // 使用输出流进行文件下载（一个流、两个头）
        String filename = "分区数据.xlsx";
        String contentType = ServletActionContext.getServletContext().getMimeType(filename);
        ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();
        ServletActionContext.getResponse().setContentType(contentType);

        // 获取客户端浏览器类型
        String agent = ServletActionContext.getRequest().getHeader("User-Agent");
        filename = FileUtils.encodeDownloadFilename(filename, agent);
        ServletActionContext.getResponse().setHeader("content-disposition", "attachment;filename=" + filename);
        xssfWorkbook.write(out);

        return NONE;
    }
}
