package com.delllogistics.service.finance;

import com.delllogistics.dto.finance.FinanceDepositLogSearch;
import com.delllogistics.entity.Finance.FinanceDepositLog;
import com.delllogistics.entity.enums.PayChannel;
import com.delllogistics.entity.enums.PayStatus;
import com.delllogistics.entity.enums.PayType;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.finance.FinanceDepositLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinanceDepositLogService {

    private final FinanceDepositLogRepository  financeDepositLogRepository;

    public FinanceDepositLogService(FinanceDepositLogRepository financeDepositLogRepository) {
        this.financeDepositLogRepository = financeDepositLogRepository;
    }


    @Transactional
    public void save(FinanceDepositLog financeDepositLog) {

        try {
            financeDepositLogRepository.save(financeDepositLog);
        } catch (Exception e) {
            throw new SystemException(ExceptionCode.CANNOT_PAY_ORDER, "订单已无法支付!");
        }

    }

    public Page<FinanceDepositLog> findAll(FinanceDepositLogSearch financeDepositLogSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(financeDepositLogSearch.getPage(), financeDepositLogSearch.getSize(), sort);
        Specification<FinanceDepositLog> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> userAccount = root.join("userAccount");// 注意join这里不用这个会关联多个
            Join<Object, Object> joinOrderMain = root.join("orderMain", JoinType.LEFT);//关联订单

            if (!StringUtils.isEmpty(financeDepositLogSearch.getUserId())) {
                predicates.add(criteriaBuilder.equal(userAccount.get("user").get("id"), financeDepositLogSearch.getUserId()));
            }

            if (!StringUtils.isEmpty(financeDepositLogSearch.getOrderMainCode())) {
                predicates.add(criteriaBuilder.equal(joinOrderMain.get("code"), financeDepositLogSearch.getOrderMainCode()));
            }

            if (!StringUtils.isEmpty(financeDepositLogSearch.getStartTime()) && !StringUtils.isEmpty(financeDepositLogSearch.getEndTime())) {
                predicates.add(
                        criteriaBuilder.between(root.get("createTime"), financeDepositLogSearch.getStartTime(), financeDepositLogSearch.getEndTime())
                );
            }
            if (financeDepositLogSearch.getPayChannel() != null && financeDepositLogSearch.getPayChannel() != PayChannel.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("payChannel"), financeDepositLogSearch.getPayChannel()));
            }
            if (financeDepositLogSearch.getPayStatus() != null && financeDepositLogSearch.getPayStatus() != PayStatus.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("payStatus"), financeDepositLogSearch.getPayStatus() ));
            }

            if (!StringUtils.isEmpty(financeDepositLogSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), financeDepositLogSearch.getCompanyId()));
            }
            if (financeDepositLogSearch.getPayType() != null && financeDepositLogSearch.getPayType() != PayType.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("payType"), financeDepositLogSearch.getPayType()));
            }
            if (!StringUtils.isEmpty(financeDepositLogSearch.getTransactionCode())) {
                predicates.add(criteriaBuilder.equal(root.get("transactionCode"), financeDepositLogSearch.getTransactionCode()));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return financeDepositLogRepository.findAll(specification, pageable);
    }


}
