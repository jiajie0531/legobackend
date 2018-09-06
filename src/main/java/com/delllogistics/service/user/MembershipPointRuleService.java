package com.delllogistics.service.user;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.user.MembershipPointRuleSearch;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.MembershipPointRule;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.repository.sys.CompanyRepository;
import com.delllogistics.repository.user.MembershipPointRuleRepository;
import com.delllogistics.util.EntityConvertUtil;
import com.delllogistics.util.ResultUtil;
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
public class MembershipPointRuleService {

    private final MembershipPointRuleRepository membershipPointRuleRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public MembershipPointRuleService(MembershipPointRuleRepository membershipPointRuleRepository, CompanyRepository companyRepository) {
        this.membershipPointRuleRepository = membershipPointRuleRepository;
        this.companyRepository = companyRepository;
    }

    public Page<MembershipPointRule> findAll(MembershipPointRuleSearch membershipPointRuleSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(membershipPointRuleSearch.getPage(), membershipPointRuleSearch.getSize(), sort);
        Specification<MembershipPointRule> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(membershipPointRuleSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), membershipPointRuleSearch.getCompanyId()));
            }
            if (!StringUtils.isEmpty(membershipPointRuleSearch.getMembershipPointType())) {
                predicates.add(criteriaBuilder.equal(root.get("membershipPointType"), membershipPointRuleSearch.getMembershipPointType()));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return membershipPointRuleRepository.findAll(specification, pageable);
    }

    public Result save(MembershipPointRule membershipPointRule, User user) {
        Long companyId = membershipPointRule.getCompany().getId();
        Company company = companyRepository.findOne(companyId);
        if (StringUtils.isEmpty(company)) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
        }
        MembershipPointRule oldRole = membershipPointRuleRepository.findByMembershipPointTypeAndCompanyAndIsDeleted(membershipPointRule.getMembershipPointType(), company, false);
        Long id = membershipPointRule.getId();
        if (StringUtils.isEmpty(id)) {
            if (oldRole != null) {
                throw new GeneralException(BizExceptionEnum.POINT_ROLE_EXISTS);
            }

            if (StringUtils.isEmpty(membershipPointRule.getCompany()) || StringUtils.isEmpty(membershipPointRule.getCompany().getId())) {
                throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
            }

            membershipPointRule.setCompany(company);
            membershipPointRule.setCreateUser(user);
            membershipPointRuleRepository.save(membershipPointRule);
        } else {
            MembershipPointRule roleById = membershipPointRuleRepository.findOne(id);
            if (oldRole != null && !oldRole.getId().equals(roleById.getId())) {
                throw new GeneralException(BizExceptionEnum.POINT_ROLE_EXISTS);
            }
            EntityConvertUtil.setFieldToEntity(membershipPointRule, roleById);
            membershipPointRule.setUpdateUser(user);
            membershipPointRuleRepository.save(roleById);
        }

        return ResultUtil.success();
    }

    /**
     * 获取会员积分规则
     *
     * @param user 用户
     * @return 会员积分规则列表
     */
    public List<MembershipPointRule> findMembershipPointRule(User user) {
        return membershipPointRuleRepository.findAllByCompanyAndIsDeleted(user.getCompany(), false);
    }
}
