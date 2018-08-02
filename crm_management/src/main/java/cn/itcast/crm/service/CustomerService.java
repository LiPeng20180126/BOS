package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

/**
 * @description:客户的Service接口
 */
public interface CustomerService {

    // 查询所有未关联客户方法
    @GET
    @Path("/noassociationcustomer")
    @Produces({ "application/xml", "application/json" })
    public List<Customer> findNoAssociationCustomer();

    // 查询已经关联到指定定区的客户方法
    @GET
    @Path("/associationcustomer/{fixedareaid}")
    @Produces({ "application/xml", "application/json" })
    public List<Customer> findAssociationFixedAreaCustomer(@PathParam("fixedareaid") String fixedAreaId);

    // 将客户关联到定区方法,将所有客户id拼接成字符串：1-2-3
    @PUT
    @Path("/customerassociationtofixedarea")
    public void customerAssociationToFixedArea(@QueryParam("fixedAreaId") String fixedAreaId,
            @QueryParam("customerIdStr") String customerIdStr);

    // 注册客户方法
    @POST
    @Path("/customer")
    @Consumes({ "application/xml", "application/json" })
    public void regist(Customer customer);

    // 通过号码查询客户信息
    @GET
    @Path("/customer/telephone/{telephone}")
    @Consumes({ "application/xml", "application/json" })
    public Customer findByTelephone(@PathParam("telephone") String telephone);

    // 通过号码修改客户绑定邮箱类型
    @PUT
    @Path("/customer/updateType/{telephone}")
    @Consumes({ "application/xml", "application/json" })
    public void updateType(@PathParam("telephone") String telephone);

    // 客户登录的方法
    @GET
    @Path("/customer/login/{email}/{password}")
    @Produces({ "application/xml", "application/json" })
    public Customer login(@PathParam("email") String email,@PathParam("password") String password);
    
    // 根据客户地区查询定区编码方法
    @GET
    @Path("/customer/findFixedAreaIdByAddress")
    @Consumes({ "application/xml", "application/json" })
    @Produces({ "application/xml", "application/json" })
    public String findFixedAreaIdByAddress(@QueryParam("address") String address);
}
