package com.ordermanager.mapper;

import com.ordermanager.entity.Partner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PartnerMapper {
    List<Partner> selectList(@Param("code") String code, @Param("name") String name,
                             @Param("contact") String contact,
                             @Param("offset") int offset, @Param("limit") int limit);

    long selectCount(@Param("code") String code, @Param("name") String name,
                     @Param("contact") String contact);

    Partner selectById(@Param("id") Long id);

    Partner selectByCode(@Param("code") String code);

    Partner selectByName(@Param("name") String name);

    int insert(Partner partner);

    int update(Partner partner);

    int deleteById(@Param("id") Long id);

    List<Partner> selectAll();

    long selectOrderCount(@Param("partnerId") Long partnerId);
    Long selectMaxId();
}
