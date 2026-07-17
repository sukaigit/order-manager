package com.ordermanager.mapper;

import com.ordermanager.entity.Function;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FuncMapper {
    List<Function> selectList(@Param("code") String code, @Param("name") String name,
                              @Param("menu") String menu, @Param("perm") String perm,
                              @Param("offset") int offset, @Param("limit") int limit);

    long selectCount(@Param("code") String code, @Param("name") String name,
                     @Param("menu") String menu, @Param("perm") String perm);

    Function selectById(@Param("id") Long id);

    Function selectByCode(@Param("code") String code);

    int insert(Function func);

    int update(Function func);

    int deleteById(@Param("id") Long id);

    List<Function> selectAll();
}
