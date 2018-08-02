package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Standard;

/**
 * @description:收派标准的Service接口
 */
public interface StandardService {

	public void save(Standard standard);

	public Page<Standard> findPageData(Pageable pageable);

	public List<Standard> findAll();

	
}