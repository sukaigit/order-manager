package com.ordermanager.mapper;

import com.ordermanager.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptMapper {
    List<Department> selectList(@Param("code") String code, @Param("name") String name,
                                @Param("offset") int offset, @Param("limit") int limit);

    long selectCount(@Param("code") String code, @Param("name") String name);

    Department selectById(@Param("id") Long id);

    Department selectByCode(@Param("code") String code);

    int insert(Department dept);

    int update(Department dept);

    int deleteById(@Param("id") Long id);

    List<Department> selectAll();
}
