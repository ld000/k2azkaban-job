package com.k2data.platform.persistence.provider;

import com.k2data.platform.utils.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.jdbc.SQL;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 通用 insert sql 生成类
 * 
 * @author lidong
 * @param <T>
 */
public class InsertSqlProvider<T> {
    
    /**
     * 生成单条 insert sql 语句, 驼峰命名会转成下划线命名
     * 
     * @param obj
     * @return
     */
    public String insertWithCamelhumpToUnderline(T obj) {
        return genInsertSql(obj, true);
    }
    
    /**
     * 生成单条 insert sql 语句
     * 
     * @param obj
     * @return
     */
    public String insert(T obj) {
        return genInsertSql(obj, false);
    }
    
    /**
     * 生成批量 insert sql 语句, 驼峰命名会转成下划线命名 </br>
     * DAO 方法参数需加 {@code @Param("list")} 注解
     * 
     * @return
     */
    public String batchInsertWithCamelhumpToUnderline(Map<String, Object> para) {
        return genBatchInsertSql(para, true);
    }
    
    /**
     * 生成批量 insert sql 语句 </br>
     * DAO 方法参数需加 {@code @Param("list")} 注解 
     * 
     * @return
     */
    public String batchInsert(Map<String, Object> para) {
        return genBatchInsertSql(para, false);
    }
    
    /* ************************************************
     * 内部方法
     * ************************************************ */
    
    /**
     * 生成单条 insert sql 语句
     * 
     * @param obj
     * @param camelhumpToUnderline 是否驼峰命名转下划线命名
     * @return
     */
    private String genInsertSql(final T obj, final boolean camelhumpToUnderline) {
        @SuppressWarnings("unchecked")
        final Class<T> clazz = (Class<T>) ClassUtils.getOriginalClass(obj.getClass());
        
        SQL sql = new SQL().INSERT_INTO(ModelUtils.getTableName(clazz));
        
        for (Field property : FieldUtils.getAllFields(clazz)) {
            if (property.isAnnotationPresent(Transient.class))
                continue;
            
            final String propertyName = property.getName();
            final Object inValue =  MethodUtils.readProperty(obj, propertyName);
            
            if (ObjectUtils.isEmpty(inValue))
                continue;    // 只添加非空字段
            
            sql.VALUES("`" + (camelhumpToUnderline ? StringUtils.toUnderScoreCase(propertyName).toUpperCase()
                    : propertyName.toUpperCase()) + "`", "#{" + propertyName + "}");
        }
        
        return sql.toString();
    }
    
    /**
     * 生成批量 insert 语句
     * 
     * @param para
     * @param camelhumpToUnderline 是否驼峰命名转下划线命名
     * @return
     */
    @SuppressWarnings("unchecked")
    private String genBatchInsertSql(final Map<String, Object> para, final boolean camelhumpToUnderline) {
        if (para.get("list") == null) 
            return "SELECT 1 FROM dual";
                    
        final List<T> list = (List<T>) para.get("list");
        
        if (list.size() == 0) {    
            return "SELECT 1 FROM dual";    
        }
        
        final Object firstObj = list.get(0);
        final Class<T> clazz = ClassUtils.getOriginalClass((Class<T>) firstObj.getClass());
        
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(ModelUtils.getTableName(clazz)).append(" (id, ");
        StringBuilder valueSql = new StringBuilder("#{list[0].id}, ");
        
        int i = 0;
        for (Field property : clazz.getDeclaredFields()) {
            if (property.isAnnotationPresent(Transient.class))
                continue;
            
            final String propertyName = property.getName();
            final Object inValue = MethodUtils.readProperty(firstObj, propertyName);
            
            if (ObjectUtils.isEmpty(inValue))
                continue;    // 只添加非空字段
            
            if (i++ > 0) {
                sql.append(", ");
                valueSql.append(", ");
            }
            
            sql.append("`").append(camelhumpToUnderline ? StringUtils.toUnderScoreCase(propertyName).toUpperCase() :
                    propertyName.toUpperCase()).append("`");
            
            valueSql.append("#{list[?].").append(propertyName).append("}");
        }
        
        sql.append(") VALUES "); 
        
        for (int j = 0; j < list.size(); j++) {
            if (j > 0)
                sql.append(",");
                
            sql.append("(");
            String vSql = StringUtils.replaceChars(valueSql.toString(), "?", String.valueOf(j));
            sql.append(vSql);
            sql.append(")");
        }
        
        return sql.toString();
    }
    
}
