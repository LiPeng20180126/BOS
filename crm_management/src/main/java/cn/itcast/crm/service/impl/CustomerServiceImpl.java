package cn.itcast.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

/**
 * @description:客户的Service实现
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	// 注入Dao对象
	@Autowired
	private CustomerRepository customerRepository;

	// 查询所有未关联客户方法
	@Override
	public List<Customer> findNoAssociationCustomer() {
		// fixedAreaId is null;
		return customerRepository.findByFixedAreaIdIsNull();
	}

	// 查询已经关联到指定定区的客户方法
	@Override
	public List<Customer> findAssociationFixedAreaCustomer(String fixedAreaId) {
		// fixedAreaId is ?
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	// 将客户关联到定区方法,将所有客户id拼接成字符串：1-2-3
	@Override
	public void customerAssociationToFixedArea(String fixedAreaId, String customerIdStr) {
		// 解除关联动作
		customerRepository.clearFixedAreaId(fixedAreaId);
		
		// customerIdStr是否为空
		if (StringUtils.isBlank(customerIdStr) || customerIdStr.equals("null")) {
			return;
		}

		String[] idArray = customerIdStr.split("-");
		for (String idStr : idArray) {
			Integer id = Integer.parseInt(idStr);
			customerRepository.updateFixedAreaId(fixedAreaId, id);
		}
	}

	// 注册客户方法
	@Override
	public void regist(Customer customer) {
		customerRepository.save(customer);		
	}

	// 通过号码查询客户信息
	@Override
	public Customer findByTelephone(String telephone) {
		return customerRepository.findByTelephone(telephone);
	}

	// 通过号码修改客户绑定邮箱类型
	@Override
	public void updateType(String telephone) {
		customerRepository.updateType(telephone);
	}

	// 客户登录的方法
    @Override
    public Customer login(String email,String password) {
        return customerRepository.findByEmailAndPassword(email,password);
    }

    // 根据客户地区查询定区编码方法
    @Override
    public String findFixedAreaIdByAddress(String address) {
        return customerRepository.findFixedAreaIdByAddress(address);
    }

}
