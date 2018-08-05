package cn.itcast.bos.service.take_delivery.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.take_delivery.WayBillService;

/**
 * @description:运单的Service实现
 */
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
    // 注入Dao对象
    @Autowired
    private WayBillRepository wayBillRepository;

    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;
 
    // 快速运单录入保存方法
    @Override
    public void quickSave(WayBill wayBill) {
        wayBillRepository.save(wayBill);
    }

    // 快速运单录入分页查询方法
    @Override
    public Page<WayBill> findPageData(WayBill wayBill, Pageable pageable) {
        // 判断WayBill中是否有条件存在
        if (StringUtils.isBlank(wayBill.getWayBillNum()) && StringUtils.isBlank(wayBill.getSendAddress())
                && StringUtils.isBlank(wayBill.getRecAddress()) && StringUtils.isBlank(wayBill.getSendProNum())
                && (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
            // 无条件查询，查询数据库
            return wayBillRepository.findAll(pageable);
        } else {
            // 有条件查询，查询索引库
            // BoolQueryBuilder布尔查询，多条件组合查询，其中有must相对于and,mustNot相对于not,should相当于or
            BoolQueryBuilder query = new BoolQueryBuilder();
            // 向组合查询添加条件
            if (StringUtils.isNotBlank(wayBill.getWayBillNum())) {
                // 运单号查询
                QueryBuilder termQuery = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
                query.must(termQuery);
            }

            if (StringUtils.isNotBlank(wayBill.getSendAddress())) {
                // 发货地查询 模糊查询
                // 情况一：查询是词条的一部分
                QueryBuilder wildcardQuery =
                        new WildcardQueryBuilder("sendAddress", "*" + wayBill.getSendAddress() + "*");
                // 情况二：多个词条组合查询，进行分词后匹配查询
                QueryBuilder queryStringQuery = new QueryStringQueryBuilder(wayBill.getSendAddress())
                        .field("sendAddress").defaultOperator(Operator.AND);
                // 两种情况取or
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(wildcardQuery);
                boolQueryBuilder.should(queryStringQuery);
                query.must(boolQueryBuilder);
            }

            if (StringUtils.isNotBlank(wayBill.getRecAddress())) {
                // 收货地查询 模糊查询
                // 情况一：查询是词条的一部分
                QueryBuilder wildcardQuery =
                        new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");
                // 情况二：多个词条组合查询，进行分词后匹配查询
                QueryBuilder queryStringQuery = new QueryStringQueryBuilder(wayBill.getRecAddress())
                        .field("recAddress").defaultOperator(Operator.AND);
                // 两种情况取or
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(wildcardQuery);
                boolQueryBuilder.should(queryStringQuery);
                query.must(boolQueryBuilder);
            }

            if (StringUtils.isNotBlank(wayBill.getSendProNum())) {
                // 快递产品类型查询
                QueryBuilder termQuery = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
                query.must(termQuery);
            }

            if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
                // 运单状态查询
                QueryBuilder termQuery = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
                query.must(termQuery);
            }

            SearchQuery searchQuery = new NativeSearchQuery(query);
            searchQuery.setPageable(pageable); // 分页效果
            return wayBillIndexRepository.search(searchQuery);
        }
    }

    // 根据运单号查询运单方法
    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }

    // 运单录入保存方法,注意运单号id
    @Override
    public void save(WayBill wayBill) {
        // 判断运单号是否存在
        Integer id = wayBillRepository.findIdByWayBillNum(wayBill.getWayBillNum());
        if (id != null) {
            wayBill.setId(id);
        }
        // 保存运单
        wayBillRepository.save(wayBill);
        // 保存索引
        wayBillIndexRepository.save(wayBill);
    }

}
