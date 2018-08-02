package cn.itcast.bos.service.take_delivery;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

/**
 * @description:促销信息的Service接口
 */
public interface PromotionService {

	// 添加促销信息方法
	public void save(Promotion promotion);

	// 分页查询促销信息方法
	public Page<Promotion> findPageData(Pageable pageable);
	
    // 根据page和rows查询返回分页数据
	@GET
	@Path("/promotion")
	@Produces({"application/xml","application/json"})
	public PageBean<Promotion> findPageData(@QueryParam("page") int page,@QueryParam("rows") int rows);
	
	// 根据id查询数据
	@GET
	@Path("/promotion/{id}")
	@Produces({"application/xml","application/json"})
	public Promotion findById(@PathParam("id") Integer id);

	// 活动过期自动处理方法
    public void updateStatus(Date date);
}
