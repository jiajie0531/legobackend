package com.delllogistics.service.finance;

import com.delllogistics.dto.finance.FinanceDepositApplyLogSearch;
import com.delllogistics.entity.Finance.FinanceDepositApplyLog;
import com.delllogistics.repository.finance.FinanceDepositApplyLogRepository;
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
public class FinanceDepositApplyLogService {

    private final FinanceDepositApplyLogRepository financeDepositApplyLogRepository;

    public FinanceDepositApplyLogService(FinanceDepositApplyLogRepository financeDepositApplyLogRepository) {
        this.financeDepositApplyLogRepository = financeDepositApplyLogRepository;
    }


    public Page<FinanceDepositApplyLog> findAll(FinanceDepositApplyLogSearch financeDepositApplyLogSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(financeDepositApplyLogSearch.getPage(), financeDepositApplyLogSearch.getSize(), sort);
        Specification<FinanceDepositApplyLog> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(financeDepositApplyLogSearch.getFinanceDepositApply())&& !StringUtils.isEmpty(financeDepositApplyLogSearch.getFinanceDepositApply().getId())) {
                predicates.add(criteriaBuilder.equal(root.get("financeDepositApply").get("id"), financeDepositApplyLogSearch.getFinanceDepositApply().getId()));
            }
            if (!StringUtils.isEmpty(financeDepositApplyLogSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("financeDepositApply").get("company").get("id"), financeDepositApplyLogSearch.getCompanyId()));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return financeDepositApplyLogRepository.findAll(specification, pageable);
    }


}
