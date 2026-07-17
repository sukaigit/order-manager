# 登录认证 - Auth

## Purpose
用户登录、退出、修改密码、验证码、锁定。

## Requirements

### REQ-AUTH-001: 登录
The system SHALL provide a login page with username, password, and captcha fields.
- 验证码: canvas 绘制 4 位数字验证码
- 登录成功 → 跳转首页
- 首次登录(firstLogin=true) → 跳转强制改密页 `/force-password?force=true`

### REQ-AUTH-002: 登录失败处理
The system SHALL display error messages on login failure.
- 密码错误次数累计，错误5次后锁定账户
- 锁定时显示锁定提示 `locked=true`

### REQ-AUTH-003: 退出
The system SHALL provide a logout button in the sidebar that redirects to the login page.

### REQ-AUTH-004: 修改密码
The system SHALL provide a change password page accessible via `/change-password`.
- 非强制模式: 需输入旧密码、新密码、确认密码
- 强制模式(isForce): 首次登录时跳转，只需新密码和确认密码

### REQ-AUTH-005: 密码安全规则
The system SHALL enforce password security rules:
- 长度 8~20 位
- 必须包含大写字母、小写字母、数字、特殊字符中至少 3 种
- 新密码不能与当前密码相同
- 实时校验密码强度(checkStrength)

## Scenarios

### SCEN-AUTH-001: 用户登录成功
- Given 用户名密码正确
- When 用户输入凭据和验证码并点击登录
- Then 登录成功，跳转首页（非首次）或强制改密码页（首次）

### SCEN-AUTH-002: 用户登录失败
- Given 用户名或密码错误
- When 用户点击登录
- Then 显示错误提示，验证码刷新
- When 错误累计5次
- Then 账户锁定，显示锁定提示

### SCEN-AUTH-003: 用户退出
- Given 用户已登录
- When 用户点击侧边栏退出按钮
- Then 返回到登录页

### SCEN-AUTH-004: 修改密码（非强制）
- Given 用户已登录
- When 用户进入修改密码页，输入旧密码+新密码+确认密码
- Then 校验旧密码正确且新密码符合规则，修改成功

### SCEN-AUTH-005: 首次登录强制改密
- Given 用户首次登录(firstLogin=true)
- When 登录成功
- Then 自动跳转到强制改密页
- When 用户设置新密码（符合规则）
- Then 密码更新，firstLogin=false，重新登录
