# 订单管理系统 — 测试案例

## 1. 合作方管理

### TC-PARTNER-001: 查询合作方列表
- 接口: `GET /api/partners?page=1&pageSize=5`
- 预期: 返回分页合作方列表，含 PARTNER-001~008

### TC-PARTNER-002: 新增合作方
- 接口: `POST /api/partners`
- 请求: `{"name":"测试合作方","contact":"测试人","phone":"13800138999"}`
- 预期: 返回成功，code 自动生成

### TC-PARTNER-003: 编辑合作方
- 接口: `PUT /api/partners/{id}`
- 预期: 修改成功

### TC-PARTNER-004: 删除无订单合作方
- 接口: `DELETE /api/partners/{id}`
- 预期: 删除成功

### TC-PARTNER-005: 删除有订单合作方
- 接口: `DELETE /api/partners/1` (合作方A有订单)
- 预期: 返回400，提示不可删除

## 2. 订单管理

### TC-ORDER-001: 查询订单列表
- 接口: `GET /api/orders?page=1&pageSize=5`
- 预期: 返回分页订单列表

### TC-ORDER-002: 新增订单
- 接口: `POST /api/orders`
- 请求: `{"name":"测试订单","type":"中间业务","partnerId":1,"amount":10000}`
- 预期: 返回成功，code 自动生成，status=待审核

### TC-ORDER-003: 编辑待审核订单
- 接口: `PUT /api/orders/{id}`
- 预期: 修改成功

### TC-ORDER-004: 不可编辑已结清订单
- 接口: `PUT /api/orders/{settledId}`
- 预期: 返回400

### TC-ORDER-005: 删除待审核订单
- 接口: `DELETE /api/orders/{id}`
- 预期: 删除成功

## 3. 首页统计

### TC-DASH-001: 首页数据
- 接口: `GET /api/dashboard`
- 预期: 返回 totalOrders/totalAmount/recentOrders/typeStats/partnerRank

## 4. 用户管理

### TC-USER-001: 查询用户
- 接口: `GET /api/users`
- 预期: 返回用户列表（admin/zhangsan）

### TC-USER-002: 新增用户
- 接口: `POST /api/users`
- 预期: 新增成功，默认密码

## 5. 部门/机构/角色/菜单/功能
- 接口: CRUD 检查，确认返回正确数据
