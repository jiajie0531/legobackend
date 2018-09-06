package com.delllogistics.service.app;

import com.delllogistics.dto.app.AppSelect;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 快递管理
 * Created by calvin  2017/12/18
 */
@Service
public class AppLogisticsExpressService {

    private EntityManager entityManager;

    public AppLogisticsExpressService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List findAllSelect(Long companyId) {
        Session session = (Session) entityManager.getDelegate();
        String sql = " SELECT lg.id as value,se.name as label " +
                " FROM logistics_express lg " +
                " LEFT JOIN sys_express se ON lg.sys_express_id=se.id" +
                " WHERE lg.company_id = ? and lg.is_used=1 and lg.is_deleted=0";
        SQLQuery query = session.createSQLQuery(sql);
        query.setLong(0, companyId);
        query.addScalar("value", StandardBasicTypes.STRING);
        query.addScalar("label", StandardBasicTypes.STRING);
        query.setResultTransformer(Transformers.aliasToBean(AppSelect.class));
        return query.list();
    }
}
