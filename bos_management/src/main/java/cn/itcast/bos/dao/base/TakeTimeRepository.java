package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.base.TakeTime;

/**
 * @description:收派时间管理的Dao接口
 */
public interface TakeTimeRepository extends JpaRepository<TakeTime, Integer> {

}
