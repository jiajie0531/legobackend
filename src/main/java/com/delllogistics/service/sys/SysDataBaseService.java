package com.delllogistics.service.sys;


import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Service
public class SysDataBaseService {

    private EntityManager entityManager;

    @Autowired
    public SysDataBaseService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Page findDataBase(String tableSchema, String tableName,boolean isNullCheck,String columnName, int page, int size) {
        List list;
        Long totalSize = 0L;
        try {
            HibernateEntityManager hEntityManager = (HibernateEntityManager) entityManager;
            Session session = hEntityManager.getSession();
            // 这里以后考虑可以做视图
            String sql = "SELECT " +
                    "  C.TABLE_NAME AS tableName," +
                    "  C.COLUMN_NAME AS columnName," +
                    "  C.COLUMN_TYPE AS columnType," +
                    "  IFNULL(C.COLUMN_COMMENT, '')  AS columnComment," +
                    "  IFNULL(U.CONSTRAINT_NAME, '')   AS  constraintName," +
                    "  IFNULL(U.REFERENCED_TABLE_NAME , '')  AS  referencedTableName," +
                    "  IFNULL(U.REFERENCED_COLUMN_NAME, '')   AS  referencedColumnName," +
                    "  IFNULL(T.TABLE_COMMENT, '')  AS tableComment," +
                    "  IFNULL(T.TABLE_ROWS, '')  AS tableRows," +
                    "  IFNULL(T.AUTO_INCREMENT, '')  AS autoIncrement," +
                    "  C.EXTRA as extra," +
                    "  C.IS_NULLABLE  AS isNull," +
                    "  (CASE WHEN C.EXTRA='auto_increment' THEN 'YES'  ELSE 'NO' END)  AS extraStr," +
                    "  (CASE WHEN C.IS_NULLABLE='YES' THEN '' WHEN C.IS_NULLABLE='NO'  THEN 'NOT NULL' END)  AS isNullable," +
                    "  C.COLUMN_DEFAULT  AS columnDefault " +
                    "  FROM INFORMATION_SCHEMA.COLUMNS C ";

            String sqlCount = "SELECT count(*) as count FROM INFORMATION_SCHEMA.COLUMNS C";


            String sqlWhere = "  LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE U" +
                    "  ON U.TABLE_SCHEMA = C.TABLE_SCHEMA AND U.TABLE_NAME = C.TABLE_NAME AND U.COLUMN_NAME = C.COLUMN_NAME   " +
                    "  LEFT JOIN INFORMATION_SCHEMA.TABLES T ON T.TABLE_SCHEMA = C.TABLE_SCHEMA AND T.TABLE_NAME = C.TABLE_NAME " +
                    "  WHERE   C.TABLE_SCHEMA =  '"+tableSchema+"'  ";

            if (!StringUtils.isEmpty(columnName)) {
                sqlWhere += "    AND C.COLUMN_NAME =  '"+columnName+"' ";
            }

            if (!StringUtils.isEmpty(tableName)) {
                sqlWhere += "    AND C.TABLE_NAME =  '"+tableName+"' ";
            }

            if(isNullCheck){

                sqlWhere += "    AND C.COLUMN_COMMENT = '' ";
            }

            String countSql = sqlCount + sqlWhere;
            //查询总记录条数
            SQLQuery sqlQuery = session.createSQLQuery(countSql);
            Object totals_obj = sqlQuery.uniqueResult();
            totalSize = Long.valueOf(totals_obj.toString());

            //根据条件分页查询
            sqlQuery = session.createSQLQuery(sql + sqlWhere);
            sqlQuery.setFirstResult(size * (page));
            sqlQuery.setMaxResults(size);
            sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            list = sqlQuery.list();
            list = setListId(list);

        } catch (Exception e) {
            list = null;
        }
        return new PageImpl<List>(list, new PageRequest(page, size), totalSize);
    }


    public List findTable(String tableSchema, String tableName) {
        List list;
        try {
            HibernateEntityManager hEntityManager = (HibernateEntityManager) entityManager;
            Session session = hEntityManager.getSession();
            String sql = "    SELECT C.TABLE_NAME AS id," +
                    "     C.TABLE_NAME AS name" +
                    "    FROM INFORMATION_SCHEMA.COLUMNS C " +
                    "    WHERE 1 =1  ";
            if (!StringUtils.isEmpty(tableSchema)) {
                sql += "    AND C.TABLE_SCHEMA =  :tableSchema ";
            }
            if (!StringUtils.isEmpty(tableName)) {
                sql += "    AND C.TABLE_NAME =  :tableName ";
            }
            sql += "    GROUP BY C.TABLE_NAME";


            SQLQuery sqlQuery = session.createSQLQuery(sql);
            if (!StringUtils.isEmpty(tableSchema)) {
                sqlQuery.setParameter("tableSchema", tableSchema);
            }
            if (!StringUtils.isEmpty(tableName)) {
                sqlQuery.setParameter("tableName", tableName);
            }

            list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (Exception e) {
            list = null;
        }
        return list;
    }


    public int saveComment(String columnName, String tableName, String columnDefault, String columnType,String isNullable, String extra, String columnComment,boolean tableCommentChecked,String tableComment) {
        int result;
        try {
            HibernateEntityManager hEntityManager = (HibernateEntityManager) entityManager;
            Session session = hEntityManager.getSession();
            //String sql = "ALTER TABLE :tableName MODIFY :columnName :columnType :isNullable :columnDefault :extra  COMMENT '" + columnComment + "'";
            String clDefault="";
            if(!StringUtils.isEmpty(columnDefault) ){
                clDefault=" DEFAULT '"+columnDefault+"' ";
            }
            String sql = "ALTER TABLE  " + tableName + " MODIFY  " + columnName + " " + columnType + " " + isNullable + " " + clDefault + " " + extra +   "     COMMENT '" + columnComment + "'";
            SQLQuery sqlQuery = session.createSQLQuery(sql);
            sqlQuery.executeUpdate();
            if(tableCommentChecked){
                sql = "ALTER TABLE " + tableName + " COMMENT = '" + tableComment + "'";
                sqlQuery = session.createSQLQuery(sql);
                sqlQuery.executeUpdate();
            }
            result = 1;
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }


    private List setListId(List list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                map.put("id", i + 1);
                list.set(i, map);
            }
        }
        return list;
    }


}

