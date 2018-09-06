package com.delllogistics.service.user;

import com.delllogistics.dto.user.MembershipRankSearch;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.MembershipRank;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.repository.sys.CompanyRepository;
import com.delllogistics.repository.user.MembershipRankRepository;
import com.delllogistics.repository.user.UserAccountRepository;
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

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Service
public class MembershipRankService {

    private final MembershipRankRepository membershipRankRepository;

    private final CompanyRepository companyRepository;

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public MembershipRankService(MembershipRankRepository membershipRankRepository, CompanyRepository companyRepository, UserAccountRepository userAccountRepository) {
        this.membershipRankRepository = membershipRankRepository;
        this.companyRepository = companyRepository;
        this.userAccountRepository = userAccountRepository;
    }


    public Page<MembershipRank> findAll(MembershipRankSearch membershipRankSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(membershipRankSearch.getPage(), membershipRankSearch.getSize(), sort);
        Specification<MembershipRank> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(membershipRankSearch.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + membershipRankSearch.getName() + "%"));
            }
            if (!StringUtils.isEmpty(membershipRankSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), membershipRankSearch.getCompanyId()));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return membershipRankRepository.findAll(specification, pageable);
    }

    public List<MembershipRank> findAllSelect(Long companyId) {
        Specification<MembershipRank> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            predicates.add(criteriaBuilder.equal(root.get("company").get("id"), companyId));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return membershipRankRepository.findAll(specification, new Sort(Sort.Direction.ASC, "minPoints"));
    }


    /**
     * 获取会员等级信息
     *
     * @param user 用户
     * @return 会员等级信息
     */
    public MembershipRank findMembershipRank(Long companyId,User user) {
        UserAccount userAccount = userAccountRepository.findByUser_idAndCompany_idAndIsDeleted(user.getId(), companyId, false);
        return userAccount.getMembershipRank();
    }


    @Transactional
    public void save(MembershipRank membershipRank, User user) {
        if (StringUtils.isEmpty(membershipRank.getId())) {

            if (StringUtils.isEmpty(membershipRank.getCompany()) || StringUtils.isEmpty(membershipRank.getCompany().getId())) {
                throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
            }
            Company company = companyRepository.findOne(membershipRank.getCompany().getId());
            if (StringUtils.isEmpty(company)) {
                throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
            }
            membershipRank.setCompany(company);
            membershipRank.setCreateUser(user);
            membershipRankRepository.save(membershipRank);
        } else {
            MembershipRank oldMembershipRank = membershipRankRepository.findOne(membershipRank.getId());
            EntityConvertUtil.setFieldToEntity(membershipRank, oldMembershipRank, "company");
            membershipRankRepository.save(oldMembershipRank);

        }

    }

    @Transactional
    public void delete(MembershipRank membershipRank, User user) {
        if (StringUtils.isEmpty(membershipRank.getId())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR);
        }
        MembershipRank oldMembershipRank = membershipRankRepository.findOne(membershipRank.getId());
        oldMembershipRank.setIsDeleted(true);
        oldMembershipRank.setUpdateUser(user);
        membershipRankRepository.save(oldMembershipRank);
    }
}
