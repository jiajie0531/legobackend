package com.delllogistics.service.sys;

import com.delllogistics.entity.sys.SysResourceRole;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.sys.SysResourceRoleRepository;
import com.delllogistics.util.EntityConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysResourceRoleService {

    private final SysResourceRoleRepository sysResourceRoleRepository;

    @Autowired
    public SysResourceRoleService(SysResourceRoleRepository sysResourceRoleRepository) {
        this.sysResourceRoleRepository = sysResourceRoleRepository;
    }

    public Page<SysResourceRole> findRoles(int page, int size, SysResourceRole sysResourceRole){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<SysResourceRole> specification = getRoleSpecification(sysResourceRole);
        return sysResourceRoleRepository.findAll(specification,pageable);
    }

    private Specification<SysResourceRole> getRoleSpecification(SysResourceRole sysResourceRole) {
        return (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
                predicates.add(criteriaBuilder.equal(root.get("company"), sysResourceRole.getCompany()));
                return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            };
    }

    public Iterable<SysResourceRole> findAll(SysResourceRole sysResourceRole){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<SysResourceRole> specification = getRoleSpecification(sysResourceRole);
        return sysResourceRoleRepository.findAll(specification,sort);
    }

    /**
     * 保存或更新用户角色
     * @param sysResourceRole 角色
     */
    public void submitRole(SysResourceRole sysResourceRole) {
        SysResourceRole oldSysResourceRole = sysResourceRoleRepository.findByNameAndCompanyAndIsDeleted(sysResourceRole.getName(), sysResourceRole.getCompany(), false);
        if (StringUtils.isEmpty(sysResourceRole.getId())) {
            if (oldSysResourceRole != null) {
                throw new SystemException(ExceptionCode.ROLE_NAME_EXISTS, "角色名已存在");
            }
            sysResourceRoleRepository.save(sysResourceRole);
        } else {
            if (oldSysResourceRole != null) {
                if (!oldSysResourceRole.getId().equals(sysResourceRole.getId())) {
                    throw new SystemException(ExceptionCode.COMPANYNAME_EXISTS, "角色名已存在");
                }
            }else {
                /*
                名称修改的情况下，根据ID获取老的数据
                 */
                oldSysResourceRole = sysResourceRoleRepository.findOne(sysResourceRole.getId());
            }
            EntityConvertUtil.setFieldToEntity(sysResourceRole, oldSysResourceRole, "menus", "buttons");
            sysResourceRoleRepository.save(oldSysResourceRole);

        }
    }

    public void delRole(Long id){
        sysResourceRoleRepository.delete(id);
    }

}
