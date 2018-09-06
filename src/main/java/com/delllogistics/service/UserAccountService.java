package com.delllogistics.service;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.dto.user.UserSearch;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.repository.user.UserAccountRepository;
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
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public Page<UserAccount> getUserAccountByName(BaseSearchModel userSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(userSearch.getPage(), userSearch.getSize(), sort);
        Specification<UserAccount> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(userSearch.getUsername())) {
                predicates.add(criteriaBuilder.like(root.get("user").get("username"), "%" + userSearch.getUsername() + "%"));
            }
            if (!StringUtils.isEmpty(userSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), userSearch.getCompanyId() ));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return userAccountRepository.findAll(specification, pageable);
    }
}
