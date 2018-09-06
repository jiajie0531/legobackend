package com.delllogistics.service.finance;

import com.delllogistics.dto.finance.FinanceRefundSearch;
import com.delllogistics.entity.Finance.FinanceRefund;
import com.delllogistics.entity.enums.PayChannel;
import com.delllogistics.entity.enums.PayRefundChannel;
import com.delllogistics.entity.enums.PayStatus;
import com.delllogistics.entity.enums.PayType;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.finance.FinanceRefundRepository;
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
public class FinanceRefundService {

    private final FinanceRefundRepository financeRefundRepository;

    public FinanceRefundService(FinanceRefundRepository financeRefundRepository) {
        this.financeRefundRepository = financeRefundRepository;
    }


    @Transactional
    public void save(FinanceRefund financeRefund,User user) {
        try{
            financeRefund.setUpdateUser(user);
            financeRefundRepository.save(financeRefund);
        }catch (Exception e){
            throw new SystemException(ExceptionCode.CANNOT_PAY_ORDER, "订单已无法支付!");
        }

    }


    public FinanceRefund findOne(Long id){
        return financeRefundRepository.findOne(id);
    }

    public Page<FinanceRefund> findAll(FinanceRefundSearch financeRefundSearch ) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(financeRefundSearch.getPage(), financeRefundSearch.getSize(), sort);
        Specification<FinanceRefund> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

           // Join<Object, Object> joinOrderRefund = root.join("orderRefund");// 注意join这里不用这个会关联多个
            if (financeRefundSearch.getPayRefundChannel() != null && financeRefundSearch.getPayRefundChannel() != PayRefundChannel.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("payRefundChannel"), financeRefundSearch.getPayRefundChannel() ));
            }
            if (financeRefundSearch.getPayChannel() != null && financeRefundSearch.getPayChannel() != PayChannel.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("payChannel"), financeRefundSearch.getPayChannel()));
            }
            if (financeRefundSearch.getPayStatus() != null && financeRefundSearch.getPayStatus() != PayStatus.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("payStatus"), financeRefundSearch.getPayStatus() ));
            }

            if (financeRefundSearch.getPayType() != null && financeRefundSearch.getPayType() != PayType.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("payType"), financeRefundSearch.getPayType()));
            }
            if (!StringUtils.isEmpty(financeRefundSearch.getTransactionCode())) {
                predicates.add(criteriaBuilder.equal(root.get("transactionCode"), financeRefundSearch.getTransactionCode()));
            }
            if (!StringUtils.isEmpty(financeRefundSearch.getFinanceRefundCode())) {
                predicates.add(criteriaBuilder.equal(root.get("code"), financeRefundSearch.getFinanceRefundCode()));
            }
            if (!StringUtils.isEmpty(financeRefundSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), financeRefundSearch.getCompanyId()));
            }
            if (!StringUtils.isEmpty(financeRefundSearch.getOrderMainCode() )) {
                predicates.add(criteriaBuilder.equal(root.get("orderRefund").get("orderMain").get("code"), financeRefundSearch.getOrderMainCode()));
            }

            if (!StringUtils.isEmpty(financeRefundSearch.getStartTime()) && !StringUtils.isEmpty(financeRefundSearch.getEndTime())) {
                predicates.add(
                        criteriaBuilder.between(root.get("createTime"), financeRefundSearch.getStartTime(),financeRefundSearch.getEndTime())
                );
            }

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return financeRefundRepository.findAll(specification, pageable);
    }

    
}
