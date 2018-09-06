package com.delllogistics.util;

import com.delllogistics.dto.BackendUser;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class EntityConvertUtil {

    /**
     * entity与DTO转换
     *
     * @param dto         数据来源
     * @param entityClass 实体
     * @param <T>         类
     * @return 转换后的类
     */
    public static <T> T convertToEntity(Object dto, Class<T> entityClass) {

        T entity;
        try {
            Field[] fields = dto.getClass().getDeclaredFields();
            entity = cast(Class.forName(entityClass.getName()).newInstance());
            setFieldLoop(dto, entity,fields,true);
            Class<?> superclass = dto.getClass().getSuperclass();
            if (superclass != null) {
                setFieldLoop(dto, entity, superclass.getDeclaredFields(),true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(ExceptionCode.CONVERT_ENTITY_FAIL, "转换Entity异常");
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object obj) {
        return (T) obj;
    }

    private static Method getClassMethod(Class<?> entityClass, Field field) {
        String name = field.getName();
        field.setAccessible(true);
        String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
        try {
            return entityClass.getDeclaredMethod(methodName, field.getType());
        } catch (NoSuchMethodException e) {
            Class superclass = entityClass.getSuperclass();
            if (superclass != null) {
                return getClassMethod(superclass, field);
            }
        }
        return null;
    }

    /**
     * @param source          源对象
     * @param target       目标对象
     */
    public static void setFieldToEntity(Object source, Object target,String... filterProperties) {
        setFieldToEntity(source,target,true,filterProperties);
    }

    public static void setFieldToEntity(Object source, Object target,boolean isAllowNull,String... filterProperties) {
        try {
            Field[] fields = source.getClass().getDeclaredFields();
            setFieldLoop(source, target, fields,isAllowNull,filterProperties);
        } catch (Exception e) {
            throw new SystemException(ExceptionCode.CONVERT_ENTITY_FAIL, "转换Entity异常");
        }
    }

    private static void setFieldLoop(Object dto, Object entity, Field[] fields,boolean isAllowdNull,String... filterProperties) throws IllegalAccessException, InvocationTargetException {
        Class<?> entityClass = entity.getClass();
        List<String> list=null;
        if(filterProperties!=null&&filterProperties.length!=0){
            list=Arrays.asList(filterProperties);
        }
        for (Field field : fields) {
            if(list!=null&&list.contains(field.getName())){
                continue;
            }
            Method method = getClassMethod(entityClass, field);
            if (method != null) {
                Object value = field.get(dto);
                if(!isAllowdNull&&value==null){
                    continue;
                }
                method.invoke(entity, value);
            }
        }
    }

    public static void main(String[] args) {
        User user = new User();
        user.setPassword("123");
        BackendUser backendUser = new BackendUser();
        backendUser.setUsername("abc");
        setFieldToEntity(user,backendUser,false);
        System.out.println(backendUser);
    }

}
