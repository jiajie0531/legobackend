package com.delllogistics.service;

import com.delllogistics.dto.*;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.goods.GoodsRepository;
import com.delllogistics.repository.sys.CompanyRepository;
import com.delllogistics.util.EntityConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * 组织机构
 * Created by xzm on 2017-11-6.
 */
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final GoodsRepository goodsRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, GoodsRepository goodsRepository) {
        this.companyRepository = companyRepository;
        this.goodsRepository = goodsRepository;
    }

    private Page<Company> findCompanys(int page, int size, Company company) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        Specification<Company> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            if (!StringUtils.isEmpty(company.getName())) {
                Predicate p2 = criteriaBuilder.like(root.get("name"), "%" + company.getName() + "%");
                predicates.add(p2);
            }
            if (company.getCreateCompany() != null) {
                predicates.add(criteriaBuilder.equal(root.get("createCompany"), company.getCreateCompany()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return companyRepository.findAll(specification, pageRequest);
    }

    public Page<CompanyInfo> findCompanyInfos(int page, int size, Company company) {
        Page<Company> companys = findCompanys(page, size, company);
        List<CompanyInfo> list = new ArrayList<>();
        for (Company company1 : companys) {
            CompanyInfo companyInfo = EntityConvertUtil.convertToEntity(company1, CompanyInfo.class);
            if (company1.getParent() != null) {
                companyInfo.setParentId(company1.getParent().getId());
            }
            list.add(companyInfo);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return new PageImpl<>(list, pageRequest, companys.getTotalElements());
    }

    public Set<Company> findCompanyTree(CompanyTreeModel companyTreeModel) {
        Set<Company> companies;
        Company company = companyRepository.findOne(companyTreeModel.getCurrentCompanyId());
        if (StringUtils.isEmpty(companyTreeModel.getCurrentId())) {
            companies= companyRepository.findByParentIsNullAndIsDeletedAndCreateCompany_id(false, companyTreeModel.getCurrentCompanyId());
            companies.add(company);
        } else {
            companies = companyRepository.findByParentIsNullAndIdNotAndIsDeletedAndCreateCompany_id(companyTreeModel.getCurrentId(), false, companyTreeModel.getCurrentCompanyId());
            if (companyTreeModel.getCurrentId() .equals( companyTreeModel.getCurrentCompanyId())) {
                companies.add(company);
            }
            checkChildren(companies, companyTreeModel.getCurrentId());
        }


        return companies;
    }

    private boolean checkChildren(Set<Company> companySet, Long currentId) {
        for (Company child : companySet) {
            if (child.getId().equals(currentId)) {
                companySet.remove(child);
                return true;
            }
            if (checkChildren(child.getChildren(), currentId)) {
                return true;
            }
        }
        return false;
    }

    public void submitCompany(Company company) {
        Company oldCompany = companyRepository.findByNameAndCreateCompanyAndIsDeleted(company.getName(), company.getCreateCompany(), false);
        if (StringUtils.isEmpty(company.getId())) {
            if (oldCompany != null) {
                throw new SystemException(ExceptionCode.COMPANYNAME_EXISTS, "机构名已存在");
            }
            companyRepository.save(company);
        } else {
            if (oldCompany != null) {
                if (!oldCompany.getId().equals(company.getId())) {
                    throw new SystemException(ExceptionCode.COMPANYNAME_EXISTS, "机构名已存在");
                }
            } else {
                /*
                名称修改的情况下，根据ID获取老的数据
                 */
                oldCompany = companyRepository.findOne(company.getId());
            }
            EntityConvertUtil.setFieldToEntity(company, oldCompany);
            companyRepository.save(oldCompany);
        }

    }

    public void delCompany(Long id) {
        companyRepository.delete(id);
    }

    /**
     * 查询上级和授权机构创建商品
     *
     * @param createCompanyId 上级机构ID
     * @param grantCompanyId  需要授权的机构ID
     * @return 可以授权的商品和已授权的商品ID集合
     */
    public TransferInfo findGoodsRelationByCompanyId(Long createCompanyId, Long grantCompanyId) {
        if (createCompanyId.equals(grantCompanyId)) {
            throw new SystemException(ExceptionCode.GRANT_COMPANY_ID_SAME, "不允许自己向自己分配商品");
        }
        Company parentCompany = companyRepository.findOne(createCompanyId);
        Company grantCompany = companyRepository.findOne(grantCompanyId);
        Set<Company> children = parentCompany.getChildren();
        if (children != null && !children.contains(grantCompany)) {
            throw new SystemException(ExceptionCode.GRANT_COMPANY_MUST_LESS, "只允许向下属机构分配商品");
        }
        /*
        查询商品创建ID为当前机构以及分配机构的商品
         */
        Set<Long> set = new HashSet<>();
        set.add(createCompanyId);
        set.add(grantCompanyId);
        Set<Goods> goods = goodsRepository.findAllByIsDeletedAndCompany_IdInOrderByName(false, set);

        TransferInfo transferInfo = new TransferInfo();
        if (goods != null && !goods.isEmpty()) {
            Set<BaseTransfer> goodsInfoSet=new LinkedHashSet<>();
            for (Goods good : goods) {
                goodsInfoSet.add(new BaseTransfer(good.getId() + "", good.getName()));
            }
            transferInfo.setMockData(goodsInfoSet);
            /*
            查询分配机构的已分配的商品
             */
//            Set<Goods> targetGoods = grantCompany.getGoods();
//            if (targetGoods != null && !targetGoods.isEmpty()) {
//                Set<String> targetKeys = targetGoods.stream().map(good -> good.getId() + "").collect(Collectors.toSet());
//                transferInfo.setTargetKeys(targetKeys);
//            }
        }
        return transferInfo;
    }

    public void grantGoodsToCompany(Company company) {
        Company oldCompany = companyRepository.findOne(company.getId());
//        oldCompany.setGoods(company.getGoods());
        companyRepository.save(oldCompany);
    }

    public List<PickerModel> getSelectCompanies() {
        List<Company> companies = companyRepository.findByIsDeletedOrderById(false);
        List<PickerModel> pickerModelList=new ArrayList<>();
        for (Company company : companies) {
            PickerModel pickerModel = new PickerModel();
            pickerModel.setLabel(company.getName());
            pickerModel.setValue(company.getId()+"");
            pickerModelList.add(pickerModel);
        }
        return pickerModelList;
    }


    public List<Company> findCompanyList() {
        return companyRepository.findByIsDeletedOrderById(false);
    }
}
