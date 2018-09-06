package com.delllogistics.service.order;

import com.delllogistics.dto.order.OrderItemEvalueteSearch;
import com.delllogistics.entity.order.OrderItemEvaluate;
import com.delllogistics.repository.order.OrderItemRepository;
import com.delllogistics.repository.goods.GoodsRepository;
import com.delllogistics.repository.order.OrderItemEvaluateRepository;
import com.delllogistics.repository.sys.SysFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.*;

@Service
public class OrderItemEvaluateService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());




    private final OrderItemRepository orderItemRepository;

    private final GoodsRepository goodsRepository;

    private final SysFileRepository sysFileRepository;

    private final OrderItemEvaluateRepository orderItemEvaluateRepository;

    public OrderItemEvaluateService(OrderItemRepository orderItemRepository, GoodsRepository goodsRepository, SysFileRepository sysFileRepository, OrderItemEvaluateRepository orderItemEvaluateRepository) {
        this.orderItemRepository = orderItemRepository;
        this.goodsRepository = goodsRepository;
        this.sysFileRepository = sysFileRepository;
        this.orderItemEvaluateRepository = orderItemEvaluateRepository;
    }

    public Page<OrderItemEvaluate> findAll(OrderItemEvalueteSearch orderItemEvalueteSearch){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(orderItemEvalueteSearch.getPage(), orderItemEvalueteSearch.getSize(), sort);
        Specification<OrderItemEvaluate> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> joinOrderItem = root.join("orderItem");
            Join<Object, Object> joinOrderMain = joinOrderItem.join("orderMain");


            if(orderItemEvalueteSearch != null){
                if (!StringUtils.isEmpty(orderItemEvalueteSearch.getOrderMainId()   )) {
                    predicates.add(criteriaBuilder.equal(joinOrderMain.get("id"), orderItemEvalueteSearch.getGoodsId() ));
                }
                //查询关联商品
                if (!StringUtils.isEmpty(orderItemEvalueteSearch.getGoodsId()   )) {
                    predicates.add(criteriaBuilder.equal(root.get("goods").get("id"), orderItemEvalueteSearch.getGoodsId() ));
                }


                //查询关联用户名
                if (  !StringUtils.isEmpty(orderItemEvalueteSearch.getUsername()) ) {
                    predicates.add(
                            criteriaBuilder.equal(root.join("user").get("username"), orderItemEvalueteSearch.getUsername())
                    );
                }

                if (!StringUtils.isEmpty(orderItemEvalueteSearch.getName())) {
                    predicates.add(criteriaBuilder.like(root.get("goods").get("name"), "%" + orderItemEvalueteSearch.getName() + "%"));
                }
                if (!StringUtils.isEmpty(orderItemEvalueteSearch.getCompanyId())) {
                    predicates.add(criteriaBuilder.equal(root.get("goods").get("company").get("id"), orderItemEvalueteSearch.getCompanyId()));
                }

                //查询关联创建日期内
                if (  !StringUtils.isEmpty(orderItemEvalueteSearch.getStartTime()) && !StringUtils.isEmpty(orderItemEvalueteSearch.getEndTime()) ) {
                    predicates.add(
                            criteriaBuilder.between(root.get("createTime"), orderItemEvalueteSearch.getStartTime(),orderItemEvalueteSearch.getEndTime())
                    );
                }
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return orderItemEvaluateRepository.findAll(specification, pageable);
    }


}
