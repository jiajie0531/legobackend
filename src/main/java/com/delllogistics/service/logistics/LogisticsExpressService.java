package com.delllogistics.service.logistics;

import com.delllogistics.dto.BaseSelect;
import com.delllogistics.dto.logistics.LogisticsExpressSearch;
import com.delllogistics.entity.logistics.LogisticsExpress;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysExpress;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.repository.logistics.LogisticsExpressRepository;
import com.delllogistics.repository.sys.CompanyRepository;
import com.delllogistics.repository.sys.SysExpressRepository;
import com.delllogistics.util.EntityConvertUtil;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 快递管理
 * Created by calvin  2017/12/18
 */
@Service
public class LogisticsExpressService {

    private final LogisticsExpressRepository logisticsExpressRepository;
    private final SysExpressRepository sysExpressRepository;
    private final CompanyRepository companyRepository;
    private EntityManager entityManager;

    public LogisticsExpressService(LogisticsExpressRepository logisticsExpressRepository, SysExpressRepository sysExpressRepository, CompanyRepository companyRepository, EntityManager entityManager) {
        this.logisticsExpressRepository = logisticsExpressRepository;
        this.sysExpressRepository = sysExpressRepository;
        this.companyRepository = companyRepository;
        this.entityManager = entityManager;
    }


    public Page<LogisticsExpress> findAll(LogisticsExpressSearch logisticsExpressSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(logisticsExpressSearch.getPage(), logisticsExpressSearch.getSize(), sort);
        Specification<LogisticsExpress> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(logisticsExpressSearch.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + logisticsExpressSearch.getName() + "%"));
            }
            if (logisticsExpressSearch.getIsUsed()) {
                predicates.add(criteriaBuilder.equal(root.get("isUsed"), logisticsExpressSearch.getIsUsed()));
            }

            if (!StringUtils.isEmpty(logisticsExpressSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), logisticsExpressSearch.getCompanyId()));
            }

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return logisticsExpressRepository.findAll(specification, pageable);
    }


    public List findAllSelect(Long companyId) {
        Session session = (Session) entityManager.getDelegate();
        String sql = " SELECT lg.id,se.name " +
                " FROM logistics_express lg " +
                " LEFT JOIN sys_express se ON lg.sys_express_id=se.id" +
                " WHERE lg.company_id = ? and lg.is_used=1 and lg.is_deleted=0";
        SQLQuery query = session.createSQLQuery(sql);
        query.setLong(0, companyId);
        query.addScalar("id", StandardBasicTypes.STRING);
        query.addScalar("name", StandardBasicTypes.STRING);
        query.setResultTransformer(Transformers.aliasToBean(BaseSelect.class));
        return query.list();
    }

    public void save(LogisticsExpress logisticsExpress, User user) {

        if (StringUtils.isEmpty(logisticsExpress.getCompany()) || StringUtils.isEmpty(logisticsExpress.getCompany().getId())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
        }
        Company company = companyRepository.findOne(logisticsExpress.getCompany().getId());
        if (StringUtils.isEmpty(company)) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
        }
        logisticsExpress.setCompany(company);

        if (StringUtils.isEmpty(logisticsExpress.getSysExpress()) || StringUtils.isEmpty(logisticsExpress.getSysExpress().getId())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_ENTITY);
        }

        SysExpress sysExpress = sysExpressRepository.findOne(logisticsExpress.getSysExpress().getId());
        if (StringUtils.isEmpty(sysExpress)) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_ENTITY);
        }
        logisticsExpress.setSysExpress(sysExpress);


        LogisticsExpress logisticsExpressNew;
        if (StringUtils.isEmpty(logisticsExpress.getId())) {
            logisticsExpress.setCreateUser(user);
            logisticsExpressNew = logisticsExpress;
        } else {
            logisticsExpressNew = logisticsExpressRepository.findOne(logisticsExpress.getId());
            EntityConvertUtil.setFieldToEntity(logisticsExpress, logisticsExpressNew, "company");
            logisticsExpressNew.setUpdateUser(user);
        }
        logisticsExpressRepository.save(logisticsExpressNew);


    }

    @Transactional
    public void saveUsed(LogisticsExpress logisticsExpress, User user) {
        if (StringUtils.isEmpty(logisticsExpress.getId()) || StringUtils.isEmpty(logisticsExpress.getIsUsed())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR);
        }
        LogisticsExpress logisticsExpressNew = logisticsExpressRepository.findOne(logisticsExpress.getId());
        logisticsExpressNew.setIsUsed(logisticsExpress.getIsUsed());
        logisticsExpressNew.setUpdateUser(user);
        logisticsExpressRepository.save(logisticsExpressNew);
    }

    @Transactional
    public void delete(LogisticsExpress logisticsExpress, User user) {
        if (StringUtils.isEmpty(logisticsExpress.getId())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR);
        }
        LogisticsExpress logisticsExpressNew = logisticsExpressRepository.findOne(logisticsExpress.getId());
        logisticsExpressNew.setIsDeleted(true);
        logisticsExpressNew.setUpdateUser(user);
        logisticsExpressRepository.save(logisticsExpressNew);
    }
}
