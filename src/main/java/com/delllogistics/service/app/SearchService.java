package com.delllogistics.service.app;

import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.app.SearchHistoryLog;
import com.delllogistics.entity.user.User;
import com.delllogistics.repository.app.SearchHistoryLogRepository;
import com.delllogistics.repository.goods.GoodsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * About Search.<br/>
 * User: jiajie<br/>
 * Date: 20/11/2017<br/>
 * Time: 7:37 AM<br/>
 */
@Service
public class SearchService {

    private final GoodsRepository goodsRepository;
    private final SearchHistoryLogRepository searchHistoryLogRepository;

    public SearchService(GoodsRepository goodsRepository, SearchHistoryLogRepository searchHistoryLogRepository) {
        this.goodsRepository = goodsRepository;
        this.searchHistoryLogRepository = searchHistoryLogRepository;
    }


    @Transactional
    public Page<Goods> findGoods(int page, int size, User user, String content, String[] ages, String[] goodsTags, Long[] goodsSeries) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<Goods> specification = (Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(content)) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + content + "%"));
            }

            if (!StringUtils.isEmpty(ages) && ages.length > 0) {
                Join<Object, Object> joinGoodsParamItems = root.join("goodsParamItems");
                Join<Object, Object> joinGoodsParam = joinGoodsParamItems.join("goodsParam");
                Join<Object, Object> joinGoodsParamValue = joinGoodsParam.join("goodsParamValues");
                predicates.add(joinGoodsParamValue.get("value").as(String.class).in((Object[]) ages));
            }

            if (!StringUtils.isEmpty(goodsSeries) && goodsSeries.length > 0)
                predicates.add(root.join("goodsSeries").get("id").as(Long.class).in((Object[]) goodsSeries));

            if (!StringUtils.isEmpty(goodsTags) && goodsTags.length > 0)
                predicates.add(root.join("goodsTags").get("name").as(String.class).in((Object[]) goodsTags));


            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsRepository.findAll(specification, pageable);
    }

    private void insertALog(String content, User user) {
        SearchHistoryLog searchHistoryLog = new SearchHistoryLog();
        searchHistoryLog.setContent(content);
        searchHistoryLog.setUser(user);
        searchHistoryLogRepository.save(searchHistoryLog);
    }

    public List<String> findHotSearchs() {
        return searchHistoryLogRepository.findHotSearchs();
    }
}
