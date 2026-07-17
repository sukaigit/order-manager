package com.ordermanager.service;

import com.ordermanager.entity.Order;
import com.ordermanager.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final OrderMapper orderMapper;

    public DashboardService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public Map<String, Object> getDashboard() {
        Map<String, Object> data = new LinkedHashMap<>();

        long totalOrders = orderMapper.selectTotalOrders();
        BigDecimal totalAmount = orderMapper.selectTotalAmount();
        long settledOrders = orderMapper.selectCountByStatus("已结清");
        BigDecimal settledAmount = orderMapper.selectAmountByStatus("已结清");

        data.put("totalOrders", totalOrders);
        data.put("totalAmount", totalAmount);
        data.put("settledOrders", settledOrders);
        data.put("settledAmount", settledAmount);

        List<Order> recentOrders = orderMapper.selectRecentOrders(10);
        List<Map<String, Object>> mappedOrders = new java.util.ArrayList<>();
        for (Order o : recentOrders) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("code", o.getCode());
            m.put("name", o.getName());
            m.put("type", o.getType());
            m.put("partner", o.getPartnerName());
            m.put("amount", o.getAmount());
            m.put("status", o.getStatus());
            m.put("statusCls", "待审核".equals(o.getStatus()) ? "pending" : "已下单".equals(o.getStatus()) ? "active" : "approved");
            m.put("date", o.getOrderTime() != null ? o.getOrderTime().toString().replace("T", " ") : "");
            mappedOrders.add(m);
        }
        data.put("recentOrders", mappedOrders);

        List<Map<String, Object>> typeStats = orderMapper.selectTypeStats(null, null);
        data.put("typeStats", typeStats);

        List<Map<String, Object>> partnerRank = orderMapper.selectPartnerRank();
        data.put("partnerRank", partnerRank);

        return data;
    }
}
