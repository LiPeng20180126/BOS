package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.Area;

/**
 * @description:区域的Dao接口
 */
public interface AreaRepository extends JpaRepository<Area, String>, JpaSpecificationExecutor<Area> {

    // 通过省市区查询区域的方法
    public Area findByProvinceAndCityAndDistrict(String province, String city, String district);

}
