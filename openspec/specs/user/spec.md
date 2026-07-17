# 用户管理 - User

## Purpose
管理用户的增删改查、状态管理。

## Requirements

### REQ-USER-001: 用户列表
The system SHALL display a paginated list of users with columns: 用户编号, 用户名, 角色, 部门, 机构, 状态, 创建时间, 备注, 操作.

### REQ-USER-002: 新增用户
The system SHALL allow creating users with fields: 用户名(必填), 角色(必填), 部门(必填), 机构, 备注.
Default password: Uu888888!.

### REQ-USER-003: 编辑用户
The system SHALL allow editing user fields: 用户名, 角色, 部门, 机构, 备注.

### REQ-USER-004: 删除用户
The system SHALL allow deleting users.

### REQ-USER-005: 锁定/解锁
The system SHALL allow locking and unlocking user accounts.

### REQ-USER-006: 重置密码
The system SHALL allow resetting user password to default.

## Scenarios

### SCEN-USER-001: 查看用户列表
- Given 系统有用户数据
- When 用户进入用户管理页面
- Then 显示用户列表

### SCEN-USER-002: 新增用户
- Given 用户已登录
- When 点击新增用户，填写表单并保存
- Then 新用户出现在列表中

### SCEN-USER-003: 编辑用户
- Given 存在一个用户
- When 点击编辑并修改
- Then 用户信息更新

### SCEN-USER-004: 删除用户
- Given 存在一个用户
- When 点击删除并确认
- Then 用户从列表中移除

### SCEN-USER-005: 锁定用户
- Given 存在一个用户
- When 点击锁定/禁用
- Then 用户状态变为锁定

### SCEN-USER-006: 重置密码
- Given 存在一个用户
- When 点击重置密码
- Then 用户密码重置为默认值，first_login=true
