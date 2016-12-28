package job.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 包装 Sql
 *
 * @author lidong
 */
public class BoundSql implements Serializable {
    private static final long serialVersionUID = 9004216128304952283L;

    /**
     * SQL 语句
     */
    private String sql;
    
    /**
     * 用于储存PreparedStatement，set的值
     */
    private List<Object> inValues = new ArrayList<Object>();
    
    public BoundSql(final String sql, final Object ... inValues) {
        this.sql = sql;
        
        if (inValues != null) {
            Collections.addAll(this.inValues, inValues);
        }
    }
    
    public String getSql() {
            return sql;
    }
    
    public List<Object> getInValues() {
        return inValues;
    }

    public String toStringInValues() {
        if (!inValues.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < inValues.size(); i++) {

                if (i != 0)
                    sb.append(" , ");

                Object inValue = inValues.get(i);

                if (inValue == null) {
                    sb.append("''");
                } else {
                    String strValue = Objects.toString(inValue);

                    if (strValue.length() > 20) {
                        strValue = strValue.substring(0, 20) + " ...";
                    }

                    sb.append(strValue).append("(").append(inValue.getClass().getName()).append(")");
                }
            }

            return sb.toString();
        }

        return "";
    }

    public void clearParams() {
        inValues.clear();
    }

    public void setInValue(final List<Object> inValues) {
        if (inValues != null) {
            this.inValues = inValues;
        }
    }

    public void setInValue(final int parameterIndex, final Object inValue) {
        while(inValues.size() < parameterIndex) {
            inValues.add(null);
        }

        inValues.set(parameterIndex-1, inValue);
    }

}
