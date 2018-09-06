package com.delllogistics.service.sys;

import com.delllogistics.dto.SysAreaDto;
import com.delllogistics.dto.sys.DtoSysArea;
import com.delllogistics.entity.enums.OrderActionType;
import com.delllogistics.entity.enums.OrderMainStatus;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.sys.SysAreaRepository;
import org.apache.commons.collections.list.TreeList;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class SysAreaService {

    private final SysAreaRepository sysAreaRepository;

    private EntityManager entityManager;

    @Autowired
    public SysAreaService(SysAreaRepository sysAreaRepository, EntityManager entityManager) {
        this.sysAreaRepository = sysAreaRepository;
        this.entityManager = entityManager;
    }

    public String save(SysArea sysArea) {
        sysAreaRepository.save(sysArea);
        return sysArea.getId().toString();
    }

    public List findChildListMap(Long parentId, int level) {
        int isUsed = 1;
        Session session = (Session) entityManager.getDelegate();
        String sql = "SELECT  id ,level ,name,parent_id as parentId,sort "
                + "FROM sys_area  "
                + "WHERE parent_id = :parentId  "
                + "AND is_used = :isUsed  "
                + "AND level = :level "
                + "ORDER BY sort ASC ";
        List list;
        list = session.createSQLQuery(sql)
                .setParameter("parentId", parentId)
                .setParameter("level", level)
                .setParameter("isUsed", isUsed)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }




    private List<DtoSysArea> setChildArea(List<DtoSysArea>  currentArea , List<DtoSysArea>  originalArea, int index, Long[] ids,int[] indexArea){
        if(currentArea.size()>0){
            List<DtoSysArea> nextArea = this.findChildByAntdCascader(ids[index - 1], index + 1);
            if(nextArea.size()<1){
                return originalArea;
            }
            for (int i = 0; i < currentArea.size(); i++) {
                Long value = currentArea.get(i).getValue().longValue();
                Long parentValue = ids[index - 1];
                if (value.equals(parentValue)) {
                    indexArea[index-1]=i;//设置当前地址的index
                    switch (index) {
                        case 1:
                            originalArea.get(indexArea[0]).setChildren(nextArea);
                            break;
                        case 2:
                            originalArea.get(indexArea[0]).getChildren()
                                    .get(indexArea[1]).setChildren(nextArea);
                            break;
                        case 3:
                            originalArea.get(indexArea[0])
                                    .getChildren().get(indexArea[1])
                                    .getChildren().get(indexArea[2])
                                    .setChildren(nextArea);
                            break;
                        case 4:
                            originalArea.get(indexArea[0])
                                    .getChildren().get(indexArea[1])
                                    .getChildren().get(indexArea[2])
                                    .getChildren().get(indexArea[3])
                                    .setChildren(nextArea);
                            break;
                        case 5:
                            originalArea.get(indexArea[0])
                                    .getChildren().get(indexArea[1])
                                    .getChildren().get(indexArea[2])
                                    .getChildren().get(indexArea[3])
                                    .getChildren().get(indexArea[4])
                                    .setChildren(nextArea);
                            break;
                        case 6:
                            originalArea.get(indexArea[0])
                                    .getChildren().get(indexArea[1])
                                    .getChildren().get(indexArea[2])
                                    .getChildren().get(indexArea[3])
                                    .getChildren().get(indexArea[4])
                                    .getChildren().get(indexArea[5])
                                    .setChildren(nextArea);
                            break;
                    }
                    index++;
                    this.setChildArea(nextArea,originalArea, index,ids,indexArea);
                }
            }
        }
        return originalArea;
    }

    public List<DtoSysArea> findAllCity() {
        List<DtoSysArea> dtoSysAreas = this.findChildByAntdCascader(0L, 1);
        List<DtoSysArea> returnList = new ArrayList<>();
        for(DtoSysArea sysArea :dtoSysAreas){
            List<DtoSysArea> cityArea = this.findChildByAntdCascader( sysArea.getValue().longValue(), 2);
            sysArea.setChecked(false);
            sysArea.setChildren(cityArea);
            returnList.add(sysArea);
        }
        return dtoSysAreas;
    }



    public List<DtoSysArea> findAllChildByAntdCascader(Long ids[]) {
        List<DtoSysArea> dtoSysAreas = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            if (i == 0) {
                dtoSysAreas = this.findChildByAntdCascader(0L, 1);
            } else {
                int[] indexArea = new int[6];
                dtoSysAreas = this.setChildArea(dtoSysAreas,dtoSysAreas,i,ids,indexArea);
                break;
            }
        }
        return dtoSysAreas;
    }

    public List<DtoSysArea> findChildByAntdCascader(Long parentId, int level) {
        int isUsed = 1;
        Session session = (Session) entityManager.getDelegate();
        String sql = "" +
                "" +
                "SELECT" +
                "  s.id        AS value," +
                "  s.level," +
                "  s.name      AS label," +
                "  s.parent_id AS pid," +
                "  (" +
                "    case" +
                "    when s1.id is NULL" +
                "    then 'true'" +
                "    else 'false'" +
                "    end" +
                "  ) as isLeaf," +
                "  s.sort" +
                " FROM sys_area  s " +
                " LEFT JOIN sys_area s1 ON s.id = s1.parent_id " +
                " WHERE s.is_used =  ? AND s.level =   ?  ";
        if (parentId != 0) {
            sql += " AND s.parent_id =  ? ";
        } else {
            sql += " AND s.parent_id is null ";
        }
        sql += "  GROUP BY s.id,isLeaf ORDER BY s.sort ASC";
        SQLQuery query = session.createSQLQuery(sql);
        query.setInteger(0, isUsed);
        query.setInteger(1, level);
        if (parentId != 0) {
            query.setLong(2, parentId);
        }
        query.addScalar("isLeaf", StandardBasicTypes.BOOLEAN);
        query.addScalar("label", StandardBasicTypes.STRING);
        query.addScalar("value", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("pid", StandardBasicTypes.BIG_INTEGER);
        query.addScalar("level", StandardBasicTypes.INTEGER);
        query.addScalar("sort", StandardBasicTypes.INTEGER);
        query.setResultTransformer(Transformers.aliasToBean(DtoSysArea.class));
        List sysAreas = query.list();
        return sysAreas;
    }


    public Set<SysAreaDto> findChildList(Long parentId, int level) {
        return sysAreaRepository.listSysAreaDto(parentId, true, level);
    }

    public List findParentList() {
        int isUsed = 1;
        int level = 1;
        Session session = (Session) entityManager.getDelegate();
        String sql = "SELECT  id ,level ,name,parent_id as parentId,sort "
                + "FROM sys_area  "
                + "WHERE parent_id IS NULL  "
                + "AND is_used = :isUsed  "
                + "AND level = :level "
                + "ORDER BY sort ASC ";
        List list;
        list = session.createSQLQuery(sql)
                .setParameter("level", level)
                .setParameter("isUsed", isUsed)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }


}

