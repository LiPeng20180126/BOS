package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.PromotionRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;

/**
 * @description:促销信息的Service实现
 */
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
    // 注入Dao对象
    @Autowired
    private PromotionRepository promotionRepository;

    // 添加促销信息方法
    @Override
    public void save(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    // 分页查询促销信息方法
    @Override
    public Page<Promotion> findPageData(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }

    // 根据page和rows查询返回分页数据
    @Override
    public PageBean<Promotion> findPageData(int page, int rows) {
        // 封装分页查询对象
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Promotion> pageData = promotionRepository.findAll(pageable);
        // 封装自定义分页对象
        PageBean<Promotion> pageBean = new PageBean<Promotion>();
        pageBean.setTotalCount(pageData.getTotalElements());
        pageBean.setPageData(pageData.getContent());

        return pageBean;
    }

    // 根据id查询数据
    @Override
    public Promotion findById(Integer id) {
        return promotionRepository.findOne(id);
    }

    // 活动过期自动处理方法
    @Override
    public void updateStatus(Date date) {
        promotionRepository.updateStatus(date);
    }

}
