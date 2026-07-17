package com.ordermanager.mapper;

import com.ordermanager.entity.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrgMapper {
    List<Organization> selectList(@Param("code") String code, @Param("label") String label,
                                  @Param("sname") String sname, @Param("level") String level);

    Organization selectById(@Param("id") Long id);

    Organization selectByCode(@Param("code") String code);

    int insert(Organization org);

    int update(Organization org);

    int deleteById(@Param("id") Long id);

    List<Organization> selectChildren(@Param("parent") String parent);

    List<Organization> selectAll();
}
