package com.delllogistics.service;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.enums.InventoryCode;
import com.delllogistics.entity.enums.InventoryOperate;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsDetail;
import com.delllogistics.entity.goods.InventoryOperationLog;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.repository.goods.GoodsDetailRepository;
import com.delllogistics.repository.goods.GoodsRepository;
import com.delllogistics.repository.goods.InventoryOperationLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存操作记录表.<br/>
 * User: jiajie<br/>
 * Date: 17/03/2018<br/>
 * Time: 11:48 AM<br/>
 */
@Service
public class InventoryOperationLogService {

    private Logger logger= LoggerFactory.getLogger(getClass());

    private final InventoryOperationLogRepository inventoryOperationLogRepository;

    private final GoodsDetailRepository goodsDetailRepository;

    private final GoodsRepository goodsRepository;


    @Autowired
    public InventoryOperationLogService(InventoryOperationLogRepository inventoryOperationLogRepository, GoodsDetailRepository goodsDetailRepository, GoodsRepository goodsRepository) {
        this.inventoryOperationLogRepository = inventoryOperationLogRepository;
        this.goodsDetailRepository = goodsDetailRepository;
        this.goodsRepository = goodsRepository;
    }


    @Transactional(propagation = Propagation.MANDATORY)
    public void recordGoodsStockLog(GoodsDetail goodsDetail, BigDecimal changeNum, InventoryOperate inventoryOperate,User user) {
        InventoryOperationLog inventoryOperationLog = new InventoryOperationLog();
        inventoryOperationLog.setOperate(inventoryOperate);
        BigDecimal oldStock = goodsDetail.getStock();
        inventoryOperationLog.setInitQuantity(oldStock);
        inventoryOperationLog.setGoods(goodsDetail.getGoods());
        inventoryOperationLog.setGoodsDetail(goodsDetail);
        inventoryOperationLog.setOperateQuantity(changeNum);
        switch (inventoryOperate) {
            case SALES:
                /*
                销售
                 */
                inventoryOperationLog.setQuantity(oldStock.subtract(changeNum));
                inventoryOperationLog.setCode(InventoryCode.SUBTRACTION);
                break;
            case PUTAWAY:
                /*
                商品上架
                 */
                inventoryOperationLog.setQuantity(oldStock.add(changeNum));
                inventoryOperationLog.setCode(InventoryCode.ADDITION);
                break;
            case UNRENT:
                /*
                退组
                 */
                inventoryOperationLog.setQuantity(oldStock.add(changeNum));
                inventoryOperationLog.setCode(InventoryCode.ADDITION);
                break;
            case OFFSHELF:
                /*
                下架
                 */
                inventoryOperationLog.setQuantity(oldStock.subtract(changeNum));
                inventoryOperationLog.setCode(InventoryCode.SUBTRACTION);
                break;
            case UPDATESTOCK:
                /*
                调整库存时，changenum表示调整后的库存数
                 */
                inventoryOperationLog.setQuantity(changeNum);
                if (changeNum.compareTo(oldStock) == 1) {
                    inventoryOperationLog.setCode(InventoryCode.ADDITION);
                    inventoryOperationLog.setOperateQuantity(changeNum.subtract(oldStock));
                } else if (changeNum.compareTo(oldStock) == -1) {
                    inventoryOperationLog.setCode(InventoryCode.SUBTRACTION);
                    inventoryOperationLog.setOperateQuantity(oldStock.subtract(changeNum));
                } else {
                    /*
                    库存无变化时，直接返回
                     */
                    logger.warn("调整后的库存无变化,调整goodsDetailID:{},库存为:{}",goodsDetail.getId(),changeNum);
                    return;
                }
                break;
            case CANCEL_ORDER:
                /*
                主动取消订单
                 */
                inventoryOperationLog.setQuantity(oldStock.add(changeNum));
                inventoryOperationLog.setCode(InventoryCode.ADDITION);
                break;
            case TIMEOUT_ORDER:
                /*
                超时释放订单
                 */
                inventoryOperationLog.setQuantity(oldStock.add(changeNum));
                inventoryOperationLog.setCode(InventoryCode.ADDITION);
            default:
                break;

        }
        BigDecimal newStock = inventoryOperationLog.getQuantity();
        if ((newStock.compareTo(new BigDecimal(0))) == -1) {
            logger.warn("goodsDetailID:{},库存不足，当前库存:{},变化数量:{}",goodsDetail.getId(),oldStock,changeNum);
            throw new GeneralException(BizExceptionEnum.GOODS_STOCK_NOT_ENOUGH);
        }
        goodsDetail.setStock(newStock);
        /*
        保存库存日志并更新库存
         */
        inventoryOperationLog.setCreateUser(user);
        inventoryOperationLog.setUpdateUser(user);
        inventoryOperationLogRepository.save(inventoryOperationLog);
        goodsDetailRepository.save(goodsDetail);

        //更新商品总库存
        BigDecimal goodsStock =  goodsDetailRepository.getSumStockByGoodsId(goodsDetail.getGoods().getId());
        Goods goods = goodsDetail.getGoods();
        goods.setStock(goodsStock);
        goodsRepository.save(goods);


    }

    public Page<InventoryOperationLog> findAllStockLog(BaseSearchModel baseSearchModel, GoodsDetail goodsDetail) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(baseSearchModel.getPage(), baseSearchModel.getSize(), sort);
        Specification<InventoryOperationLog> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            if (goodsDetail != null) {
                predicates.add(criteriaBuilder.equal(root.join("goodsDetail"), goodsDetail.getId()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return inventoryOperationLogRepository.findAll(specification, pageable);
    }

}
