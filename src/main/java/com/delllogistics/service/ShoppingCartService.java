package com.delllogistics.service;

import com.delllogistics.entity.*;
import com.delllogistics.entity.app.ShoppingCart;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.app.ShoppingCartRepository;
import com.delllogistics.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车
 *
 * @author JiaJie
 * 2017-11-24 4:53 PM
 **/
@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    private final UserRepository userRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               UserRepository userRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
    }

    public void addToShoppingCart(ShoppingCart shoppingCart, User user) {

            if (shoppingCart.getGoods() != null) {
                user=userRepository.findOne(user.getId());
                ShoppingCart oldShoppingCard = shoppingCartRepository.findByUserAndGoodsAndGoodsDetailAndIsDeleted(
                        user, shoppingCart.getGoods(),shoppingCart.getGoodsDetail(), false);
                if(oldShoppingCard==null){
                    shoppingCart.setUser(user);
                    shoppingCart.setCompany(user.getCompany());
                    shoppingCartRepository.save(shoppingCart);
                }else {
                    BigDecimal shoppingCartMergeQuantity = oldShoppingCard.getQuantity().add(shoppingCart.getQuantity());
                    oldShoppingCard.setQuantity(shoppingCartMergeQuantity);
                    shoppingCartRepository.save(oldShoppingCard);
                }

                } else {
                    throw new SystemException(ExceptionCode.SHOPPINGCART_GOODS_NULL,"加入购物车的商品不能为空");
                }
    }

    public List<ShoppingCart> findShoppingCart(User user, Long companyId) {
        return shoppingCartRepository.findAllByUserAndCompany_idAndIsDeleted(user,companyId, false);
    }

    @Transactional
    public void removeByIds(Long[] ids) {
        for (Long id : ids) {
            shoppingCartRepository.delete(id);
        }
    }

    public void remove(Long id) {
        shoppingCartRepository.delete(id);
    }

    @Transactional
    public void removeAll(Long userId) {
        User user = userRepository.findOne(userId);
        List<Long> ids = shoppingCartRepository.findAllByUserAndCompany_idAndIsDeleted(user,user.getCompany().getId(), false)
                .stream()
                .map(BaseModel::getId)
                .collect(Collectors.toList());
        for (Long id : ids) {
            shoppingCartRepository.delete(id);
        }
    }

    @Transactional
    public void updateShoppingCart(List<ShoppingCart> shoppingCart) {
        for (ShoppingCart cart : shoppingCart) {
            ShoppingCart shoppingCart1 = shoppingCartRepository.findOne(cart.getId());
            shoppingCart1.setQuantity(cart.getQuantity());
            shoppingCartRepository.save(shoppingCart1);
        }
    }

    public int findShoppingCartCount(User user, Goods goods) {
         ShoppingCart shoppingCart=shoppingCartRepository.findByUserAndGoodsAndIsDeleted(user,goods,false);
         if(shoppingCart!=null){
             return shoppingCart.getQuantity().intValue();
         }
         else{
             return 0;
         }
    }
}
