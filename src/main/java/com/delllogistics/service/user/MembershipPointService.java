package com.delllogistics.service.user;

import com.delllogistics.dto.user.MembershipPointAggregate;
import com.delllogistics.dto.user.MembershipPointLogSearch;
import com.delllogistics.dto.user.UserPoints;
import com.delllogistics.entity.enums.MembershipPointType;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.*;
import com.delllogistics.repository.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 会员积分.<br/>
 * User: jiajie<br/>
 * Date: 11/03/2018<br/>
 * Time: 2:58 PM<br/>
 */
@Service
public class MembershipPointService {

    private Logger logger= LoggerFactory.getLogger(getClass());

    private final MembershipPointLogRepository membershipPointLogRepository;

    private final MembershipPointRuleRepository membershipPointRuleRepository;

    private final MembershipRankRepository membershipRankRepository;

    private final UserAccountRepository userAccountRepository;

    private final UserRepository userRepository;

    @Autowired
    public MembershipPointService(MembershipPointLogRepository membershipPointLogRepository, MembershipPointRuleRepository membershipPointRuleRepository, MembershipRankRepository membershipRankRepository, UserAccountRepository userAccountRepository, UserRepository userRepository) {
        this.membershipPointLogRepository = membershipPointLogRepository;
        this.membershipPointRuleRepository = membershipPointRuleRepository;
        this.membershipRankRepository = membershipRankRepository;
        this.userAccountRepository = userAccountRepository;
        this.userRepository = userRepository;
    }

    public Page<MembershipPointLog> findAll(MembershipPointLogSearch membershipPointLogSearch){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(membershipPointLogSearch.getPage(), membershipPointLogSearch.getSize(), sort);
        Specification<MembershipPointLog> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (!StringUtils.isEmpty(membershipPointLogSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), membershipPointLogSearch.getCompanyId()));
            }
            if(!StringUtils.isEmpty(membershipPointLogSearch.getUsername())){
                predicates.add(criteriaBuilder.like(root.join("user").get("username"), "%" + membershipPointLogSearch.getUsername() + "%"));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return membershipPointLogRepository.findAll(specification, pageable);
    }


    @Transactional
    public UserAccount addPoint(MembershipPointType membershipPointType, Company company, User user){

        UserAccount userAccount = userAccountRepository.findByUser_idAndCompany_idAndIsDeleted(user.getId(), company.getId(), false);
        if(userAccount==null){
            userAccount=new UserAccount();
            userAccount.setUser(user);
            userAccount.setCompany(company);
        }
        MembershipPointRule pointRule = membershipPointRuleRepository.findByMembershipPointTypeAndCompanyAndIsDeleted(membershipPointType,company, false);
        if(pointRule!=null){
            BigDecimal point = pointRule.getPoint();

            /*
            更新用户账户的等级积分以及消费积分
             */

            BigDecimal totalPoint = userAccount.getRankPoints().add(point);
            userAccount.setRankPoints(totalPoint);
            userAccount.setPayPoints(userAccount.getPayPoints().add(point));
            MembershipRank rank = membershipRankRepository.findByMinPointsLessThanEqualAndMaxPointsGreaterThanEqualAndCompany_Id(totalPoint.intValue(), totalPoint.intValue(),company.getId());
            userAccount.setMembershipRank(rank);

            /*
            保存积分日志
             */
            user=userRepository.findOne(user.getId());
            MembershipPointLog membershipPointLog = new MembershipPointLog();
            membershipPointLog.setUser(user);
            membershipPointLog.setMembershipPointType(membershipPointType);
            membershipPointLog.setPoints(point);
            membershipPointLog.setCreateUser(user);
            membershipPointLog.setCompany(company);
            membershipPointLogRepository.save(membershipPointLog);

        }else{
            logger.info("积分规则{}未定义",membershipPointType);
            if(userAccount.getMembershipRank()==null){
                MembershipRank membershipRank = initUserMemberShip(BigDecimal.ZERO,company.getId());
                userAccount.setMembershipRank(membershipRank);
            }
        }
        userAccountRepository.save(userAccount);
        return userAccount;
    }

    /**
     * 获取个人积分来源列表
     * @param user  用户
     * @return  个人积分来源列表
     */
    public List<MembershipPointAggregate> findPointAggregateList(User user){
        List<MembershipPointAggregate> membershipPointAggregates = new ArrayList<>();
        user=userRepository.findOne(user.getId());
        Company company = user.getCompany();
        List<UserPoints> points = membershipPointLogRepository.findPoints(user.getId(),company.getId());

        List<MembershipPointRule> membershipPointRuleList = membershipPointRuleRepository.findAllByCompanyAndIsDeleted(company, false);
        for (MembershipPointRule membershipPointRule : membershipPointRuleList) {
            MembershipPointAggregate membershipPointAggregate = new MembershipPointAggregate();
            membershipPointAggregate.setUserId(user.getId());
            MembershipPointType membershipPointType = membershipPointRule.getMembershipPointType();
            membershipPointAggregate.setMembershipPointType(membershipPointType.name());
            membershipPointAggregate.setRule(membershipPointRule.getPoint().toString());
            membershipPointAggregate.setPoints(getTotalPointsByType(points,membershipPointType));
            membershipPointAggregates.add(membershipPointAggregate);
        }
        return membershipPointAggregates;
    }

    private BigDecimal getTotalPointsByType(List<UserPoints> list,MembershipPointType membershipPointType){
        for (UserPoints userPoints : list) {
            if(userPoints.getMembershipPointType().equals(membershipPointType)){
                return userPoints.getPoints();
            }
        }
        return BigDecimal.ZERO;
    }

    private MembershipRank initUserMemberShip(BigDecimal point,Long companyId){
       return membershipRankRepository.findByMinPointsLessThanEqualAndMaxPointsGreaterThanEqualAndCompany_Id(point.intValue(), point.intValue(),companyId);
    }

}
