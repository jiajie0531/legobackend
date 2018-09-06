package com.delllogistics.repository.sys;

import com.delllogistics.dto.SysAreaDto;
import com.delllogistics.entity.sys.SysArea;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SysAreaRepository extends PagingAndSortingRepository<SysArea, Long>, JpaSpecificationExecutor<SysArea> {

/*
    @Query("select new com.delllogistics.dto.SysAreaDto(ara.name,ara.id,ara.sort, cla.id ) FROM SysArea ara LEFT JOIN are.parent cla where cla.id=29 AND ara.isUsed = 1  AND are.level = 2    ORDER BY are.sort DESC")
    Set<SysAreaDto> listSysAreaDto(@Param("parentId") String parentId,   @Param("isUsed")String isUsed, @Param("level") String level);
*/
    //数据格式必须要按照entity 而且不能使用order by
    @Query("select new com.delllogistics.dto.SysAreaDto(area.id,area.name,area.sort, cla.id) from SysArea area LEFT JOIN area.parent cla where cla.id=:parentId AND area.isUsed = :isUsed  AND area.level = :level")
    Set<SysAreaDto> listSysAreaDto(@Param("parentId") Long parentId, @Param("isUsed")Boolean isUsed, @Param("level") int level);

    Set<SysArea> findByParentIsNullAndIsUsedAndLevelOrderBySort(boolean isUsed,Integer level);
}
