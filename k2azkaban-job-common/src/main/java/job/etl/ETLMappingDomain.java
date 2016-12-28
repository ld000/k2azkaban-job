package job.etl;

import java.util.List;

/**
 * @author lidong 16-10-31.
 */
public class ETLMappingDomain {

    private String sourceTable;
    private String targetTable;
    private String type;
    private String flagColumn;
    private List<String> sourceWhere;
    private History history;
    private List<ColumnMappings> columnMappings;
    private List<String> deleteWhere;

    public class History {
        private String type;
        private String BDMTable;
        private String HisTable;

        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public String getBDMTable() {
            return BDMTable;
        }
        public void setBDMTable(String BDMTable) {
            this.BDMTable = BDMTable;
        }
        public String getHisTable() {
            return HisTable;
        }
        public void setHisTable(String hisTable) {
            HisTable = hisTable;
        }
    }

    public class ColumnMappings {
        private String source;
        private String target;
        private String constant;

        public String getSource() {
            return source;
        }
        public void setSource(String source) {
            this.source = source;
        }
        public String getTarget() {
            return target;
        }
        public void setTarget(String target) {
            this.target = target;
        }
        public String getConstant() {
            return constant;
        }
        public void setConstant(String constant) {
            this.constant = constant;
        }
    }

    public String getSourceTable() {
        return sourceTable;
    }
    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }
    public String getTargetTable() {
        return targetTable;
    }
    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List<String> getSourceWhere() {
        return sourceWhere;
    }
    public void setSourceWhere(List<String> sourceWhere) {
        this.sourceWhere = sourceWhere;
    }
    public History getHistory() {
        return history;
    }
    public void setHistory(History history) {
        this.history = history;
    }
    public List<ColumnMappings> getColumnMappings() {
        return columnMappings;
    }
    public void setColumnMappings(List<ColumnMappings> columnMappings) {
        this.columnMappings = columnMappings;
    }
    public String getFlagColumn() {
        return flagColumn;
    }
    public void setFlagColumn(String flagColumn) {
        this.flagColumn = flagColumn;
    }
    public List<String> getDeleteWhere() {
        return deleteWhere;
    }
    public void setDeleteWhere(List<String> deleteWhere) {
        this.deleteWhere = deleteWhere;
    }

}
