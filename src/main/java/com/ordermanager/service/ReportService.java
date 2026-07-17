package com.ordermanager.service;

import com.ordermanager.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final OrderMapper orderMapper;

    public ReportService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public Map<String, Object> typeStats(String type, Long partnerId) {
        List<Map<String, Object>> list = orderMapper.selectTypeStats(type, partnerId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", list);
        result.put("total", list.size());
        return result;
    }

    public Map<String, Object> partnerStats(Long partnerId, String type) {
        List<Map<String, Object>> list = orderMapper.selectPartnerStats(partnerId, type);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", list);
        result.put("total", list.size());
        return result;
    }

    public Map<String, Object> statusStats(String status, String type, Long partnerId) {
        List<Map<String, Object>> list = orderMapper.selectStatusStats(status, type, partnerId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", list);
        result.put("total", list.size());
        return result;
    }
}
