package com.delllogistics.service.sys;

import com.delllogistics.dto.sys.SysExpressSearch;
import com.delllogistics.entity.sys.SysExpress;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.repository.sys.SysExpressRepository;
import com.delllogistics.util.EntityConvertUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 快递公司
 * Created by calvin  2017/12/18
 */
@Service
public class SysExpressService {
    private  final SysExpressRepository sysExpressRepository;

    public SysExpressService(SysExpressRepository sysExpressRepository) {
        this.sysExpressRepository = sysExpressRepository;
    }

    public Page<SysExpress> findAll(SysExpressSearch sysExpressSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(sysExpressSearch.getPage(), sysExpressSearch.getSize(), sort);
        Specification<SysExpress> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(!StringUtils.isEmpty(sysExpressSearch.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + sysExpressSearch.getName() + "%"));
            }

            if (sysExpressSearch.getIsUsed()) {
                predicates.add(criteriaBuilder.equal(root.get("isUsed"), sysExpressSearch.getIsUsed()));
            }


            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return sysExpressRepository.findAll(specification, pageable);
    }

    public Iterable<SysExpress> findAllSelect() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<SysExpress> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return sysExpressRepository.findAll(specification,sort);
    }


    


    @Transactional
    public void save(SysExpress sysExpress,User user){
        SysExpress sysExpressNew;
        if (StringUtils.isEmpty(sysExpress.getId())) {
            sysExpress.setCreateUser(user);
            sysExpressNew = sysExpress;
        } else {
            sysExpressNew = sysExpressRepository.findOne(sysExpress.getId());
            EntityConvertUtil.setFieldToEntity(sysExpress, sysExpressNew);
            sysExpressNew.setUpdateUser(user);
        }
        sysExpressRepository.save(sysExpressNew);

    }

    @Transactional
    public void saveUsed(SysExpress sysExpress, User user) {
        if (StringUtils.isEmpty(sysExpress.getId()) || StringUtils.isEmpty(sysExpress.getIsUsed())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR);
        }
        SysExpress sysExpressNew = sysExpressRepository.findOne(sysExpress.getId());
        sysExpressNew.setIsUsed(sysExpress.getIsUsed());
        sysExpressNew.setUpdateUser(user);
        sysExpressRepository.save(sysExpressNew);
    }

    @Transactional
    public void delete(SysExpress sysExpress, User user) {
        if (StringUtils.isEmpty(sysExpress.getId())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR);
        }
        SysExpress sysExpressNew = sysExpressRepository.findOne(sysExpress.getId());
        sysExpressNew.setIsDeleted(true);
        sysExpressNew.setUpdateUser(user);
        sysExpressRepository.save(sysExpressNew);
    }
}
