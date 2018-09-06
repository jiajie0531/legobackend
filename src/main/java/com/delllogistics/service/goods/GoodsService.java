package com.delllogistics.service.goods;

import com.delllogistics.dto.goods.GoodsSearch;
import com.delllogistics.dto.goods.GoodsStockModel;
import com.delllogistics.entity.enums.InventoryOperate;
import com.delllogistics.entity.goods.*;
import com.delllogistics.entity.logistics.LogisticsTemplate;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.goods.*;
import com.delllogistics.repository.logistics.LogisticsTemplateRepository;
import com.delllogistics.repository.sys.CompanyRepository;
import com.delllogistics.repository.sys.SysFileRepository;
import com.delllogistics.sequence.Sequence;
import com.delllogistics.service.InventoryOperationLogService;
import com.delllogistics.util.EntityConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jiajie on 27/10/2017.
 */
@Service
public class GoodsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final CompanyRepository companyRepository;
    private final GoodsBrandRepository goodsBrandRepository;
    private final GoodsRepository goodsRepository;
    private final GoodsSeriesRepository goodsSeriesRepository;
    private final GoodsParamItemRepository goodsParamItemRepository;
    private final LogisticsTemplateRepository logisticsTemplateRepository;
    private final GoodsParamRepository goodsParamRepository;
    private final GoodsTagRepository goodsTagRepository;
    private final Sequence sequence;
    private final SysFileRepository sysFileRepository;
    private final GoodsDetailRepository goodsDetailRepository;
    private final InventoryOperationLogService inventoryOperationLogService;

    @Autowired
    public GoodsService(GoodsRepository goodsRepository, GoodsBrandRepository goodsBrandRepository, GoodsSeriesRepository goodsSeriesRepository, GoodsParamItemRepository goodsParamItemRepository, LogisticsTemplateRepository logisticsTemplateRepository, GoodsParamRepository goodsParamRepository, GoodsTagRepository goodsTagRepository, Sequence sequence, CompanyRepository companyRepository, SysFileRepository sysFileRepository, GoodsDetailRepository goodsDetailRepository, InventoryOperationLogService inventoryOperationLogService) {
        this.goodsRepository = goodsRepository;
        this.goodsBrandRepository = goodsBrandRepository;
        this.goodsSeriesRepository = goodsSeriesRepository;
        this.goodsParamItemRepository = goodsParamItemRepository;
        this.logisticsTemplateRepository = logisticsTemplateRepository;
        this.goodsParamRepository = goodsParamRepository;
        this.goodsTagRepository = goodsTagRepository;
        this.sequence = sequence;
        this.companyRepository = companyRepository;
        this.sysFileRepository = sysFileRepository;
        this.goodsDetailRepository = goodsDetailRepository;
        this.inventoryOperationLogService = inventoryOperationLogService;
    }

    public Page<Goods> findAll(int page, int size, Goods goods) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<Goods> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(goods.getId())) {
                predicates.add(criteriaBuilder.equal(root.get("id"), goods.getId()));
            }
            if (!StringUtils.isEmpty(goods.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + goods.getName() + "%"));
            }
            if (goods.getGoodsTags() != null) {
                predicates.add(criteriaBuilder.in(root.join("goodsTags")).value(goods.getGoodsTags()));
            }
            if (goods.getGoodsSeries() != null) {
                predicates.add(criteriaBuilder.in(root.join("goodsSeries")).value(goods.getGoodsSeries()));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            if (goods.getCompany() != null) {
                predicates.add(criteriaBuilder.equal(root.join("company"), goods.getCompany().getId()));
            }
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsRepository.findAll(specification, pageable);
    }

    public Page<Goods> findAll(GoodsSearch goodsSearch) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(goodsSearch.getPage(), goodsSearch.getSize(), sort);
        Specification<Goods> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(goodsSearch.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + goodsSearch.getName() + "%"));
            }
            if (!StringUtils.isEmpty(goodsSearch.getGoodsSeriesId()) && !goodsSearch.getGoodsSeriesId().equals("ALL")) {
                predicates.add(criteriaBuilder.equal(root.get("goodsSeries").get("id"), goodsSearch.getGoodsSeriesId()));
            }
            if (!StringUtils.isEmpty(goodsSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), goodsSearch.getCompanyId()));
            }
            if (!StringUtils.isEmpty(goodsSearch.getStartTime()) && !StringUtils.isEmpty(goodsSearch.getEndTime())) {
                predicates.add(
                        criteriaBuilder.between(root.get("createTime"), goodsSearch.getStartTime(), goodsSearch.getEndTime())
                );
            }

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsRepository.findAll(specification, pageable);
    }

    public Iterable<Goods> findLogisticTemplateByIds(GoodsSearch goodsSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<Goods> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(goodsSearch.getGoodsIds()) && goodsSearch.getGoodsIds().length > 0) {
                predicates.add(root.get("id").as(Long.class).in((Object[]) goodsSearch.getGoodsIds()));
                predicates.add(criteriaBuilder.equal(root.get("logisticsTemplate").get("isUsed"), 1));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));

            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsRepository.findAll(specification, sort);
    }

    public Goods findById(Long id) {
        return goodsRepository.findOne(id);
    }

    @Transactional
    public void assignGoods(Goods goods, User user) {
        if (StringUtils.isEmpty(goods)
                || StringUtils.isEmpty(goods.getId())
                || StringUtils.isEmpty(goods.getCompany())
                || StringUtils.isEmpty(goods.getCompany().getId())
                ) {
            throw new GeneralException(BizExceptionEnum.CLASS_FORMAT_ERROR);
        }
        Company company = companyRepository.findOne(goods.getCompany().getId());
        if (StringUtils.isEmpty(company)) {
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
        }
        Goods originalGoods = goodsRepository.findOne(goods.getId());
        Goods newGoods = new Goods();
        EntityConvertUtil.setFieldToEntity(originalGoods, newGoods,
                "id",
                "goodsAttributeDetail",
                "goodsDetail",
                "detailPics",
                "goodsTags",
                "code",
                "company",
                "createUser",
                "updateUser",
                "createTime",
                "updateTime",
                "version",
                "goodsParamItems"
        );//
        newGoods.setCode("G" + String.valueOf(sequence.nextId()));//添加商品编号
        newGoods.setCreateUser(user);
        newGoods.setCompany(company);

        //商品详细图
        Set<SysFile> detailPics = new HashSet<>();
        for (SysFile file : originalGoods.getDetailPics()) {
            SysFile newFile = new SysFile();
            EntityConvertUtil.setFieldToEntity(file, newFile,"id");//
            sysFileRepository.save(newFile);
            detailPics.add(newFile);
        }
        newGoods.setDetailPics(detailPics);


        //标签保存
        Set<GoodsTag> goodsTagSet = new HashSet<>();
        for (GoodsTag tag : originalGoods.getGoodsTags()) {
            if (tag != null) {
                goodsTagSet.add(tag);
            }
        }
        newGoods.setGoodsTags(goodsTagSet);

        goodsRepository.save(newGoods);


        for (GoodsDetail goodsDetail : originalGoods.getGoodsDetail()) {
            GoodsDetail newGoodsDetail= new GoodsDetail();
            EntityConvertUtil.setFieldToEntity(goodsDetail, newGoodsDetail,"id","goods");
            newGoodsDetail.setGoods(newGoods);
            newGoodsDetail.setStock(BigDecimal.ZERO);
            newGoodsDetail.setCreateUser(user);
            goodsDetailRepository.save(newGoodsDetail);
        }

        //商品参数保存
        for (GoodsParamItem goodsParamItem : originalGoods.getGoodsParamItems()) {
            GoodsParamItem newGoodsParamItem= new GoodsParamItem();
            EntityConvertUtil.setFieldToEntity(goodsParamItem, newGoodsParamItem,"id","goods");
            newGoodsParamItem.setGoods(newGoods);
            newGoodsParamItem.setCreateUser(user);
            goodsParamItemRepository.save(newGoodsParamItem);

        }
    }

    @Transactional
    public void save(Goods goods, User user) {
        GoodsBrand goodsBrand = goodsBrandRepository.findOne(goods.getGoodsBrand().getId());
        goods.setGoodsBrand(goodsBrand);
        GoodsSeries goodsSeries = goodsSeriesRepository.findOne(goods.getGoodsSeries().getId());
        goods.setGoodsSeries(goodsSeries);
        SysFile goodsPic1 = goods.getGoodsPic();
        if (goodsPic1 != null && !StringUtils.isEmpty(goodsPic1.getId())) {
            SysFile goodsPic = sysFileRepository.findOne(goodsPic1.getId());
            goods.setGoodsPic(goodsPic);
        }

        Company company = companyRepository.findOne(goods.getCompany().getId());


        goods.setCompany(company);
        LogisticsTemplate logisticsTemplate;
        if (!StringUtils.isEmpty(goods.getLogisticsTemplate()) && !StringUtils.isEmpty(goods.getLogisticsTemplate().getId())) {
            logisticsTemplate = logisticsTemplateRepository.findOne(goods.getLogisticsTemplate().getId());
            goods.setLogisticsTemplate(logisticsTemplate);
        }

        //商品详细图
        if (goods.getDetailPics().size() > 0) {
            Set<SysFile> detailPics = new HashSet<>();
            for (SysFile file : goods.getDetailPics()) {
                file = sysFileRepository.findOne(file.getId());
                if (file != null) {
                    detailPics.add(file);
                }
            }
            goods.setDetailPics(detailPics);
        }
        //标签保存
        if (goods.getGoodsTags() != null && goods.getGoodsTags().size() > 0) {
            Set<GoodsTag> goodsTagSet = new HashSet<>();
            for (GoodsTag tag : goods.getGoodsTags()) {
                GoodsTag goodsTag = goodsTagRepository.findOne(tag.getId());
                if (goodsTag != null) {
                    goodsTagSet.add(goodsTag);
                }
            }
            goods.setGoodsTags(goodsTagSet);
        }


        if (!StringUtils.isEmpty(goods.getId())) {
            //修改
            Goods goodsUP = goodsRepository.findOne(goods.getId());
            EntityConvertUtil.setFieldToEntity(goods, goodsUP, "companies", "code", "company", "goodsParamItems", "goodsDetail", "stock");
            goodsUP.setUpdateUser(user);
            goodsRepository.save(goodsUP);

            //商品参数保存
            for (GoodsParamItem goodsParamItem : goods.getGoodsParamItems()) {
                if (goodsParamItem.getGoodsParam() == null || StringUtils.isEmpty(goodsParamItem.getGoodsParam().getId())) {
                    throw new SystemException(ExceptionCode.INVALID_CANCEL_ORDER, "无效的参数!");
                }
                GoodsParamItem goodsParamItemNew = goodsParamItemRepository.findByGoods_IdAndGoodsParam_Id(goodsUP.getId(), goodsParamItem.getGoodsParam().getId());
                if (goodsParamItemNew != null) {
                    EntityConvertUtil.setFieldToEntity(goodsParamItem, goodsParamItemNew, "goods", "goodsParam");
                } else {
                    goodsParamItemNew = goodsParamItem;
                    GoodsParam goodsParam = goodsParamRepository.findOne(goodsParamItem.getGoodsParam().getId());
                    goodsParamItemNew.setGoodsParam(goodsParam);
                    goodsParamItemNew.setGoods(goodsUP);
                }
                goodsParamItemNew.setCreateUser(user);
                goodsParamItemRepository.save(goodsParamItemNew);
            }

        } else {
            goods.setCode("G" + String.valueOf(sequence.nextId()));//添加商品编号
            goods.setCreateUser(user);
            goods.setStock(BigDecimal.ZERO);
            goodsRepository.save(goods);
            for (GoodsDetail goodsDetail : goods.getGoodsDetail()) {
                goodsDetail.setGoods(goods);
                goodsDetail.setStock(BigDecimal.ZERO);
                goodsDetail.setCreateUser(user);
                goodsDetail.setId(null);
                goodsDetailRepository.save(goodsDetail);
            }

            //商品参数保存
            for (GoodsParamItem goodsParamItem : goods.getGoodsParamItems()) {
                if (goodsParamItem.getGoodsParam() == null || StringUtils.isEmpty(goodsParamItem.getGoodsParam().getId())) {
                    throw new SystemException(ExceptionCode.INVALID_CANCEL_ORDER, "无效的参数!");
                }
                GoodsParam goodsParam = goodsParamRepository.findOne(goodsParamItem.getGoodsParam().getId());
                goodsParamItem.setGoodsParam(goodsParam);
                goodsParamItem.setGoods(goods);
                goodsParamItem.setCreateUser(user);
                goodsParamItem.setId(null);
                goodsParamItemRepository.save(goodsParamItem);
            }


        }


    }

    public void delete(Long id) {
        goodsRepository.delete(id);
    }

    public Page<GoodsDetail> findAllGoodsDetail(int page, int size, Goods goods) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<GoodsDetail> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(goods.getName())) {
                predicates.add(criteriaBuilder.like(root.get("goods").get("name"), "%" + goods.getName() + "%"));
            }

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            if (goods.getCompany() != null) {
                predicates.add(criteriaBuilder.equal(root.get("goods").get("company").get("id"), goods.getCompany().getId()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return goodsDetailRepository.findAll(specification, pageable);
    }

    @Transactional
    public void updateStock(GoodsStockModel goodsStockModel, User user) {
        if (goodsStockModel.getStock().compareTo(BigDecimal.ZERO) == -1) {
            /*
            库存不能小于0
             */
            throw new GeneralException(BizExceptionEnum.GOODS_STOCK_MUST_GREATER_ZERO);
        }
        GoodsDetail detail = goodsDetailRepository.findOne(goodsStockModel.getId());
        inventoryOperationLogService.recordGoodsStockLog(detail, goodsStockModel.getStock(), InventoryOperate.UPDATESTOCK, user);
    }

    @Transactional
    public void addStock(GoodsStockModel goodsStockModel, User user) {
        if (goodsStockModel.getAddNums().compareTo(BigDecimal.ZERO) == 1) {
            /*
            上架数量必须大于0
             */
            GoodsDetail detail = goodsDetailRepository.findOne(goodsStockModel.getId());
            inventoryOperationLogService.recordGoodsStockLog(detail, goodsStockModel.getAddNums(), InventoryOperate.PUTAWAY, user);
        } else {
            throw new GeneralException(BizExceptionEnum.ADD_STOCK_MUST_GREATER_ZERO);
        }
    }


}
