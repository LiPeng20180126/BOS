package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

/**
 * @description:收派标准的Service实现
 */
@Service
@Transactional
public class StandardServiceImpl implements StandardService {
    // 注入收派标准的Repository对象
    @Autowired
    private StandardRepository standardRepository;

    // 添加收派标准信息方法
    @Override
    @CacheEvict(value = "standard", allEntries = true)
    public void save(Standard standard) {
        standardRepository.save(standard);
    }

    // 分页查询收派标准信息方法
    @Override
    @Cacheable(value = "standard", key = "#pageable.pageNumber+'_'+#pageable.pageSize")
    public Page<Standard> findPageData(Pageable pageable) {
        System.out.println("分页查询收派标准信息");
        return standardRepository.findAll(pageable);
    }

    // 查询所有收派标准信息方法
    @Override
    @Cacheable("standard")
    public List<Standard> findAll() {
        System.out.println("查询所有收派标准信息");
        return standardRepository.findAll();
    }

}
