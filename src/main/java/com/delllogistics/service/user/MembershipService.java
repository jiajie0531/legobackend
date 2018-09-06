package com.delllogistics.service.user;

import com.delllogistics.dto.user.MembershipSearch;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.repository.user.UserAccountRepository;
import com.delllogistics.repository.user.UserRepository;
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
 * 会员管理.<br/>
 * User: jiajie<br/>
 * Date: 10/03/2018<br/>
 * Time: 12:27 PM<br/>
 */
@Service
public class MembershipService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public MembershipService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public Page<UserAccount> findAll(MembershipSearch membershipSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(membershipSearch.getPage(), membershipSearch.getSize(), sort);
        Specification<UserAccount> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(membershipSearch.getUsername())) {
                predicates.add(criteriaBuilder.like(root.get("user").get("username"), "%" + membershipSearch.getUsername() + "%"));
            }
            if (!StringUtils.isEmpty(membershipSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), membershipSearch.getCompanyId()));
            }

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            predicates.add(criteriaBuilder.equal(root.get("user").get("type"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return userAccountRepository.findAll(specification, pageable);
    }
}
