package cn.itcast.bos.service.base;

import java.util.List;

import cn.itcast.bos.domain.base.TakeTime;

/**
 * @description:收派时间管理的Service接口
 */
public interface TakeTimeService {
	
	// 查询所有收派时间方法
	public List<TakeTime> findAll();

}
