package com.delllogistics.service.finance;

import com.delllogistics.dto.finance.FinanceReceivableSearch;
import com.delllogistics.entity.Finance.FinanceReceivable;
import com.delllogistics.entity.enums.*;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.finance.FinanceReceivableRepository;
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
public class FinanceReceivableService {

    private final FinanceReceivableRepository financeReceivableRepository;


    public FinanceReceivableService(FinanceReceivableRepository financeReceivableRepository) {
        this.financeReceivableRepository = financeReceivableRepository;
    }


    @Transactional
    public void save(FinanceReceivable financeReceivable) {

        try {
            financeReceivableRepository.save(financeReceivable);
        } catch (Exception e) {
            throw new SystemException(ExceptionCode.CANNOT_PAY_ORDER, "订单已无法支付!");
        }
    }

    public FinanceReceivable findOne(Long id){
        return financeReceivableRepository.findOne(id);
    }

    public Page<FinanceReceivable> findAll(FinanceReceivableSearch financeReceivableSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(financeReceivableSearch.getPage(), financeReceivableSearch.getSize(), sort);
        Specification<FinanceReceivable> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> joinOrderMain = root.join("orderMain");// 注意join这里不用这个会关联多个
            if (financeReceivableSearch.getPayChannel() != null && financeReceivableSearch.getPayChannel() != PayChannel.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("payChannel"), financeReceivableSearch.getPayChannel()));
            }

            if (financeReceivableSearch.getPayType() != null && financeReceivableSearch.getPayType() != PayType.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("payType"), financeReceivableSearch.getPayType()));
            }

            if (financeReceivableSearch.getPayStatus() != null && financeReceivableSearch.getPayStatus() != PayStatus.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("payStatus"), financeReceivableSearch.getPayStatus()));
            }
            if (!StringUtils.isEmpty(financeReceivableSearch.getTransactionCode())) {
                predicates.add(criteriaBuilder.equal(root.get("transactionCode"), financeReceivableSearch.getTransactionCode()));
            }
            if (!StringUtils.isEmpty(financeReceivableSearch.getFinanceReceivableCode())) {
                predicates.add(criteriaBuilder.equal(root.get("code"), financeReceivableSearch.getFinanceReceivableCode()));
            }

            if (!StringUtils.isEmpty(financeReceivableSearch.getOrderMainCode())) {
                predicates.add(criteriaBuilder.equal(joinOrderMain.get("code"), financeReceivableSearch.getOrderMainCode()));
            }

            if (!StringUtils.isEmpty(financeReceivableSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(joinOrderMain.get("company").get("id"), financeReceivableSearch.getCompanyId()));
            }


            if (!StringUtils.isEmpty(financeReceivableSearch.getStartTime()) && !StringUtils.isEmpty(financeReceivableSearch.getEndTime())) {
                predicates.add(
                        criteriaBuilder.between(root.get("createTime"), financeReceivableSearch.getStartTime(), financeReceivableSearch.getEndTime())
                );
            }

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return financeReceivableRepository.findAll(specification, pageable);
    }

}
