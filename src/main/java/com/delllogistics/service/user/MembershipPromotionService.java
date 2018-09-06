package com.delllogistics.service.user;

import com.delllogistics.dto.user.MembershipPromotionLogSearch;
import com.delllogistics.entity.user.MembershipPromotionLog;
import com.delllogistics.repository.user.MembershipPromotionRepository;
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
 * 会员推广返利.<br/>
 * User: jiajie<br/>
 * Date: 11/03/2018<br/>
 * Time: 3:03 PM<br/>
 */
@Service
public class MembershipPromotionService {

    private final MembershipPromotionRepository membershipPromotionRepository;

    @Autowired
    public MembershipPromotionService(MembershipPromotionRepository membershipPromotionRepository) {
        this.membershipPromotionRepository = membershipPromotionRepository;
    }

    public Page<MembershipPromotionLog> findAll(MembershipPromotionLogSearch membershipPromotionLogSearc){
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(membershipPromotionLogSearc.getPage(), membershipPromotionLogSearc.getSize(), sort);
        Specification<MembershipPromotionLog> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(membershipPromotionLogSearc.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), membershipPromotionLogSearc.getCompanyId()));
            }
            if(!StringUtils.isEmpty(membershipPromotionLogSearc.getReferrer())){
                predicates.add(criteriaBuilder.like(root.get("referrer").get("username"), "%" + membershipPromotionLogSearc.getReferrer() + "%"));
            }
            if(!StringUtils.isEmpty(membershipPromotionLogSearc.getRecommended())){
                predicates.add(criteriaBuilder.like(root.get("recommended").get("username"), "%" + membershipPromotionLogSearc.getRecommended() + "%"));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return membershipPromotionRepository.findAll(specification, pageable);
    }
}
