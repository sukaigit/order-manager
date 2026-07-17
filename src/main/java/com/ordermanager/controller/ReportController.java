package com.ordermanager.controller;

import com.ordermanager.common.Result;
import com.ordermanager.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/type")
    public Result<Map<String, Object>> typeStats(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long partnerId) {
        return Result.success(reportService.typeStats(type, partnerId));
    }

    @GetMapping("/partner")
    public Result<Map<String, Object>> partnerStats(
            @RequestParam(required = false) Long partnerId,
            @RequestParam(required = false) String type) {
        return Result.success(reportService.partnerStats(partnerId, type));
    }

    @GetMapping("/status")
    public Result<Map<String, Object>> statusStats(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long partnerId) {
        return Result.success(reportService.statusStats(status, type, partnerId));
    }
}
