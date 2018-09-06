package com.delllogistics.service.app;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.app.UserAddress;
import com.delllogistics.repository.sys.SysAreaRepository;
import com.delllogistics.repository.user.UserAddressRepository;
import com.delllogistics.util.EntityConvertUtil;
import com.delllogistics.util.ResultUtil;
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
 *  用户收货地址
 * Created by calvin  2017/12/9
 */
@Service
public class UserAddressService {
    private final UserAddressRepository userAddressRepository;
    private final SysAreaRepository sysAreaRepository;

    @Autowired
    public UserAddressService(UserAddressRepository userAddressRepository,SysAreaRepository sysAreaRepository) {
        this.userAddressRepository = userAddressRepository;
        this.sysAreaRepository = sysAreaRepository;
    }
    public void deleteAddress(Long id){
        userAddressRepository.delete(id);
    }



    public Result save(UserAddress userAddress, Long areaId , User user){
        SysArea sysArea = sysAreaRepository.findOne(areaId);
        if(sysArea==null){
            return ResultUtil.error(-1, "所在地区不正确！");
        }
        userAddress.setUser(user);
        userAddress.setArea(sysArea);
        if (!StringUtils.isEmpty(userAddress.getId())) {
            //修改
            UserAddress userAddressUP =   userAddressRepository.findOne(userAddress.getId());
            EntityConvertUtil.setFieldToEntity(userAddress,userAddressUP);
            if (userAddressRepository.save(userAddressUP).getId()==null) {
                return ResultUtil.error(-1, "保存失败！");
            }
        }else{
            //添加
            if (userAddressRepository.save(userAddress).getId()==null) {
                return ResultUtil.error(-1, "保存失败！");
            }
        }



        return ResultUtil.success();
    }
    public Result isUsed(Long userId ,Long id){
        int rst  = userAddressRepository.isUsed(userId,id);
        int rstOther  = userAddressRepository.isOtherNoneUsed(userId,id);
        if(rst==1 && rstOther>=0){
            return ResultUtil.success();
        }else{
            return ResultUtil.error(-1,"操作错误！");
        }
    }

    public Page<UserAddress> findAllAddressList(int page, int size, User user) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<UserAddress> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(user !=null){
                predicates.add(criteriaBuilder.in(root.join("user")).value(user));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return userAddressRepository.findAll(specification, pageable);
    }

    public UserAddress findDefaultAddress(User user) {
        return userAddressRepository.findByUserAndIsUsedAndIsDeleted(user,true,false);
    }
}
