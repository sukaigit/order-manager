package com.ordermanager.service;

import com.ordermanager.common.PageResult;
import com.ordermanager.entity.Order;
import com.ordermanager.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderMapper orderMapper;

    public OrderService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public PageResult<Order> page(String code, String type, Long partnerId, String status,
                                   int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Order> list = orderMapper.selectList(code, type, partnerId, status, offset, pageSize);
        long total = orderMapper.selectCount(code, type, partnerId, status);
        return new PageResult<>(list, total);
    }

    public Order getById(Long id) {
        return orderMapper.selectById(id);
    }

    public int create(Order order) {
        order.setStatus("待审核");
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return orderMapper.insert(order);
    }

    public int update(Order order) {
        return orderMapper.update(order);
    }

    public int delete(Long id) {
        return orderMapper.deleteById(id);
    }

    // Dashboard
    public long getTotalOrders() { return orderMapper.selectTotalOrders(); }
    public BigDecimal getTotalAmount() { return orderMapper.selectTotalAmount(); }
    public long getSettledOrders() { return orderMapper.selectCountByStatus("已结清"); }
    public BigDecimal getSettledAmount() { return orderMapper.selectAmountByStatus("已结清"); }
    public List<Order> getRecentOrders(int limit) { return orderMapper.selectRecentOrders(limit); }
    public List<Map<String, Object>> getPartnerRank() { return orderMapper.selectPartnerRank(); }

    // Reports
    public List<Map<String, Object>> getTypeStats(String type, Long partnerId) {
        return orderMapper.selectTypeStats(type, partnerId);
    }

    public List<Map<String, Object>> getPartnerStats(Long partnerId, String type) {
        return orderMapper.selectPartnerStats(partnerId, type);
    }

    public List<Map<String, Object>> getStatusStats(String status, String type, Long partnerId) {
        return orderMapper.selectStatusStats(status, type, partnerId);
    }
}
