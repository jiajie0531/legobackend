package com.delllogistics.service.finance;

import com.delllogistics.dto.finance.FinanceDepositApplySearch;
import com.delllogistics.entity.Finance.FinanceDeposit;
import com.delllogistics.entity.Finance.FinanceDepositApply;
import com.delllogistics.entity.Finance.FinanceDepositApplyLog;
import com.delllogistics.entity.Finance.FinanceDepositLog;
import com.delllogistics.entity.enums.ApplyStatus;
import com.delllogistics.entity.enums.PayChannel;
import com.delllogistics.entity.enums.PayStatus;
import com.delllogistics.entity.enums.PayType;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.finance.FinanceDepositApplyLogRepository;
import com.delllogistics.repository.finance.FinanceDepositApplyRepository;
import com.delllogistics.repository.finance.FinanceDepositLogRepository;
import com.delllogistics.repository.finance.FinanceDepositRepository;
import com.delllogistics.repository.sys.CompanyRepository;
import com.delllogistics.repository.user.UserAccountRepository;
import com.delllogistics.sequence.Sequence;
import com.delllogistics.util.EntityConvertUtil;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinanceDepositApplyService {

    private final FinanceDepositApplyRepository financeDepositApplyRepository;
    private final FinanceDepositRepository financeDepositRepository;
    private final FinanceDepositLogRepository financeDepositLogRepository;
    private final CompanyRepository companyRepository;

    private final Sequence sequence;
    private final FinanceDepositApplyLogRepository financeDepositApplyLogRepository;
    private final UserAccountRepository userAccountRepository;

    public FinanceDepositApplyService(FinanceDepositApplyRepository financeDepositApplyRepository, FinanceDepositRepository financeDepositRepository, FinanceDepositLogRepository financeDepositLogRepository, CompanyRepository companyRepository, Sequence sequence, FinanceDepositApplyLogRepository financeDepositApplyLogRepository, UserAccountRepository userAccountRepository) {
        this.financeDepositApplyRepository = financeDepositApplyRepository;
        this.financeDepositRepository = financeDepositRepository;
        this.financeDepositLogRepository = financeDepositLogRepository;
        this.companyRepository = companyRepository;
        this.sequence = sequence;
        this.financeDepositApplyLogRepository = financeDepositApplyLogRepository;
        this.userAccountRepository = userAccountRepository;
    }


    @Transactional
    public void save(FinanceDepositApply financeDepositApply, User actionUser) {
        try {

            if (StringUtils.isEmpty(financeDepositApply.getUserAccount().getId())) {
                throw new SystemException(ExceptionCode.INVALID_ERRO, "无效的参数!");
            }
            UserAccount userAccount = userAccountRepository.findOne(financeDepositApply.getUserAccount().getId());

            if (StringUtils.isEmpty(userAccount)) {
                throw new SystemException(ExceptionCode.INVALID_ERRO, "无效的参数!");
            }
            financeDepositApply.setCode("DA" + String.valueOf(sequence.nextId()));
            if (StringUtils.isEmpty(financeDepositApply.getId())) {
                if (StringUtils.isEmpty(financeDepositApply.getCompany()) || StringUtils.isEmpty(financeDepositApply.getCompany().getId())) {
                    throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
                }
                Company company = companyRepository.findOne(financeDepositApply.getCompany().getId());
                if (StringUtils.isEmpty(company)) {
                    throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
                }
                financeDepositApply.setCompany(company);
                financeDepositApply.setCreateUser(actionUser);
                financeDepositApply.setActionUser(actionUser);
                financeDepositApply.setUserAccount(userAccount);
                financeDepositApply.setApplyStatus(ApplyStatus.APPLY);
            } else {
                FinanceDepositApply financeDepositApplyNew = financeDepositApplyRepository.findOne(financeDepositApply.getId());
                EntityConvertUtil.setFieldToEntity(financeDepositApply, financeDepositApplyNew, "company", "financeDepositApplyLogs");
                financeDepositApplyNew.setUserAccount(userAccount);
                financeDepositApplyNew.setActionUser(actionUser);
                financeDepositApplyNew.setUpdateUser(actionUser);
                financeDepositApplyNew.setApplyStatus(ApplyStatus.APPLY);
                financeDepositApply = financeDepositApplyNew;//赋值最新
            }
            financeDepositApplyRepository.save(financeDepositApply);
            FinanceDepositApplyLog financeDepositApplyLog = new FinanceDepositApplyLog();
            financeDepositApplyLog.setFinanceDepositApply(financeDepositApply);
            financeDepositApplyLog.setAmount(financeDepositApply.getAmount());
            financeDepositApplyLog.setActionUser(actionUser);
            financeDepositApplyLog.setCreateUser(actionUser);
            financeDepositApplyLog.setUserAccount(userAccount);
            financeDepositApplyLog.setApplyStatus(ApplyStatus.APPLY);

            financeDepositApplyLogRepository.save(financeDepositApplyLog);


        } catch (Exception e) {
            throw new SystemException(ExceptionCode.CANNOT_PAY_ORDER, "申请失败!");
        }
    }

    @Transactional
    public void success(FinanceDepositApply financeDepositApply, User actionUser) {
        if (StringUtils.isEmpty(financeDepositApply.getCompany()) || StringUtils.isEmpty(financeDepositApply.getCompany().getId())) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
        }
        Company company = companyRepository.findOne(financeDepositApply.getCompany().getId());
        if (StringUtils.isEmpty(company)) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
        }
        if (StringUtils.isEmpty(financeDepositApply.getId())) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "无效的参数!");
        }
        financeDepositApply = financeDepositApplyRepository.findOne(financeDepositApply.getId());

        if (StringUtils.isEmpty(financeDepositApply)) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "无效的参数!");
        }
        if (!financeDepositApply.getApplyStatus().equals(ApplyStatus.APPLY)) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "申请已被审批!");
        }
        UserAccount userAccount = financeDepositApply.getUserAccount();
        FinanceDeposit financeDeposit = financeDepositRepository.findByUserAccount(userAccount);


        financeDepositApply.setActionUser(actionUser);
        financeDepositApply.setUpdateUser(actionUser);
        financeDepositApply.setApplyStatus(ApplyStatus.SUCCESS);
        financeDepositApplyRepository.save(financeDepositApply);

        FinanceDepositApplyLog financeDepositApplyLog = new FinanceDepositApplyLog();
        financeDepositApplyLog.setFinanceDepositApply(financeDepositApply);
        financeDepositApplyLog.setAmount(financeDepositApply.getAmount());
        financeDepositApplyLog.setActionUser(actionUser);
        financeDepositApplyLog.setCreateUser(actionUser);
        financeDepositApplyLog.setUserAccount(userAccount);
        financeDepositApplyLog.setApplyStatus(ApplyStatus.SUCCESS);
        financeDepositApplyLogRepository.save(financeDepositApplyLog);


        BigDecimal usableAmount = BigDecimal.ZERO;//可用金额
        //预存款操作 开始
        if (StringUtils.isEmpty(financeDeposit)) {
            financeDeposit = new FinanceDeposit();
            financeDeposit.setUserAccount(financeDepositApply.getUserAccount());
            financeDeposit.setFrozenAmount(BigDecimal.ZERO);
            financeDeposit.setCreateUser(actionUser);
            financeDeposit.setUsableAmount(financeDepositApply.getAmount());//首次充值
        } else {
            financeDeposit.setUpdateUser(actionUser);
            usableAmount = financeDeposit.getUsableAmount();//可用金额初始化
            financeDeposit.setUsableAmount(usableAmount.add(financeDepositApply.getAmount()));//充值
        }
        financeDepositRepository.save(financeDeposit);
        //预存款操作 结束
        userAccountRepository.save(userAccount);

        //保存预存款交易记录 开始
        FinanceDepositLog financeDepositLog = new FinanceDepositLog();
        financeDepositLog.setCode("DL" + String.valueOf(sequence.nextId()));
        financeDepositLog.setUserAccount(userAccount);
        financeDepositLog.setPayStatus(PayStatus.SUCCESS);//交易状态
        financeDepositLog.setPayType(PayType.RECHARGEDEPOSIT);//交易类型
        financeDepositLog.setCreateUser(actionUser);
        financeDepositLog.setCompany(company);
        financeDepositLog.setPayChannel(PayChannel.OFFLINE);//交易渠道
        financeDepositLog.setTransactionCode(financeDepositApply.getCode());
        financeDepositLog.setAmount(financeDepositApply.getAmount());//交易金额
        financeDepositLog.setBalance(usableAmount);//可用金额
        financeDepositLogRepository.save(financeDepositLog);
        //保存预存款交易记录结束


    }

    @Transactional
    public void failed(FinanceDepositApply financeDepositApply, User actionUser) {
        if (StringUtils.isEmpty(financeDepositApply.getId())) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "无效的参数!");
        }
        financeDepositApply = financeDepositApplyRepository.findOne(financeDepositApply.getId());

        if (StringUtils.isEmpty(financeDepositApply)) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "无效的参数!");
        }
        if (!financeDepositApply.getApplyStatus().equals(ApplyStatus.APPLY)) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "申请无法驳回!");
        }
        financeDepositApply.setActionUser(actionUser);
        financeDepositApply.setApplyStatus(ApplyStatus.REJECT);
        financeDepositApply.setUpdateUser(actionUser);
        financeDepositApplyRepository.save(financeDepositApply);

        FinanceDepositApplyLog financeDepositApplyLog = new FinanceDepositApplyLog();
        financeDepositApplyLog.setAmount(financeDepositApply.getAmount());
        financeDepositApplyLog.setActionUser(actionUser);
        financeDepositApplyLog.setFinanceDepositApply(financeDepositApply);
        financeDepositApplyLog.setUserAccount(financeDepositApply.getUserAccount());
        financeDepositApplyLog.setCreateUser(actionUser);
        financeDepositApplyLog.setApplyStatus(ApplyStatus.REJECT);

        financeDepositApplyLogRepository.save(financeDepositApplyLog);

    }

    public Page<FinanceDepositApply> findAll(FinanceDepositApplySearch financeDepositApplySearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(financeDepositApplySearch.getPage(), financeDepositApplySearch.getSize(), sort);
        Specification<FinanceDepositApply> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> userAccount = root.join("userAccount");// 注意join这里不用这个会关联多个

            if (financeDepositApplySearch.getApplyStatus() != null && financeDepositApplySearch.getApplyStatus() != ApplyStatus.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("applyStatus"), financeDepositApplySearch.getApplyStatus()));
            }
            if (!StringUtils.isEmpty(financeDepositApplySearch.getUsername())) {
                predicates.add(criteriaBuilder.equal(userAccount.get("user").get("username"), financeDepositApplySearch.getUsername()));
            }
            if (!StringUtils.isEmpty(financeDepositApplySearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), financeDepositApplySearch.getCompanyId()));
            }
            if (!StringUtils.isEmpty(financeDepositApplySearch.getStartTime()) && !StringUtils.isEmpty(financeDepositApplySearch.getEndTime())) {
                predicates.add(
                        criteriaBuilder.between(root.get("createTime"), financeDepositApplySearch.getStartTime(), financeDepositApplySearch.getEndTime())
                );
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return financeDepositApplyRepository.findAll(specification, pageable);
    }


}
