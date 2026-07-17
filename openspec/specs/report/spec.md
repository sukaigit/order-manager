# 报表统计 - Report

## Purpose
展示订单数据的统计报表，支持多维度筛选和导出。

## Requirements

### REQ-REPORT-001: 订单金额汇总
The system SHALL display total order amount and count by status.

### REQ-REPORT-002: 按订单类型统计
The system SHALL display order statistics grouped by order type.

### REQ-REPORT-003: 按时间段统计
The system SHALL support filtering statistics by date range.

### REQ-REPORT-004: 合作方订单量统计
The system SHALL display partner rankings by order count and amount.

### REQ-REPORT-005: 筛选维度
The system SHALL support filtering by: 时间范围, 订单类型, 合作方, 状态.

### REQ-REPORT-006: 导出
The system SHALL support exporting report data to Excel (.xls).

## Scenarios

### SCEN-REPORT-001: 查看报表
- Given 系统有订单数据
- When 用户访问报表统计页面
- Then 展示各项统计数据

### SCEN-REPORT-002: 筛选报表
- Given 报表页面已加载
- When 用户选择筛选条件
- Then 报表数据按条件更新

### SCEN-REPORT-003: 导出报表
- Given 报表页面有数据
- When 用户点击导出
- Then 下载 Excel 文件
