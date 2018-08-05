package cn.itcast.bos.index;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import cn.itcast.bos.domain.take_delivery.WayBill;
/**
 * @description:运单检索的Dao接口
 */
public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill, Integer>{

}
