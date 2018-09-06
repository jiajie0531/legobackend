package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysResourceRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysResourceRoleRepository extends PagingAndSortingRepository<SysResourceRole,Long> , JpaSpecificationExecutor<SysResourceRole> {
    SysResourceRole findByNameAndCompanyAndIsDeleted(String name, Company company, boolean isDeleted);
}
