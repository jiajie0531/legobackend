package com.delllogistics.service.goods;

import com.delllogistics.entity.goods.GoodsBrand;
import com.delllogistics.repository.goods.GoodsBrandRepository;
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
public class GoodsBrandService {

    private final GoodsBrandRepository goodsBrandRepository;

    @Autowired
    public GoodsBrandService(GoodsBrandRepository goodsBrandRepository) {
        this.goodsBrandRepository = goodsBrandRepository;
    }


    public Page<GoodsBrand> findAll(int page, int size, GoodsBrand goodsBrand) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<GoodsBrand> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(goodsBrand.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + goodsBrand.getName() + "%"));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsBrandRepository.findAll(specification, pageable);
    }
    public Iterable<GoodsBrand> findAllSelect() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<GoodsBrand> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsBrandRepository.findAll(specification,sort);
    }

    public void save(GoodsBrand goodsBrand) {
        GoodsBrand oldGoodsBrand;
        if (StringUtils.isEmpty(goodsBrand.getId())) {
            oldGoodsBrand = EntityConvertUtil.convertToEntity(goodsBrand, GoodsBrand.class);
        } else {
            oldGoodsBrand = goodsBrandRepository.findOne(goodsBrand.getId());
            EntityConvertUtil.setFieldToEntity(goodsBrand,oldGoodsBrand);
        }
        goodsBrandRepository.save(oldGoodsBrand);
    }

    public void delete(Long id) {
        goodsBrandRepository.delete(id);
    }
}
