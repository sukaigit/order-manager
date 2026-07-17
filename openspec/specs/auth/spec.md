# 登录认证 - Auth

## Purpose
用户登录、退出、修改密码。

## Requirements

### REQ-AUTH-001: 登录
The system SHALL allow users to log in with username and password.

### REQ-AUTH-002: 退出
The system SHALL provide a logout button in the sidebar that redirects to the login page.

### REQ-AUTH-003: 修改密码
The system SHALL provide a change password page accessible via `/change-password`.

## Scenarios

### SCEN-AUTH-001: 用户登录
- Given 用户未登录
- When 用户输入用户名和密码并点击登录
- Then 登录成功，跳转到首页

### SCEN-AUTH-002: 用户退出
- Given 用户已登录
- When 用户点击侧边栏退出按钮
- Then 返回到登录页

### SCEN-AUTH-003: 修改密码
- Given 用户已登录
- When 用户访问修改密码页面
- Then 显示密码修改表单
