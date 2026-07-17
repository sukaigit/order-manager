# 合作方管理 - Partner

## Purpose
管理合作方信息的增删改查、筛选、导出。

## Requirements

### REQ-PARTNER-001: 字段定义
The system SHALL support the following fields: 合作方编号 (自动生成 PARTNER-{NNN}), 合作方名称 (唯一必填), 联系人 (必填), 联系电话 (11位手机号), 备注 (可选).

### REQ-PARTNER-002: 新增合作方
The system SHALL allow creating partners with fields: 合作方名称, 联系人, 联系电话, 备注.

### REQ-PARTNER-003: 编辑合作方
The system SHALL allow editing all fields of a partner.

### REQ-PARTNER-004: 删除合作方
The system SHALL allow deleting a partner only if no orders reference it.

### REQ-PARTNER-005: 筛选
The system SHALL support filtering by all fields.

### REQ-PARTNER-006: 导出
The system SHALL support exporting the filtered partner list to Excel (.xls).

## Scenarios

### SCEN-PARTNER-001: 新增合作方
- Given 用户已登录
- When 用户点击新增合作方
- Then 弹出表单，保存后合作方出现在列表中

### SCEN-PARTNER-002: 编辑合作方
- Given 存在一个合作方
- When 用户点击编辑
- Then 可修改所有字段并保存

### SCEN-PARTNER-003: 删除无订单合作方
- Given 该合作方没有关联订单
- When 用户点击删除
- Then 弹出确认框，确认后删除

### SCEN-PARTNER-004: 删除有订单合作方
- Given 该合作方有关联订单
- When 用户点击删除
- Then 提示不可删除

### SCEN-PARTNER-005: 筛选合作方
- Given 有多个合作方
- When 用户输入筛选条件
- Then 列表只显示符合条件的合作方

### SCEN-PARTNER-006: 导出合作方
- Given 列表有合作方数据
- When 用户点击导出
- Then 下载 Excel 文件
