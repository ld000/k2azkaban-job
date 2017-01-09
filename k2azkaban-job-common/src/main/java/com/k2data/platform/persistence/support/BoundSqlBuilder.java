package com.k2data.platform.persistence.support;

import com.k2data.platform.utils.*;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建增删改查SQL的工具类<br>
 * <p>
 * 其中Model类必须要<br>
 * 注解{@link javax.persistence.Table}来说明对应的数据库表<br>
 * 注解{@link Id}来说明对应数据库表的主键
 * </p>
 * 
 * @author lidong 2017-01-03
 */
public abstract class BoundSqlBuilder {
    
    /**
     * 通过Model类名生成批量添加的SQL语句
     * 
     * @param modelClass Model类名
     * @return 批量添加的SQL语句
     */
    public static <T> String buildBatchInsertSql(final Class<T> modelClass) {
        final Class<T> clazz = ClassUtils.getOriginalClass(modelClass);
        
        // Sql语句
        final StringBuilder sql = new StringBuilder("INSERT INTO ").append(ModelUtils.getTableName(clazz)).append("\n  (");
        
        final Field[] declaredFields = clazz.getDeclaredFields();
        
        // SQL添加语句要添加的字段数量
        int count = 0;
        
        for (Field property : declaredFields) {
            // 注释 Transient的成员变量不会对应数据库表中的字段
            if (property.isAnnotationPresent(Transient.class)) {
                continue;
            }
            
            final String propertyName = property.getName();
            
            if (count++ > 0) {
                sql.append(", ");
            }
            
            sql.append(propertyName.toUpperCase());
        }
        
        if (count == 0) {
            return null;
        }
        
        sql.append(")VALUES  ( ?").append(StringUtils.repeat(", ?", count - 1)).append(")");
        
        return sql.toString();
    }
    
    /**
     * 通过Model来创建一个SQL的添加语句，只有非空的成员变量才会添加
     * 
     * @param model 用于添加的Model，对应数据库中表的实体
     * @return 执行添加操作的{@link BoundSql}或{@code null}
     */
    public static <T> BoundSql buildInsertBoundSql(final T model) {
        @SuppressWarnings("unchecked")
        final Class<T> clazz = ClassUtils.getOriginalClass((Class<T>) model.getClass());
        
        // Sql语句
        final StringBuilder sql = new StringBuilder("INSERT INTO ").append(ModelUtils.getTableName(clazz)).append(" (");
        
        // 所有IN的值储存在这里,是有顺序的
        final List<Object> inValues = new ArrayList<>();
        
        // SQL添加语句要添加的字段数量
        int count = 0;
        
        for (Field property : clazz.getDeclaredFields()) {
            // 注释 Transient的成员变量不会对应数据库表中的字段
            if (property.isAnnotationPresent(Transient.class)) {
                continue;
            }
            
            final String propertyName = property.getName();
            
            Object inValue = MethodUtils.readProperty(model, propertyName);
            
            // 只添加非空的字段
            if (ObjectUtils.isEmpty(inValue)) {
                continue;
            }
            
            if (count++ > 0) {
                sql.append(", ");
            }
            
            sql.append(propertyName.toUpperCase());
            
            inValues.add(inValue);
        }
        
        if (count == 0) {
            return null;
        }
        
        sql.append(") VALUES ( ?").append(StringUtils.repeat(", ?", count - 1)).append(")");
        
        BoundSql boundSql = new BoundSql(sql.toString());
        boundSql.setInValue(inValues);
        
        return boundSql;
    }
    
    /**
     * 通过Model类和主键的值来生成删除操作的SQL语句
     * 
     * @param modelClazz Model类，对应数据库中表的实体类
     * @param idValue 主键的值
     * @return 执行删除操作的{@link BoundSql}
     */
    public static <T> BoundSql buildDeleteByIdBoundSql(final Class<T> modelClazz, final Object idValue) {
        return new BoundSql(new StringBuilder("DELETE FROM ").append(ModelUtils.getTableName(modelClazz))
                                      .append(" WHERE ").append(ModelUtils.getPkProperty(modelClazz).getName().toUpperCase())
                                      .append(" = ?").toString(),
                                      idValue);
    }
    
    /**
     * 通过Model类和一列主键的值来生成批量删除操作的SQL语句
     * 
     * @param modelClazz Model类，对应数据库中表的实体类
     * @param idValues 一列主键的值
     * @return 执行批量删除操作的{@link BoundSql}
     */
    public static <T> BoundSql buildBatchDeleteByIdBoundSql(final Class<T> modelClazz, final List<?> idValues) {
        if (idValues.size() == 1) {
            return buildDeleteByIdBoundSql(modelClazz, idValues.get(0));
        }
        
        return new BoundSql(new StringBuilder("DELETE FROM ").append(ModelUtils.getTableName(modelClazz)).append("\n")
                                      .append(" WHERE ").append(ModelUtils.getPkProperty(modelClazz).getName().toUpperCase())
                                      .append(" IN ( ?").append(StringUtils.repeat(", ?", idValues.size() - 1)).append(")").toString(),
                                      idValues.toArray());
    }
    
}
