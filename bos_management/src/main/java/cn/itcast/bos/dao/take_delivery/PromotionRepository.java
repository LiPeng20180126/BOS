package cn.itcast.bos.dao.take_delivery;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.take_delivery.Promotion;

/**
 * @description:促销信息的Dao接口
 */
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    // 活动过期自动处理方法
    @Query("update Promotion set status='2' where endDate<? and status='1'")
    @Modifying
    public void updateStatus(Date now);

}
