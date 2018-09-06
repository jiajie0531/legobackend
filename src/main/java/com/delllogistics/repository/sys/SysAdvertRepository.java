package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.SysAdvert;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by jiajie on 27/10/2017.
 */
@Repository
public interface SysAdvertRepository
        extends PagingAndSortingRepository<SysAdvert, Long>, JpaSpecificationExecutor<SysAdvert> {


}
