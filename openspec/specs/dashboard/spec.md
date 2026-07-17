# 首页 - Dashboard

## Purpose
系统首页看板，展示核心运营数据。

## Requirements

### REQ-DASH-001: 统计卡片
The system SHALL display four statistic cards: 累计订单数、累计订单金额、今日订单数、今日订单金额.

### REQ-DASH-002: 近期订单列表
The system SHALL display a list of recent orders (latest 5).

### REQ-DASH-003: 订单类型图表
The system SHALL display a chart showing order distribution by order type.

### REQ-DASH-004: 合作方排名
The system SHALL display a partner ranking list sorted by order count and amount.

## Scenarios

### SCEN-DASH-001: 首页展示
- Given 系统有订单数据和合作方数据
- When 用户访问首页
- Then 页面显示统计卡片、近期订单列表、订单类型图表、合作方排名
