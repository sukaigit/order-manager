# 部门管理 - Department

## Purpose
管理部门的增删改查。

## Requirements

### REQ-DEPT-001: 部门列表
The system SHALL display a paginated list of departments with columns: 部门编号, 部门名称, 备注, 操作.

### REQ-DEPT-002: 新增部门
The system SHALL allow creating departments with fields: 部门编号(必填), 部门名称(必填), 备注.

### REQ-DEPT-003: 编辑部门
The system SHALL allow editing departments.

### REQ-DEPT-004: 删除部门
The system SHALL allow deleting departments.

## Scenarios

### SCEN-DEPT-001: 查看部门列表
- Given 系统有部门数据
- When 用户进入部门管理
- Then 显示部门列表

### SCEN-DEPT-002: 新增部门
- When 点击新增，填写编号和名称
- Then 保存成功，列表更新

### SCEN-DEPT-003: 编辑部门
- When 点击编辑，修改名称
- Then 部门信息更新

### SCEN-DEPT-004: 删除部门
- When 点击删除并确认
- Then 部门从列表中移除
