package cn.itcast.crm.service.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.crm.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CustomerServiceTest {

	//注入Service
	@Autowired
	private CustomerService customerService;
	
	@Test
	public void testFindNoAssociationCustomer() {
		System.out.println(customerService.findNoAssociationCustomer());
	}

	@Test
	public void testFindAssociationFixedAreaCustomer() {
		System.out.println(customerService.findAssociationFixedAreaCustomer("DQ001"));
	}

	@Test
	public void testCustomerAssociationToFixedArea() {
		customerService.customerAssociationToFixedArea("DQ001", "1-2");
	}

}
