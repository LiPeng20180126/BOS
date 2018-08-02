package cn.itcast.fore.web.action;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

import cn.itcast.crm.contant.Contants;
import cn.itcast.crm.domain.Customer;
import cn.itcast.fore.utils.MailUtils;
import cn.itcast.fore.utils.SmsUtils;
import cn.itcast.fore.web.action.common.BaseAction;

/**
 * @description:客户的Action
 */
@SuppressWarnings("all")
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CustomerAction extends BaseAction<Customer> {

    // 注入ActiveMQ的JmsTemplate
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    // 发送手机验证码方法
    @Action(value = "customer_sendSms")
    public String sendSms() {

        // 生成6位手机短信验证码
        String randomCode = RandomStringUtils.randomNumeric(6);
        // 将短信验证码保存在session中
        ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), randomCode);
        // 打印短信验证码
        System.out.println("生成的手机验证码为：" + randomCode);

        // 调用mq服务，发送一条消息
        jmsTemplate.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", model.getTelephone());
                mapMessage.setString("randomCode", randomCode);
                return mapMessage;
            }
        });

        /*
         * try { // 调用SMS服务发送短信 SendSmsResponse sendSms = SmsUtils.sendSms(model.getTelephone(), randomCode); } catch
         * (ClientException e) { System.out.println("短信发送失败"); e.printStackTrace(); }
         */

        return NONE;
    }

    // 属性驱动接收验证码参数
    private String checkCode;

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    // 注入Redis模板
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 客户注册的方法
    @Action(value = "customer_regist", results = {
            @Result(name = "success", type = "redirect", location = "signup-success.html"),
            @Result(name = "input", type = "redirect", location = "signup.html") })
    public String regist() {
        // 从session中获取短信验证码,清除session值
        String checkCodeSession =
                (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
        ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), null);
        // 校验短信验证码
        if (checkCodeSession == null || !checkCodeSession.equals(checkCode)) {
            System.out.println("短信验证码错误...");
            return INPUT;
        }
        // 调用webService保存客户信息
        WebClient.create(Contants.CRM_MANAGEMENT_URL + "/crm_management/services/customerService/customer")
                .type(MediaType.APPLICATION_JSON).post(model);
        System.out.println("客户注册成功...");

        // 发送一封激活邮件
        // 生成激活码
        String activeCode = RandomStringUtils.randomNumeric(32);
        // String activeCode = UUID.randomUUID().toString().replace("-", "");
        // 将激活码保存在redis中
        redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 24, TimeUnit.HOURS);
        // 调用MailUtils工具发送激活邮件
        String content = "尊敬的客户您好，请于24小时内，进行邮箱账户的绑定，点击下面地址完成绑定:<br/><a href='" + MailUtils.activeUrl + "?telephone="
                + model.getTelephone() + "&activeCode=" + activeCode + "'>速运快递邮箱绑定地址</a>";
        MailUtils.sendMail("速运快递激活邮件", content, model.getTelephone());
        return SUCCESS;
    }

    // http://localhost:9003/bos_fore/customer_activeMail?
    // telephone=18956068569&activeCode=18020630430925528877930703333959
    // 属性驱动接收激活码参数
    private String activeCode;

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    // 激活邮件方法
    @Action(value = "customer_activeMail")
    public String activeMail() throws IOException {
        // 响应中文乱码解决
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        // 判断激活码是否有效
        String activeCodeRedis = redisTemplate.opsForValue().get(model.getTelephone());
        if (activeCodeRedis == null || !activeCodeRedis.equals(activeCode)) {
            // 激活码无效
            ServletActionContext.getResponse().getWriter().println("激活码失效，请重新注册绑定邮箱");
        } else {
            // 激活码有效
            // 调用webservice查询客户信息，判定是否绑定
            Customer customer = WebClient.create(Contants.CRM_MANAGEMENT_URL
                    + "/crm_management/services/customerService/customer/telephone/" + model.getTelephone())
                    .type(MediaType.APPLICATION_JSON).get(Customer.class);
            if (customer.getType() == null || customer.getType() != 1) {
                // 客户未绑定,进行绑定
                WebClient.create(Contants.CRM_MANAGEMENT_URL
                        + "/crm_management/services/customerService/customer/updateType/" + model.getTelephone())
                        .put(null);
                ServletActionContext.getResponse().getWriter().println("邮箱绑定成功");
            } else {
                // 客户已绑定
                ServletActionContext.getResponse().getWriter().println("邮箱已经绑定，无须重新绑定");
            }
            // 删除Redis中激活码
            redisTemplate.delete(model.getTelephone());
        }
        return NONE;
    }

    // 客户登录的方法
    @Action(value = "customer_login", results = {
            @Result(name = "success", type = "redirect", location = "./index.html#/myhome"),
            @Result(name = "login", type = "redirect", location = "./login.html") })
    public String login() {
        // 基于webService查询客户信息
        Customer customer = WebClient
                .create(Contants.CRM_MANAGEMENT_URL + "/crm_management/services/customerService/customer/login/"
                        + model.getEmail() + "/" + model.getPassword())
                .accept(MediaType.APPLICATION_JSON).get(Customer.class);
        // 判断客户是否存在
        if (customer == null) {
            // 登录失败
            return LOGIN;
        } else {
            // 登录成功，将客户保存到session中
            ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
            return SUCCESS;
        }
    }

}
