package com.ordermanager.mapper;

import com.ordermanager.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    List<Order> selectList(@Param("code") String code, @Param("type") String type,
                           @Param("partnerId") Long partnerId, @Param("status") String status,
                           @Param("offset") int offset, @Param("limit") int limit);

    long selectCount(@Param("code") String code, @Param("type") String type,
                     @Param("partnerId") Long partnerId, @Param("status") String status);

    Order selectById(@Param("id") Long id);

    Order selectByCode(@Param("code") String code);

    int insert(Order order);

    int update(Order order);

    int deleteById(@Param("id") Long id);

    // Dashboard queries
    long selectTotalOrders();
    BigDecimal selectTotalAmount();
    long selectCountByStatus(@Param("status") String status);
    BigDecimal selectAmountByStatus(@Param("status") String status);
    List<Order> selectRecentOrders(@Param("limit") int limit);

    // Report queries
    List<Map<String, Object>> selectTypeStats(@Param("type") String type, @Param("partnerId") Long partnerId);
    List<Map<String, Object>> selectPartnerStats(@Param("partnerId") Long partnerId, @Param("type") String type);
    List<Map<String, Object>> selectStatusStats(@Param("status") String status, @Param("type") String type,
                                                @Param("partnerId") Long partnerId);
    List<Map<String, Object>> selectPartnerRank();
}
