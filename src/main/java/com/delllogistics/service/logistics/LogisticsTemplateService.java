package com.delllogistics.service.logistics;

import com.delllogistics.dto.logistics.LogisticsTemplateSearch;
import com.delllogistics.entity.logistics.LogisticsTemplate;
import com.delllogistics.entity.logistics.LogisticsTemplateItem;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.repository.logistics.LogisticsTemplateItemRepository;
import com.delllogistics.repository.logistics.LogisticsTemplateRepository;
import com.delllogistics.repository.sys.CompanyRepository;
import com.delllogistics.repository.sys.SysAreaRepository;
import com.delllogistics.util.EntityConvertUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 物流模版
 * Created by calvin  2017/12/18
 */
@Service
public class LogisticsTemplateService {

    private final LogisticsTemplateRepository logisticsTemplateRepository;
    private final LogisticsTemplateItemRepository logisticsTemplateItemRepository;
    private final SysAreaRepository sysAreaRepository;
    private final CompanyRepository companyRepository;

    public LogisticsTemplateService(LogisticsTemplateRepository logisticsTemplateRepository, LogisticsTemplateItemRepository logisticsTemplateItemRepository, SysAreaRepository sysAreaRepository, CompanyRepository companyRepository) {
        this.logisticsTemplateRepository = logisticsTemplateRepository;
        this.logisticsTemplateItemRepository = logisticsTemplateItemRepository;
        this.sysAreaRepository = sysAreaRepository;
        this.companyRepository = companyRepository;
    }
    public Page<LogisticsTemplate> findAll(LogisticsTemplateSearch logisticsTemplateSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(logisticsTemplateSearch.getPage(), logisticsTemplateSearch.getSize(), sort);
        Specification<LogisticsTemplate> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(logisticsTemplateSearch.getName())){
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + logisticsTemplateSearch.getName() + "%"));
            }
            if (logisticsTemplateSearch.getIsUsed()) {
                predicates.add(criteriaBuilder.equal(root.get("isUsed"), logisticsTemplateSearch.getIsUsed()));
            }

            if (!StringUtils.isEmpty(logisticsTemplateSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), logisticsTemplateSearch.getCompanyId()));
            }

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return logisticsTemplateRepository.findAll(specification, pageable);
    }

    public Iterable<LogisticsTemplate> findAllSelect(Long companyId) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<LogisticsTemplate> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(companyId)) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), companyId));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            predicates.add(criteriaBuilder.equal(root.get("isUsed"), 1));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return logisticsTemplateRepository.findAll(specification,sort);
    }

    public LogisticsTemplate findOne(Long templateId) {
        return logisticsTemplateRepository.findOne(templateId);
    }


    @Transactional
    public void save(LogisticsTemplate logisticsTemplate,User user){

        if (StringUtils.isEmpty(logisticsTemplate.getCompany()) || StringUtils.isEmpty(logisticsTemplate.getCompany().getId())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
        }

        Company company = companyRepository.findOne(logisticsTemplate.getCompany().getId());
        if (StringUtils.isEmpty(company)) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
        }

        logisticsTemplate.setCompany(company);

        if (StringUtils.isEmpty(logisticsTemplate.getArea()) || StringUtils.isEmpty(logisticsTemplate.getArea().getId())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_ENTITY);
        }


        SysArea sysArea = sysAreaRepository.findOne(logisticsTemplate.getArea().getId());
        if (StringUtils.isEmpty(sysArea)) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_SYSAREA);
        }

        logisticsTemplate.setArea(sysArea);

        LogisticsTemplate logisticsTemplateNew;
        if (StringUtils.isEmpty(logisticsTemplate.getId())) {
            logisticsTemplate.setCreateUser(user);
            logisticsTemplateNew = logisticsTemplate;
        } else {
            logisticsTemplateItemRepository.deleteByLogisticsTemplate_Id(logisticsTemplate.getId());
            logisticsTemplateNew = logisticsTemplateRepository.findOne(logisticsTemplate.getId());
            EntityConvertUtil.setFieldToEntity(logisticsTemplate, logisticsTemplateNew,  "company","logisticsTemplateItems");
            logisticsTemplateNew.setUpdateUser(user);
        }
        logisticsTemplateRepository.save(logisticsTemplateNew);

        //这里因为包邮会没有明细
        if( logisticsTemplate.getLogisticsTemplateItems()!=null){
            for(LogisticsTemplateItem item : logisticsTemplate.getLogisticsTemplateItems()){
                item.setLogisticsTemplate(logisticsTemplateNew);
                item.setCreateUser(user);
                logisticsTemplateItemRepository.save(item);
            }
        }


    }

    @Transactional
    public void saveUsed(LogisticsTemplate logisticsTemplate, User user) {
        if (StringUtils.isEmpty(logisticsTemplate.getId()) || StringUtils.isEmpty(logisticsTemplate.getIsUsed())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR);
        }
        LogisticsTemplate logisticsTemplateNew = logisticsTemplateRepository.findOne(logisticsTemplate.getId());
        logisticsTemplateNew.setIsUsed(logisticsTemplate.getIsUsed());
        logisticsTemplateNew.setUpdateUser(user);
        logisticsTemplateRepository.save(logisticsTemplateNew);
    }

    @Transactional
    public void delete(LogisticsTemplate logisticsTemplate,  User user) {
        if (StringUtils.isEmpty(logisticsTemplate.getId())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR);
        }
        LogisticsTemplate logisticsTemplateNew = logisticsTemplateRepository.findOne(logisticsTemplate.getId());
        logisticsTemplateNew.setIsDeleted(true);
        logisticsTemplateNew.setUpdateUser(user);
        logisticsTemplateRepository.save(logisticsTemplateNew);
    }
}
