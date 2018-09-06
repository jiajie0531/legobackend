package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.SysResourceButton;
import com.delllogistics.entity.sys.SysResourceMenu;
import com.delllogistics.entity.sys.SysResourceRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/** 按钮权限
 * Created by xzm on 2017/10/19.
 */
@Repository
public interface SysResourceButtonRepository extends CrudRepository<SysResourceButton,Long>{

    Set<SysResourceButton> findByMenuAndRolesIn(SysResourceMenu menu, Set<SysResourceRole> sysResourceRoles);
}
