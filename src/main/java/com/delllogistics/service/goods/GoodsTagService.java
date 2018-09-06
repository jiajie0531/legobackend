package com.delllogistics.service.goods;

import com.delllogistics.entity.goods.GoodsTag;
import com.delllogistics.repository.goods.GoodsTagRepository;
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

@Service
public class GoodsTagService {

    protected final GoodsTagRepository goodsTagRepository;

    @Autowired
    public GoodsTagService(GoodsTagRepository goodsTagRepository) {
        this.goodsTagRepository = goodsTagRepository;
    }

    public Page<GoodsTag> findAll(int page, int size, GoodsTag goodsTag) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<GoodsTag> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(goodsTag) && !StringUtils.isEmpty(goodsTag.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + goodsTag.getName() + "%"));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsTagRepository.findAll(specification, pageable);
    }
    public Iterable<GoodsTag> findAllSelect(Long companyId) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<GoodsTag> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsTagRepository.findAll(specification,sort);
    }
    public void save(GoodsTag goodsTag) {
        GoodsTag oldGoodsTag;
        if (StringUtils.isEmpty(goodsTag.getId())) {
            oldGoodsTag = EntityConvertUtil.convertToEntity(goodsTag, GoodsTag.class);
        } else {
            oldGoodsTag = goodsTagRepository.findOne(goodsTag.getId());
            EntityConvertUtil.setFieldToEntity(goodsTag,oldGoodsTag);
        }
        goodsTagRepository.save(oldGoodsTag);
    }
    public void delete(Long id) {
        goodsTagRepository.delete(id);
    }
}
