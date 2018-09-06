package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.Company;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**组织机构
 * Created by xzm on 2017-11-6.
 */
@Repository
public interface CompanyRepository extends PagingAndSortingRepository<Company,Long>,JpaSpecificationExecutor<Company> {
    Set<Company> findByParentIsNullAndIdNotAndIsDeletedAndCreateCompany_id(Long id,boolean isDeleted,Long createCompany_id);

    Set<Company> findByParentIsNullAndIsDeletedAndCreateCompany_id(boolean isDeleted,Long createCompany_id);

    Company findByNameAndCreateCompanyAndIsDeleted(String name,Company company,boolean isDeleted);

    List<Company> findByIsDeletedOrderById(boolean isDeleted);
}
