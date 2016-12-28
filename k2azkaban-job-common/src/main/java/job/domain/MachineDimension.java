package job.domain;

/**
 * @author lidong 12/1/16.
 */
public class MachineDimension {

    private String id;
    private String dimensionType;        // 维度类别
    private String dimensionCode;        // 维度编号
    private String dimensionName;        // 维度名称
    private Integer ldpid;               //LDPID
    private Integer dateLimit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(Integer dateLimit) {
        this.dateLimit = dateLimit;
    }

    public String getDimensionType() {
        return dimensionType;
    }

    public void setDimensionType(String dimensionType) {
        this.dimensionType = dimensionType;
    }

    public String getDimensionCode() {
        return dimensionCode;
    }

    public void setDimensionCode(String dimensionCode) {
        this.dimensionCode = dimensionCode;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public Integer getLdpid() {
        return ldpid;
    }

    public void setLdpid(Integer ldpid) {
        this.ldpid = ldpid;
    }
    
}
