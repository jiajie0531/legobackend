package com.delllogistics.service.app;

import com.delllogistics.entity.enums.MembershipPointType;
import com.delllogistics.entity.enums.OrderItemEvaluateStatus;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.order.OrderItemEvaluate;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.order.OrderItemRepository;
import com.delllogistics.repository.goods.GoodsRepository;
import com.delllogistics.repository.order.OrderItemEvaluateRepository;
import com.delllogistics.repository.sys.SysFileRepository;
import com.delllogistics.service.user.MembershipPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AppOrderItemEvaluateService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());




    private final OrderItemRepository orderItemRepository;

    private final GoodsRepository goodsRepository;

    private final SysFileRepository sysFileRepository;

    private final OrderItemEvaluateRepository orderItemEvaluateRepository;

    private final MembershipPointService membershipPointService;

    public AppOrderItemEvaluateService(OrderItemRepository orderItemRepository, GoodsRepository goodsRepository, SysFileRepository sysFileRepository, OrderItemEvaluateRepository orderItemEvaluateRepository, MembershipPointService membershipPointService) {
        this.orderItemRepository = orderItemRepository;
        this.goodsRepository = goodsRepository;
        this.sysFileRepository = sysFileRepository;
        this.orderItemEvaluateRepository = orderItemEvaluateRepository;
        this.membershipPointService = membershipPointService;
    }
    @Transactional
    public int submit(OrderItemEvaluate orderItemEvaluate, User user) {
        int rst;
        try {
            Goods goods = goodsRepository.findOne(orderItemEvaluate.getGoods().getId());
            OrderItem orderItem = orderItemRepository.findOne(orderItemEvaluate.getOrderItem().getId());

            if (goods == null || orderItem == null) {
                throw new SystemException(ExceptionCode.INVALID_ERRO, "提交的数据校验后发现有问题");
            }
            //为保证第二次添加晒图图片做更新操作
            OrderItemEvaluate newOrderItemEvaluate = orderItemEvaluateRepository.findByUserAndGoodsAndOrderItem(user, goods, orderItem);
            if (newOrderItemEvaluate != null && newOrderItemEvaluate.getEvaluateStatus().equals(OrderItemEvaluateStatus.COMMENT_FINISHED)) {
                throw new SystemException(ExceptionCode.COMPLETED_ERRO, "操作已完成不要重复提交");
            }

            //订单详细中评价状态改为评价完成
            orderItemEvaluate.setGoods(goods);
            orderItemEvaluate.setOrderItem(orderItem);
            orderItemEvaluate.setUser(user);
            orderItemEvaluate.setEvaluateStatus(OrderItemEvaluateStatus.COMMENT_FINISHED);
            orderItem.setOrderItemEvaluateStatus(OrderItemEvaluateStatus.COMMENT_FINISHED);
            //评论积分更新
            Company company = goods.getCompany();
            membershipPointService.addPoint(MembershipPointType.COMMENT, company,user);

            if (orderItemEvaluate.getDetailPics() != null && orderItemEvaluate.getDetailPics().size() > 0) {
                List<Long> detailPicsList = new ArrayList<>();
                for (SysFile sysFile : orderItemEvaluate.getDetailPics()) {
                    detailPicsList.add(sysFile.getId());
                }
                //保存详细图片
                Set<SysFile> pics = new HashSet<>((Collection<? extends SysFile>) sysFileRepository.findAll(detailPicsList));
                if(pics.size()<1){
                    throw new GeneralException(BizExceptionEnum.MEMBER_DIS_ERROR);
                }

                orderItemEvaluate.setDetailPics(pics);
                //订单详细中晒图状态更新
                orderItem.setOrderItemEvaluateStatus(OrderItemEvaluateStatus.SHARE_FINISHED);
                orderItemEvaluate.setEvaluateStatus(OrderItemEvaluateStatus.SHARE_FINISHED);
                //晒图积分更新
                membershipPointService.addPoint(MembershipPointType.SHAREBUY, company,user);
            }

            //orderItemEvaluate.setDetailPics(null);
            orderItemEvaluate.setCreateUser(user);
            orderItemEvaluateRepository.save(orderItemEvaluate);
            orderItemRepository.save(orderItem);
            rst = 1;
        } catch (Exception e) {
            if (e instanceof SystemException) {
                throw new SystemException(((SystemException) e).getCode(), e.getMessage());
            }
            logger.error("用户 {} 提交订单详细评论  发生错误 //r//n {} //r//n 提交数据 {}", user.getId(), e.toString(), orderItemEvaluate);
            throw new SystemException(ExceptionCode.SYSTEM, "提交订单详细评论失败");
        }
        return rst;
    }


    @Transactional
    public int submitShare(OrderItemEvaluate orderItemEvaluate, User user) {
        int rst;
        try {
            Goods goods = goodsRepository.findOne(orderItemEvaluate.getGoods().getId());
            OrderItem orderItem = orderItemRepository.findOne(orderItemEvaluate.getOrderItem().getId());

            if (goods == null || orderItem == null) {
                throw new SystemException(ExceptionCode.INVALID_ERRO, "提交的数据校验后发现有问题");
            }
            //为保证第二次添加晒图图片做更新操作
            OrderItemEvaluate newOrderItemEvaluate = orderItemEvaluateRepository.findByUserAndGoodsAndOrderItem(user, goods, orderItem);
            if (newOrderItemEvaluate == null) {
                throw new SystemException(ExceptionCode.INVALID_ERRO, "提交的数据校验后发现有问题");

            }

            if (newOrderItemEvaluate.getEvaluateStatus().equals(OrderItemEvaluateStatus.SHARE_FINISHED)) {
                throw new SystemException(ExceptionCode.COMPLETED_ERRO, "操作已完成不要重复提交");
            }

            if (orderItemEvaluate.getDetailPics() != null && orderItemEvaluate.getDetailPics().size() > 0) {
                List<Long> detailPicsList = new ArrayList<>();
                for (SysFile sysFile : orderItemEvaluate.getDetailPics()) {
                    detailPicsList.add(sysFile.getId());
                }
                //保存详细图片
                Set<SysFile> pics = new HashSet<>((Collection<? extends SysFile>) sysFileRepository.findAll(detailPicsList));
                newOrderItemEvaluate.setDetailPics(pics);
                //订单详细中晒图状态更新
                orderItem.setOrderItemEvaluateStatus(OrderItemEvaluateStatus.SHARE_FINISHED);
                newOrderItemEvaluate.setEvaluateStatus(OrderItemEvaluateStatus.SHARE_FINISHED);
            } else {
                throw new SystemException(ExceptionCode.NONEPIC_ERRO, "请选择图片上传!");
            }
            //晒图图片做更新操作
            newOrderItemEvaluate.setUpdateUser(user);
            orderItem.setUpdateUser(user);
            orderItemEvaluateRepository.save(newOrderItemEvaluate);
            orderItemRepository.save(orderItem);
            membershipPointService.addPoint(MembershipPointType.SHAREBUY,goods.getCompany(),user);
            rst = 1;
        } catch (Exception e) {
            if (e instanceof SystemException) {
                throw new SystemException(((SystemException) e).getCode(), e.getMessage());
            }
            logger.error("用户 {} 提交晒图  发生错误 //r//n {} //r//n 提交数据 {}", user.getId(), e.toString(), orderItemEvaluate);
            throw new SystemException(ExceptionCode.SYSTEM, "提交晒图失败");
        }
        return rst;
    }


    public Page<OrderItemEvaluate> findOrderItemEvaluatesByGoods(Long goodsId,  int page, int size){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return orderItemEvaluateRepository.findOrderItemEvaluatesByGoods(goodsRepository.findOne(goodsId), pageable);
    }
}
