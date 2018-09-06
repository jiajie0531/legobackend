package com.delllogistics.service.goods;

import com.delllogistics.dto.goods.GoodsParamValueSearch;
import com.delllogistics.entity.goods.GoodsParam;
import com.delllogistics.entity.goods.GoodsParamValue;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.goods.GoodsParamRepository;
import com.delllogistics.repository.goods.GoodsParamValueRepository;
import com.delllogistics.util.EntityConvertUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


/**
 *  商品参数
 * Created by calvin  2018/3/19
 */
@Service
public class GoodsParamValueService {
    private  final  GoodsParamRepository goodsParamRepository;
    private  final GoodsParamValueRepository goodsParamValueRepository;

    public GoodsParamValueService(GoodsParamRepository goodsParamRepository, GoodsParamValueRepository goodsParamValueRepository) {
        this.goodsParamRepository = goodsParamRepository;
        this.goodsParamValueRepository = goodsParamValueRepository;
    }


    public Page<GoodsParamValue> findAll(GoodsParamValueSearch goodsParamValueSearch) {
        if(goodsParamValueSearch.getGoodsParamId()==null){
            throw new SystemException(ExceptionCode.INVALID_ERRO, "数据校验错误!");
        }
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(goodsParamValueSearch.getPage(), goodsParamValueSearch.getSize(), sort);
        Specification<GoodsParamValue> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("GoodsParam").get("id"), goodsParamValueSearch.getGoodsParamId()));
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsParamValueRepository.findAll(specification, pageable);
    }
    public List<GoodsParamValue> findAllByGoodsParamId(Long goodsParamId) {
        return goodsParamValueRepository.findAllByGoodsParam_Id(goodsParamId);
    }



    @Transactional
    public void save(GoodsParamValue goodsParamValue, User user) {
        try {
            GoodsParamValue goodsParamValueNew;
            if(!StringUtils.isEmpty(goodsParamValue.getId())){
                goodsParamValueNew = goodsParamValueRepository.findOne(goodsParamValue.getId());
                if(goodsParamValueNew==null){
                    throw new SystemException(ExceptionCode.INVALID_ERRO, "数据校验错误!");
                }
                goodsParamValueNew.setUpdateUser(user);
                //goodsParamValueNew.setValue(goodsParamValue.getValue());
                EntityConvertUtil.setFieldToEntity(goodsParamValue, goodsParamValueNew,"goodsParam");

            }else {
                if(StringUtils.isEmpty(goodsParamValue.getGoodsParam()) || StringUtils.isEmpty(goodsParamValue.getGoodsParam().getId())){
                    throw new SystemException(ExceptionCode.INVALID_ERRO, "数据校验错误!");
                }
                GoodsParam goodsParam = goodsParamRepository.findOne(goodsParamValue.getGoodsParam().getId());
                goodsParamValue.setGoodsParam(goodsParam);
                goodsParamValue.setCreateUser(user);
                goodsParamValueNew = goodsParamValue;
            }
            goodsParamValueRepository.save(goodsParamValueNew);

        } catch (Exception e) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "数据校验错误!");
        }
    }

    @Transactional
    public void delete(GoodsParamValue goodsParamValue,User user) {

        try{
            GoodsParamValue oldGoodsParamValue= goodsParamValueRepository.findOne(goodsParamValue.getId());
            if(oldGoodsParamValue==null){
                throw new SystemException(ExceptionCode.INVALID_ERRO, "数据校验错误!");
            }
            goodsParamValueRepository.delete(oldGoodsParamValue.getId());
        }catch (Exception e) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "数据校验错误!");
        }
    }


}
