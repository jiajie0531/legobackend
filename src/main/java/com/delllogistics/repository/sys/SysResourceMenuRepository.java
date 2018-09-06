package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.SysResourceMenu;
import com.delllogistics.entity.sys.SysResourceRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/** 菜单权限
 * Created by xzm on 2017/10/19.
 */
@Repository
public interface SysResourceMenuRepository extends CrudRepository<SysResourceMenu,Long>{
    Set<SysResourceMenu> findByParentIsNullOrderBySort();

    Set<SysResourceMenu>  findByParentIsNullAndRolesInOrderBySort(Set<SysResourceRole> sysResourceRoles);

    Set<SysResourceMenu> findByParentAndRolesInOrderBySort(SysResourceMenu parent, Set<SysResourceRole> sysResourceRoles);
}
