package com.delllogistics.service.app;

import com.delllogistics.entity.app.BrowsingHistory;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.user.User;
import com.delllogistics.repository.app.BrowsingHistoryRepository;
import com.delllogistics.repository.goods.GoodsRepository;
import com.delllogistics.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * 足迹Service.<br/>
 * User: jiajie<br/>
 * Date: 09/12/2017<br/>
 * Time: 10:43 PM<br/>
 */
@Service
public class BrowsingHistoryService {

    private final BrowsingHistoryRepository browsingHistoryRepository;

    private final GoodsRepository goodsRepository;

    private final UserRepository userRepository;

    @Autowired
    public BrowsingHistoryService(BrowsingHistoryRepository browsingHistoryRepository, GoodsRepository goodsRepository, UserRepository userRepository) {
        this.browsingHistoryRepository = browsingHistoryRepository;
        this.goodsRepository = goodsRepository;
        this.userRepository = userRepository;
    }

    public void addBrowsingHistory(User user, long goodsId) {
        Goods goods = goodsRepository.findOne(goodsId);
        user=userRepository.findOne(user.getId());
        BrowsingHistory browsingHistory = browsingHistoryRepository.findByGoodsAndUserAndIsDeleted(goods, user, false);
        if(browsingHistory==null){
            browsingHistory = new BrowsingHistory();
            browsingHistory.setCreateUser(user);
            browsingHistory.setGoods(goods);
            browsingHistory.setUser(user);
            browsingHistory.setCompany(user.getCompany());
            browsingHistory.setCount(1);
        }else{
            browsingHistory.setCount(browsingHistory.getCount()+1);
            browsingHistory.setUpdateUser(user);
        }
        browsingHistoryRepository.save(browsingHistory);
    }

    public void deleteBrowsingHistory(long id) {
        browsingHistoryRepository.delete(id);
    }

    public Page<BrowsingHistory> findBrowsingHistory(int page, int size, User user,Long companyId) {
        return browsingHistoryRepository.findAllByUserAndCompany_idAndIsDeleted(user, companyId,false, new PageRequest(page, size,Sort.Direction.DESC,"updateTime"));
    }

    public int findBrowsingHistoryCountByUser(User user,Long companyId) {
        return browsingHistoryRepository.countByUser_idAndCompany_id(user.getId(),companyId);
    }


}
