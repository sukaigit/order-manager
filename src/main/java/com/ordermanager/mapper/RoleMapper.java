package com.ordermanager.mapper;

import com.ordermanager.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    List<Role> selectList(@Param("code") String code, @Param("name") String name,
                          @Param("offset") int offset, @Param("limit") int limit);

    long selectCount(@Param("code") String code, @Param("name") String name);

    Role selectById(@Param("id") Long id);

    Role selectByCode(@Param("code") String code);

    int insert(Role role);

    int update(Role role);

    int deleteById(@Param("id") Long id);

    List<Role> selectAll();
}
