package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;

/**
 * @description:收派标准的Dao接口
 * Spring data jpa的查询API:
 * 			save(Object)、delete(Object)、deteleAll()、findAll()、findOne(id)、count
 * 			按照指定命名规则查询（findBy列名）
 * 			自定义命名规则条件查询
 * 				需加@Query注解,nativeQuery=false是JPQL语法,nativeQuery=true是SQL语法
 * 				如果是修改或删除还需加@Modifying注解
 */
public interface StandardRepository extends JpaRepository<Standard, Integer> {

	// 按照指定命名规则查询（findBy列名）
	// 根据收派标准名称查询
	public List<Standard> findByName(String name);

	// 自定义命名规则条件查询
	// 需加@Query注解,nativeQuery=false是JPQL语法,nativeQuery=true是SQL语法
	@Query(value = "from Standard where name=?", nativeQuery = false)
	public List<Standard> QueryName(String name);
	
	@Query(value="update Standard set minLength=?2 where id=?1")
	@Modifying
	public void updateMinLength(Integer id,Integer minLength);
}
