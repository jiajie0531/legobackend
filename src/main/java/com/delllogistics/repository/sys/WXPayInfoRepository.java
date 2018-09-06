package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.WXPayInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WXPayInfoRepository extends PagingAndSortingRepository<WXPayInfo, Long>,JpaSpecificationExecutor<WXPayInfo> {

}

