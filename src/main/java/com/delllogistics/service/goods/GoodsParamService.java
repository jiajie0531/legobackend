package com.delllogistics.service.goods;

import com.delllogistics.dto.goods.GoodsParamSearch;
import com.delllogistics.entity.enums.GoodsParamType;
import com.delllogistics.entity.goods.GoodsParam;
import com.delllogistics.entity.sys.SysGoodsCategory;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.sys.CompanyRepository;
import com.delllogistics.repository.goods.GoodsParamRepository;
import com.delllogistics.repository.sys.SysGoodsCategoryRepository;
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
public class GoodsParamService {
    private  final  GoodsParamRepository goodsParamRepository;
    private  final  CompanyRepository companyRepository;
    private  final SysGoodsCategoryRepository sysGoodsCategoryRepository;

    public GoodsParamService(GoodsParamRepository goodsParamRepository, CompanyRepository companyRepository, SysGoodsCategoryRepository sysGoodsCategoryRepository) {
        this.goodsParamRepository = goodsParamRepository;
        this.sysGoodsCategoryRepository = sysGoodsCategoryRepository;
        this.companyRepository = companyRepository;
    }

    public Page<GoodsParam> findAll(GoodsParamSearch goodsParamSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(goodsParamSearch.getPage(), goodsParamSearch.getSize(), sort);
        Specification<GoodsParam> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(goodsParamSearch.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + goodsParamSearch.getName() + "%"));
            }

            if (goodsParamSearch.getIsUsed() != null && goodsParamSearch.getIsUsed()!=-1) {
                predicates.add(criteriaBuilder.equal(root.get("isUsed").as(Integer.class), goodsParamSearch.getIsUsed()));
            }

            if (goodsParamSearch.getSysGoodsCategory() != null && !StringUtils.isEmpty(goodsParamSearch.getSysGoodsCategory().getId())) {
                predicates.add(criteriaBuilder.equal(root.get("sysGoodsCategory").get("id"), goodsParamSearch.getSysGoodsCategory().getId()));
            }

            if (goodsParamSearch.getGoodsParamType() != null && goodsParamSearch.getGoodsParamType() != GoodsParamType.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("goodsParamType"), goodsParamSearch.getGoodsParamType()));
            }

            if (!StringUtils.isEmpty(goodsParamSearch.getStartTime()) && !StringUtils.isEmpty(goodsParamSearch.getEndTime())) {
                predicates.add(
                        criteriaBuilder.between(root.get("createTime"), goodsParamSearch.getStartTime(), goodsParamSearch.getEndTime())
                );
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            predicates.add(criteriaBuilder.equal(root.get("isUsed"), 1));
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsParamRepository.findAll(specification, pageable);
    }
    @Transactional
    public void save(GoodsParam goodsParam, User user) {
        try {
            GoodsParam goodsParamNew;
            if(!StringUtils.isEmpty(goodsParam.getId())){
                goodsParamNew = goodsParamRepository.findOne(goodsParam.getId());
                if(goodsParamNew==null){
                    throw new SystemException(ExceptionCode.INVALID_ERRO, "数据校验错误!");
                }
                EntityConvertUtil.setFieldToEntity(goodsParam, goodsParamNew,false);
                goodsParamNew.setUpdateUser(user);
            }else{
                goodsParamNew = goodsParam;
                SysGoodsCategory sysGoodsCategory = new SysGoodsCategory();
                if (!StringUtils.isEmpty(goodsParamNew.getSysGoodsCategory()) && !StringUtils.isEmpty(goodsParamNew.getSysGoodsCategory().getId())) {
                     sysGoodsCategory =  sysGoodsCategoryRepository.findOne(goodsParamNew.getSysGoodsCategory().getId());
                }
                goodsParamNew.setSysGoodsCategory(sysGoodsCategory);
                goodsParamNew.setCreateUser(user);
            }
            goodsParamRepository.save(goodsParamNew);

        } catch (Exception e) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "数据校验错误!");
        }
    }

    @Transactional
    public void saveUsed(GoodsParam goodsParam,User user) {
        try{
            GoodsParam oldGoodsParam= goodsParamRepository.findOne(goodsParam.getId());
            oldGoodsParam.setIsUsed(goodsParam.getIsUsed());
            oldGoodsParam.setUpdateUser(user);
            goodsParamRepository.save(oldGoodsParam);
        }catch (Exception e) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "数据校验错误!");
        }

    }

    @Transactional
    public void delete(GoodsParam goodsParam,User user) {
        try{
            GoodsParam oldGoodsParam= goodsParamRepository.findOne(goodsParam.getId());
            oldGoodsParam.setIsDeleted(true);
            oldGoodsParam.setUpdateUser(user);
            goodsParamRepository.save(oldGoodsParam);
        }catch (Exception e) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "数据校验错误!");
        }
    }


}
