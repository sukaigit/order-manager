# 订单管理 - Order

## Purpose
管理订单的增删改查、筛选、导出。

## Requirements

### REQ-ORDER-001: 字段定义
The system SHALL support the following fields: 订单编号, 订单类型 (中间业务/数据开发/应用开发), 合作方 (从合作方列表选择), 订单金额 (decimal 10.2, min 0.01), 订单时间 (自动生成), 状态 (待审核/已下单/已结清), 备注 (可选).

### REQ-ORDER-002: 新增订单
The system SHALL allow creating orders with fields: 订单类型, 合作方, 订单金额, 备注.

### REQ-ORDER-003: 编辑订单
The system SHALL allow editing only when status is 待审核.

### REQ-ORDER-004: 删除订单
The system SHALL allow deleting only when status is 待审核.

### REQ-ORDER-005: 筛选
The system SHALL support filtering by all fields.

### REQ-ORDER-006: 导出
The system SHALL support exporting the filtered order list to Excel (.xls).

## Scenarios

### SCEN-ORDER-001: 新增订单
- Given 用户已登录
- When 用户点击新增订单
- Then 弹出表单，保存后订单出现在列表中

### SCEN-ORDER-002: 编辑待审核订单
- Given 有一条待审核订单
- When 用户点击编辑
- Then 可修改字段并保存

### SCEN-ORDER-003: 不可编辑已结清
- Given 有一条已结清订单
- When 用户点击编辑
- Then 提示不可修改

### SCEN-ORDER-004: 删除待审核订单
- Given 有一条待审核订单
- When 用户点击删除
- Then 弹出确认框，确认后删除

### SCEN-ORDER-005: 筛选订单
- Given 有多条订单
- When 用户输入筛选条件
- Then 列表只显示符合条件的订单

### SCEN-ORDER-006: 导出订单
- Given 列表有订单数据
- When 用户点击导出
- Then 下载 Excel 文件
