package com.ordermanager.mapper;

import com.ordermanager.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selectList(@Param("code") String code, @Param("name") String name,
                          @Param("role") String role, @Param("dept") String dept,
                          @Param("org") String org, @Param("active") Integer active,
                          @Param("offset") int offset, @Param("limit") int limit);

    long selectCount(@Param("code") String code, @Param("name") String name,
                     @Param("role") String role, @Param("dept") String dept,
                     @Param("org") String org, @Param("active") Integer active);

    User selectById(@Param("id") Long id);

    User selectByName(@Param("name") String name);

    int insert(User user);

    int update(User user);

    int deleteById(@Param("id") Long id);

    int updateLocked(@Param("id") Long id, @Param("locked") Integer locked);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int updateFirstLogin(@Param("id") Long id, @Param("firstLogin") Integer firstLogin);

    List<User> selectAll();
}
