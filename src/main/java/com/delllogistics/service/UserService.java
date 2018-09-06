package com.delllogistics.service;

import com.delllogistics.dto.WechatRegister;
import com.delllogistics.dto.app.AuthResponse;
import com.delllogistics.dto.user.UserSearch;
import com.delllogistics.entity.enums.MembershipPointType;
import com.delllogistics.entity.enums.UserType;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.entity.user.WechatUser;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.exception.ServiceException;
import com.delllogistics.repository.sys.CompanyRepository;
import com.delllogistics.repository.user.UserAccountRepository;
import com.delllogistics.repository.user.UserRepository;
import com.delllogistics.service.user.MembershipPointService;
import com.delllogistics.util.JwtTokenUtil;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    private final WechatService wechatService;

    private final JwtTokenUtil jwtTokenUtil;

    private final MembershipPointService membershipPointService;

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserService(UserRepository userRepository, CompanyRepository companyRepository, WechatService wechatService, JwtTokenUtil jwtTokenUtil, MembershipPointService membershipPointService, UserAccountRepository userAccountRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.wechatService = wechatService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.membershipPointService = membershipPointService;
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * 分页查询用户
     *
     * @param page 页码
     * @param size 每页显示条数
     * @return Page
     */
    public Page<User> getUsersByPage(int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        if (page >= 1) {
            page--;
        }
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("type").as(Integer.class), UserType.WECHATUSER.value);
        return userRepository.findAll(specification, pageable);
    }

    /**
     * 微信授权用户登录
     *
     * @param appId 公众号appId
     * @param code  授权code
     * @param wechatRegister 额外参数
     * @return AuthResponse 包含token
     */
    @Transactional
    public AuthResponse getUserByWechat(String appId, String code, WechatRegister wechatRegister) {
        WechatUser wechatUser = wechatService.getWechatUser(appId, code);
        if (wechatUser == null) {
            throw new ServiceException(ExceptionCode.GET_WECHATUSER_FAIL, "获取微信用户失败");
        }
        logger.info("微信授权用户登录,appId:{},code:{},nickName:{}", appId, code,wechatUser.getNickName());

        User user = userRepository.findByWechatUser(wechatUser);
        boolean isNewUser=false;
        if (user == null) {
            user = new User();
            isNewUser=true;
            /*
            处理是否有注册门店ID,无门店ID前端重新选择
             */
            if(wechatRegister!=null && wechatRegister.getCompanyId()!=null){
                Company company = companyRepository.findOne(wechatRegister.getCompanyId());
                if(company==null){
                    throw new ServiceException(ExceptionCode.COMPANY_ID_INVALID, "无效的门店ID");
                }
                user.setCompany(company);
            }else{
//                throw new ServiceException(ExceptionCode.COMPANY_ID_INVALID, "门店ID不能为空");
            }
            /*
            处理是否有邀请人,无邀请人或者邀请人ID无效时不做处理
             */
            if( wechatRegister.getUserId()!=null){
                User inviter = userRepository.findOne(wechatRegister.getUserId());
                if(inviter!=null){
                    user.setInviter(inviter);
                    user.setCompany(inviter.getCompany());
                    /*
                    邀请人增加邀请积分奖励
                     */
                    membershipPointService.addPoint(MembershipPointType.PROMOTION,inviter.getCompany(),inviter);
                }
            }
        }



        user.setUsername(wechatUser.getNickName());
        user.setCity(wechatUser.getCity());
        user.setWechatUser(wechatUser);
        user.setPhoto(wechatUser.getHeadImageUrl());
        final String randomKey = jwtTokenUtil.getRandomKey();
        String token = jwtTokenUtil.generateToken(user.getUsername(), randomKey);
        user.setToken(token);
        userRepository.save(user);
        if(isNewUser&&user.getCompany()!=null){

             /*
            新用户会员等级与积分处理
             */
            membershipPointService.addPoint(MembershipPointType.REGISTER,user.getCompany(),user);
        }
        if(user.getCompany()!=null){
            UserAccount userAccount = userAccountRepository.findByUser_idAndCompany_idAndIsDeleted(user.getId(), user.getCompany().getId(), false);
            return new AuthResponse(token, randomKey,userAccount);

        }else{
            return new AuthResponse(token, randomKey);
        }


    }

    public Page<User> getUserByName(UserSearch userSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(userSearch.getPage(), userSearch.getSize(), sort);
        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(userSearch.getUsername())) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + userSearch.getUsername() + "%"));
            }
            if (!StringUtils.isEmpty(userSearch.getUserType())) {
                predicates.add(criteriaBuilder.equal(root.get("type").as(Integer.class), userSearch.getUserType().value));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return userRepository.findAll(specification, pageable);
    }


    /**
     * 根据token查询用户
     *
     * @param user token
     * @return UserAccount
     */
    public UserAccount findUserAcount(User user) {
        user=userRepository.findOne(user.getId());
        Company company = user.getCompany();
        return userAccountRepository.findByUser_idAndCompany_idAndIsDeleted(user.getId(),company.getId(),false);
    }

    @Transactional
    public UserAccount bindCompany(Long companyId, User user) {
        Company company = companyRepository.findOne(companyId);
        if(company==null){
            throw new GeneralException(BizExceptionEnum.ILLEGAL_ACCESS_ERROR);
        }
        user.setCompany(company);
        userRepository.save(user);
        UserAccount userAccount = userAccountRepository.findByUser_idAndCompany_idAndIsDeleted(user.getId(), user.getCompany().getId(), false);
        if(userAccount==null){
            return membershipPointService.addPoint(MembershipPointType.REGISTER,company,user);
        }
        return userAccount;
    }

}
