package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.take_delivery.OrderRepository;
import cn.itcast.bos.dao.take_delivery.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.crm.contant.Contants;

/**
 * @description:订单的Service实现
 */
@Service
@Transactional
@SuppressWarnings("all")
public class OrderServiceImpl implements OrderService {

    // 注入Dao对象
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private FixedAreaRepository fixedAreaRepository;

    @Autowired
    private WorkBillRepository workBillRepository;

    // 注入ActiveMQ的JmsTemplate
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    // 保存订单方法
    @Override
    public void saveOrder(Order order) {
        // order基本信息设置
        order.setOrderNum(UUID.randomUUID().toString().replace("-", "")); // 订单号
        order.setOrderTime(new Date()); // 下单时间
        order.setStatus("1"); // 订单状态：待取件

        // 查询寄件人省市区信息
        Area sendArea = order.getSendArea();
        Area persistSendArea = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(),
                sendArea.getCity(), sendArea.getDistrict());
        order.setSendArea(persistSendArea);
        // 查询收件人省市区信息
        Area recArea = order.getRecArea();
        Area persistRecArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(),
                recArea.getDistrict());
        order.setRecArea(persistRecArea);

        // 自动分单逻辑1：基于CRM系统客户地址完全匹配，获取定区，匹配快递员
        String fixedAreaId = WebClient.create(Contants.CRM_MANAGEMENT_URL
                + "/crm_management/services/customerService/customer/findFixedAreaIdByAddress?address="
                + order.getSendAddress()).accept(MediaType.APPLICATION_JSON).get(String.class);
        if (fixedAreaId != null) {
            // 查询定区关联的快递员
            FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
            Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
            if (iterator.hasNext()) {
                Courier courier = iterator.next();
                if (courier != null) {
                    System.out.println("自动分单成功");
                    autoOrder(order, courier);
                    // 生成工单，发送短信
                    generateWorkBill(order);

                    return;
                }
            }
        }

        // 自动分单逻辑2：通过省市区查询分区的关键字或辅助关键字，匹配地址，自动分单
        // 根据关键字来匹配
        for (SubArea subArea : persistSendArea.getSubareas()) {
            if (order.getSendAddress().contains(subArea.getKeyWords())) {
                // 查询定区关联的快递员
                FixedArea fixedArea = subArea.getFixedArea();
                Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
                if (iterator.hasNext()) {
                    Courier courier = iterator.next();
                    if (courier != null) {
                        System.out.println("自动分单成功");
                        autoOrder(order, courier);
                        // 生成工单，发送短信
                        generateWorkBill(order);

                        return;
                    }
                }
            }
        }
        // 根据辅助关键字来匹配
        for (SubArea subArea : persistSendArea.getSubareas()) {
            if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {
                // 查询定区关联的快递员
                FixedArea fixedArea = subArea.getFixedArea();
                Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
                if (iterator.hasNext()) {
                    Courier courier = iterator.next();
                    if (courier != null) {
                        System.out.println("自动分单成功");
                        autoOrder(order, courier);
                        // 生成工单，发送短信
                        generateWorkBill(order);

                        return;
                    }
                }
            }
        }

        // 进行人工分单
        order.setOrderType("2");
        orderRepository.save(order);

    }

    // 生成工单发送短信方法
    private void generateWorkBill(Order order) {
        // 生成工单
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("新单");
        workBill.setBuildtime(new Date());
        workBill.setRemark(order.getRemark());
        String smsNumber = RandomStringUtils.randomNumeric(6);
        workBill.setSmsNumber(smsNumber);
        workBill.setCourier(order.getCourier());
        workBill.setOrder(order);
        workBillRepository.save(workBill);
        // 发送短信
        jmsTemplate.send("bos_sms", new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", order.getCourier().getTelephone());
                mapMessage.setString("msg", "短信序号：" + smsNumber + ",取件地址：" + order.getSendAddress() + ",联系人:"
                        + order.getSendName() + ",手机:" + order.getSendMobile() + "，快递员捎话：" + order.getSendMobileMsg());
                return mapMessage;
            }
        });
        // 修改工单状态
        workBill.setPickstate("已通知");
    }

    // 自动分单的方法
    private void autoOrder(Order order, Courier courier) {
        // 设置分单类型：自动分单
        order.setOrderType("1");
        // 关联快递员到订单上
        order.setCourier(courier);
        // 保存订单
        orderRepository.save(order);
    }

    // 根据订单号查询订单方法
    @Override
    public Order findByOrderNum(String orderNum) {
        return orderRepository.findByOrderNum(orderNum);
    }

}
