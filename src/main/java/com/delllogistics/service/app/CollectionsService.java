package com.delllogistics.service.app;

import com.delllogistics.entity.app.Collections;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.user.User;
import com.delllogistics.repository.app.CollectionsRepository;
import com.delllogistics.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * 收藏Service.<br/>
 * User: jiajie<br/>
 * Date: 09/12/2017<br/>
 * Time: 10:42 PM<br/>
 */
@Service
public class CollectionsService {

    private final CollectionsRepository collectionsRepository;

    private final UserRepository userRepository;

    @Autowired
    public CollectionsService(CollectionsRepository collectionsRepository, UserRepository userRepository) {
        this.collectionsRepository = collectionsRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addCollection(User user, Goods goods) {
        Collections collections = collectionsRepository.findByUserAndIsDeletedAndGoods(user, false, goods);
        if (ObjectUtils.isEmpty(collections)) {
            collections = new Collections();
        }
        user=userRepository.findOne(user.getId());
        collections.setGoods(goods);
        collections.setUser(user);
        collections.setCompany(user.getCompany());
        collectionsRepository.save(collections);
    }

    public Collections findCollectionByGoodsId(User user, Goods goods) {

        return collectionsRepository.findByUserAndIsDeletedAndGoods(user, false, goods);

    }

    public int findCollectionsCountByGoods(User user, Goods goods) {
        return collectionsRepository.countByUserAndGoodsAndIsDeleted(user, goods, false);
    }

    public Page<Collections> findCollections(int page, int size, User user, Long companyId) {
        return collectionsRepository.findAllByUserAndCompany_idAndIsDeleted(user, companyId,false, new PageRequest(page, size));
    }

    public int findCollectionsCountByUser(User user,Long companyId) {
        return collectionsRepository.countByUser_idAndCompany_idAndIsDeleted(user.getId(),companyId, false);
    }

    public void deleteCollection(User user, Goods goods) {
        Collections collections = collectionsRepository.findByUserAndIsDeletedAndGoods(user, false, goods);
        if (!ObjectUtils.isEmpty(collections)) {
            collectionsRepository.delete(collections.getId());
        }
    }
}
