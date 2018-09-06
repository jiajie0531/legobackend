package com.delllogistics.service.sys;

import com.delllogistics.dto.sys.SysAdvertSearch;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysAdvert;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.repository.sys.CompanyRepository;
import com.delllogistics.repository.sys.SysAdvertRepository;
import com.delllogistics.repository.sys.SysFileRepository;
import com.delllogistics.util.EntityConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiajie on 27/10/2017.
 */
@Service
public class SysAdvertService {

    private final SysAdvertRepository sysAdvertRepository;
    private final SysFileRepository sysFileRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public SysAdvertService(SysAdvertRepository sysAdvertRepository, SysFileRepository sysFileRepository, CompanyRepository companyRepository) {
        this.sysAdvertRepository = sysAdvertRepository;
        this.sysFileRepository = sysFileRepository;
        this.companyRepository = companyRepository;
    }

    public Page<SysAdvert> findAdvertList (SysAdvertSearch sysAdvertSearch) {
        sysAdvertSearch.setIsUsed(true);
        return this.findAll(sysAdvertSearch);
    }

    public Page<SysAdvert> findAll(SysAdvertSearch sysAdvertSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "sort", "updateTime");
        Pageable pageable = new PageRequest(sysAdvertSearch.getPage(), sysAdvertSearch.getSize(), sort);
        Specification<SysAdvert> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = getPredicates(sysAdvertSearch, root, criteriaBuilder);
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return sysAdvertRepository.findAll(specification, pageable);
    }

    private List<Predicate> getPredicates(SysAdvertSearch sysAdvertSearch, Root<SysAdvert> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(sysAdvertSearch.getName())) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + sysAdvertSearch.getName() + "%"));
        }
        if (sysAdvertSearch.getIsUsed()) {
            predicates.add(criteriaBuilder.equal(root.get("isUsed"), sysAdvertSearch.getIsUsed()));
        }
        if (!StringUtils.isEmpty(sysAdvertSearch.getCompanyId())) {
            predicates.add(criteriaBuilder.equal(root.get("company").get("id"), sysAdvertSearch.getCompanyId()));
        }
        return predicates;
    }

    public void save(SysAdvert sysAdvert, User user) {

        if (StringUtils.isEmpty(sysAdvert.getDescriptionPic()) || StringUtils.isEmpty(sysAdvert.getDescriptionPic().getId())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_SYSFILE);
        }

        SysFile sysFile = sysFileRepository.findOne(sysAdvert.getDescriptionPic().getId());
        if (StringUtils.isEmpty(sysFile)) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_SYSFILE);
        }
        sysAdvert.setDescriptionPic(sysFile);

        SysAdvert newSysAdvert;
        if (StringUtils.isEmpty(sysAdvert.getId())) {
            sysAdvert.setCreateUser(user);
            if (StringUtils.isEmpty(sysAdvert.getCompany()) || StringUtils.isEmpty(sysAdvert.getCompany().getId())) {
                throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
            }
            Company company = companyRepository.findOne(sysAdvert.getCompany().getId());
            if (StringUtils.isEmpty(company)) {
                throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
            }
            sysAdvert.setCompany(company);
            newSysAdvert = sysAdvert;
        } else {
            newSysAdvert = sysAdvertRepository.findOne(sysAdvert.getId());
            EntityConvertUtil.setFieldToEntity(sysAdvert, newSysAdvert, "company");
            newSysAdvert.setUpdateUser(user);
        }
        sysAdvertRepository.save(newSysAdvert);


    }

    public void saveUsed(SysAdvert sysAdvert, User user) {
        if (StringUtils.isEmpty(sysAdvert.getId()) || StringUtils.isEmpty(sysAdvert.getIsUsed())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR);
        }
        SysAdvert oldSysAdvert = sysAdvertRepository.findOne(sysAdvert.getId());
        oldSysAdvert.setIsUsed(sysAdvert.getIsUsed());
        oldSysAdvert.setUpdateUser(user);
        sysAdvertRepository.save(oldSysAdvert);
    }

    @Transactional
    public void delete(SysAdvert sysAdvert, User user) {
        if (StringUtils.isEmpty(sysAdvert.getId())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR);
        }
        SysAdvert oldSysAdvert = sysAdvertRepository.findOne(sysAdvert.getId());
        oldSysAdvert.setIsDeleted(true);
        oldSysAdvert.setUpdateUser(user);
        sysAdvertRepository.save(oldSysAdvert);
    }

}
