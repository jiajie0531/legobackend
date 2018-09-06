package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.SysExpress;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysExpressRepository extends PagingAndSortingRepository<SysExpress, Long>, JpaSpecificationExecutor<SysExpress> {

}
