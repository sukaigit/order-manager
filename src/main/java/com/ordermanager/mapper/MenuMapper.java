package com.ordermanager.mapper;

import com.ordermanager.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<Menu> selectList(@Param("code") String code, @Param("label") String label,
                          @Param("route") String route, @Param("type") String type);

    Menu selectById(@Param("id") Long id);

    Menu selectByCode(@Param("code") String code);

    int insert(Menu menu);

    int update(Menu menu);

    int deleteById(@Param("id") Long id);

    List<Menu> selectChildren(@Param("parent") String parent);

    List<Menu> selectAll();
}
