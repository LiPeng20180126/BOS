package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Area;

/**
 * @description:区域的Service接口
 */
public interface AreaService {

    // 批量数据的区域导入方法
    public void save(List<Area> areas);

    // 带条件分页查询区域的方法
    public Page<Area> findPageData(Specification<Area> specification, Pageable pageable);

    // 添加区域的方法
    public void add(Area area);

    // 查询所有区域的方法
    public List<Area> findAll();

    // 通过省市区查询区域方法
    public Area findByProvinceAndCityAndDistrict(String province, String city, String district);

}
