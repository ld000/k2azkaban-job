package job.persistence.provider;

import com.k2data.job.utils.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.jdbc.SQL;

import javax.persistence.Transient;
import java.lang.reflect.Field;

/**
 * 通用 select sql 生成类
 * 
 * @author lidong
 * @param <T>
 */
public class SelectSqlProvider<T> {
    
    /**
     * 生成单条 select sql 语句, 只查询当前类包含的字段，不查询父类字段, 驼峰命名会转成下划线命名
     * 
     * @return
     */
    public String querySingleTableWithCamelhumpToUnderline(T condition) {
        return genQuerySql(condition, true);
    }
    
    /**
     * 生成单条 select sql 语句, 只查询当前类包含的字段，不查询父类字段
     * 
     * @return
     */
    public String querySingleTable(T condition) {
        return genQuerySql(condition, false);
    }
    
    /**
     * 生成单表统计指定条件数据数量 sql, 驼峰命名会转成下划线命名
     * 
     * @return
     */
    public String countSingleTableWithCamelhumpToUnderline(T condition) {
        return genCountSql(condition, true);
    }
    
    /**
     * 生成单表统计指定条件数据数量 sql
     * 
     * @return
     */
    public String countSingleTable(T condition) {
        return genCountSql(condition, false);
    }
    
    /**
     * 生成单表查询指定条件数据是否存在 sql
     * 
     * @return
     */
    public String checkExist(T condition) {
        return genCheckExistSql(condition, false);
    }
    
    /**
     * 生成单表查询指定条件数据是否存在 sql, 驼峰命名会转成下划线命名
     * 
     * @return
     */
    public String checkExistWithCamelhumpToUnderline(T condition) {
        return genCheckExistSql(condition, true);
    }
    
    /* ************************************************
     * 内部方法
     * ************************************************ */
    
    /**
     * 生成单表 select sql 语句
     * 
     * @param condition 查询条件
     * @param camelhumpToUnderline 是否驼峰命名转下划线命名
     * @return
     */
    private String genQuerySql(final T condition, final boolean camelhumpToUnderline) {
        return genQuerySingleTableSql(condition, false, false, camelhumpToUnderline);
    }
    
    /**
     * 生成单表统计指定条件数据数量 sql 语句
     * 
     * @param condition 查询条件
     * @param camelhumpToUnderline 是否驼峰命名转下划线命名
     * @return
     */
    private String genCountSql(final T condition, final boolean camelhumpToUnderline) {
        return genQuerySingleTableSql(condition, true, false, camelhumpToUnderline);
    }
    
    /**
     * 生成单表查询指定条件数据是否存在 sql 语句
     * 
     * @param condition 查询条件
     * @param camelhumpToUnderline 是否驼峰命名转下划线命名
     * @return
     */
    private String genCheckExistSql(final T condition, final boolean camelhumpToUnderline) {
        return genQuerySingleTableSql(condition, false, true, camelhumpToUnderline);
    }
    
    /**
     * 生成通用 select sql, 只查询当前类包含的字段，不查询父类字段
     * 
     * @param condition 查询条件
     * @param count 是否 count sql
     * @param checkExist 是否 checkExist sql
     * @param camelhumpToUnderline 是否驼峰命名转下划线命名
     * @return
     */
    private String genQuerySingleTableSql(final T condition, 
            final boolean count, final boolean checkExist, final boolean camelhumpToUnderline) {
        @SuppressWarnings("unchecked")
        final Class<T> clazz = (Class<T>) ClassUtils.getOriginalClass(condition.getClass());
        
        SQL sql = new SQL().FROM(ModelUtils.getTableName(clazz));
        
        // 根据不同查询类型添加不同的 SELECT 语句
        if (count) {
            sql.SELECT("COUNT(1)");
        } else if (checkExist) {
            sql.SELECT("1");
        } else {
            for (Field property : clazz.getDeclaredFields()) {
                if (property.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                
                final String propertyName = property.getName();
                
                sql.SELECT("`" + (camelhumpToUnderline ? StringUtils.toUnderScoreCase(propertyName).toUpperCase() :
                    propertyName.toUpperCase()) + "` AS \"" + propertyName + "\"");
            }
        }
        
        // 添加 WHERE 条件
        for (Field property : FieldUtils.getAllFields(clazz)) {
            if (property.isAnnotationPresent(Transient.class)) {
                continue;
            }
            
            final String propertyName = property.getName();
            final Object inValue = MethodUtils.readProperty(condition, propertyName);
            
            // 只添加非空的字段
            if (ObjectUtils.isEmpty(inValue)) {
                continue;
            }
            
            sql.WHERE("`" + (camelhumpToUnderline ? StringUtils.toUnderScoreCase(propertyName).toUpperCase() :
                propertyName.toUpperCase()) + "` = #{" + propertyName + "}");
        }
        
        if (checkExist)
            return sql.toString() + " LIMIT 1";
        
        return sql.toString();
    }
    
}
