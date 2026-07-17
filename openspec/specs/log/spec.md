# 操作日志 - Log

## Purpose
记录和查询用户操作日志。

## Requirements

### REQ-LOG-001: 日志列表
The system SHALL display a paginated list of operation logs with columns: 操作人, 操作类型, 操作目标, IP地址, 操作时间.

### REQ-LOG-002: 日志查询
The system SHALL support filtering logs by user, action, and target.

## Scenarios

### SCEN-LOG-001: 查看日志
- Given 系统有操作日志
- When 用户进入操作日志页面
- Then 显示日志列表

### SCEN-LOG-002: 查询日志
- When 输入筛选条件并点击查询
- Then 显示匹配的日志
