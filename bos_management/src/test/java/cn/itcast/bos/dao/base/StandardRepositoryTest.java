package cn.itcast.bos.dao.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @description:Spring data jpa的条件查询测试
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class StandardRepositoryTest {

	@Autowired
	private StandardRepository standardRepository;
	
	@Test
	public void test01(){
		System.out.println(standardRepository.findByName("0-10斤"));
	}
	
	@Test
	public void test02(){
		System.out.println(standardRepository.QueryName("10-20斤"));
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void test03(){
		standardRepository.updateMinLength(2, 12);
	}
	
}
