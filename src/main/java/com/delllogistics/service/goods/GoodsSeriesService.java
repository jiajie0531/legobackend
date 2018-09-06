package com.delllogistics.service.goods;

import com.delllogistics.entity.goods.GoodsSeries;
import com.delllogistics.repository.goods.GoodsSeriesRepository;
import com.delllogistics.util.EntityConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiajie on 27/10/2017.
 */
@Service
public class GoodsSeriesService {

    private final GoodsSeriesRepository goodsSeriesRepository;

    @Autowired
    public GoodsSeriesService(GoodsSeriesRepository goodsSeriesRepository) {
        this.goodsSeriesRepository = goodsSeriesRepository;
    }


    public Page<GoodsSeries> findAll(int page, int size, GoodsSeries goodsSeries,boolean isCondition) {
        Sort sort = new Sort(Sort.Direction.DESC, "sort","updateTime");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<GoodsSeries> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(isCondition){
                predicates.add(criteriaBuilder.equal(root.get("isUsed"),goodsSeries.getIsUsed()));
            }
            if(!StringUtils.isEmpty(goodsSeries.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + goodsSeries.getName() + "%"));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsSeriesRepository.findAll(specification, pageable);
    }
    public Iterable<GoodsSeries> findAllSelect() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<GoodsSeries> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsSeriesRepository.findAll(specification,sort);
    }

    public void save(GoodsSeries goodsSeries) {
        GoodsSeries oldGoodsSeries;
        if (StringUtils.isEmpty(goodsSeries.getId())) {
            oldGoodsSeries = EntityConvertUtil.convertToEntity(goodsSeries, GoodsSeries.class);
        } else {
            oldGoodsSeries = goodsSeriesRepository.findOne(goodsSeries.getId());
            EntityConvertUtil.setFieldToEntity(goodsSeries,oldGoodsSeries);
        }
        goodsSeriesRepository.save(oldGoodsSeries);
    }

    public void saveUsed(GoodsSeries goodsSeries) {
        GoodsSeries oldGoodsSeries= goodsSeriesRepository.findOne(goodsSeries.getId());
        oldGoodsSeries.setIsUsed(goodsSeries.getIsUsed());
        goodsSeriesRepository.save(oldGoodsSeries);
    }


    public void delete(Long id) {
        goodsSeriesRepository.delete(id);
    }
}
