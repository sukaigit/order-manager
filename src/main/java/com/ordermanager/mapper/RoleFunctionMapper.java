package com.ordermanager.mapper;

import com.ordermanager.entity.RoleFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleFunctionMapper {
    List<RoleFunction> selectByRoleId(@Param("roleId") Long roleId);

    List<String> selectFuncCodesByRoleId(@Param("roleId") Long roleId);

    int insertBatch(@Param("list") List<RoleFunction> list);

    int deleteByRoleId(@Param("roleId") Long roleId);

    int deleteByFuncCode(@Param("funcCode") String funcCode);
}
