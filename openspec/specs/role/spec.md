# 角色管理 - Role

## Purpose
管理角色的增删改查和权限分配。

## Requirements

### REQ-ROLE-001: 角色列表
The system SHALL display a paginated list of roles with columns: 角色编号, 角色名称, 备注, 操作.

### REQ-ROLE-002: 新增角色
The system SHALL allow creating roles with fields: 角色编号(必填), 角色名称(必填), 备注.

### REQ-ROLE-003: 编辑角色
The system SHALL allow editing roles.

### REQ-ROLE-004: 删除角色
The system SHALL allow deleting roles.

### REQ-ROLE-005: 分配权限
The system SHALL allow assigning function permissions to roles.

## Scenarios

### SCEN-ROLE-001: 查看角色列表
- Given 系统有角色数据
- When 用户进入角色管理
- Then 显示角色列表

### SCEN-ROLE-002: 新增角色
- When 点击新增，填写编号和名称
- Then 保存成功

### SCEN-ROLE-003: 编辑角色
- When 点击编辑，修改角色名称
- Then 更新成功

### SCEN-ROLE-004: 删除角色
- When 点击删除并确认
- Then 角色删除

### SCEN-ROLE-005: 分配权限
- When 点击分配权限，勾选功能
- Then 角色权限更新
