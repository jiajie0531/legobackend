package com.delllogistics.service.finance;

import com.delllogistics.dto.finance.FinanceDepositSearch;
import com.delllogistics.entity.Finance.FinanceDeposit;
import com.delllogistics.entity.enums.UserStatus;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.finance.FinanceDepositRepository;
import com.delllogistics.repository.user.UserAccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinanceDepositService {

    private final FinanceDepositRepository financeDepositRepository;
    private final UserAccountRepository userAccountRepository;

    public FinanceDepositService(FinanceDepositRepository financeDepositRepository, UserAccountRepository userAccountRepository) {
        this.financeDepositRepository = financeDepositRepository;
        this.userAccountRepository = userAccountRepository;
    }


    @Transactional
    public void save(FinanceDeposit financeDeposit) {

        try {
            financeDepositRepository.save(financeDeposit);
        } catch (Exception e) {
            throw new SystemException(ExceptionCode.CANNOT_PAY_ORDER, "订单已无法支付!");
        }

    }

    public Page<UserAccount> findAll(FinanceDepositSearch financeDepositSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(financeDepositSearch.getPage(), financeDepositSearch.getSize(), sort);
        Specification<UserAccount> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> user = root.join("user");
            if (financeDepositSearch.getUserStatus() != null && financeDepositSearch.getUserStatus() != UserStatus.ALL) {
                predicates.add(criteriaBuilder.equal(user.get("userStatus"), financeDepositSearch.getUserStatus()));
            }
            if (!StringUtils.isEmpty(financeDepositSearch.getUsername())) {
                predicates.add(criteriaBuilder.equal(user.get("username"), financeDepositSearch.getUsername()));
            }
            if (!StringUtils.isEmpty(financeDepositSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), financeDepositSearch.getCompanyId()));
            }
            if (!StringUtils.isEmpty(financeDepositSearch.getStartTime()) && !StringUtils.isEmpty(financeDepositSearch.getEndTime())) {
                predicates.add(
                        criteriaBuilder.between(root.get("createTime"), financeDepositSearch.getStartTime(), financeDepositSearch.getEndTime())
                );
            }
            //微信
            predicates.add(criteriaBuilder.equal(user.get("type"), 0));
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return userAccountRepository.findAll(specification, pageable);
    }

    public FinanceDeposit findByUserAccountId(Long userId) {
        return financeDepositRepository.findByUserAccount_Id(userId);
    }





}
