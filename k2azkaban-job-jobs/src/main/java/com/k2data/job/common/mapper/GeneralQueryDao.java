package com.k2data.job.common.mapper;

import com.k2data.platform.domain.MachineDimension;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lidong 12/1/16.
 */
public interface GeneralQueryDao {

    @Select("SELECT value FROM sys_dict WHERE type = #{type} AND label = #{label}")
    String queryDictValue(@Param("label") String label, @Param("type") String type);

    @Select("SELECT id, dimensionType, dimensionName, ldpid, dateLimit" +
            " FROM lg_machineDimension" +
            " WHERE dimensionType = #{type}")
    List<MachineDimension> queryMachineDimensionList(@Param("type") Integer type);

}
