# 订单管理系统 — 测试案例

基于 `openspec/specs/` 逐条编写，每个 Requirement 和 Scenario 对应一个测试案例。

---

## 1. 首页 — Dashboard

### REQ-DASH-001: 统计卡片
- **接口:** `GET /api/dashboard`
- **验证:** 返回 `totalOrders`(累计订单数)、`totalAmount`(累计订单金额)、`settledOrders`(累计已结清订单数)、`settledAmount`(累计已结清订单金额)
- **状态:** ✅

### REQ-DASH-002: 近期订单列表
- **接口:** `GET /api/dashboard`
- **验证:** `recentOrders` 数组长度 ≥ 1，每个订单含 code/type/partner/amount/status/date
- **状态:** ✅

### REQ-DASH-003: 订单类型图表
- **接口:** `GET /api/dashboard`
- **验证:** `typeStats` 数组含各类型(name/count/amount)
- **状态:** ✅

### REQ-DASH-004: 合作方排名
- **接口:** `GET /api/dashboard`
- **验证:** `partnerRank` 数组按金额降序排列，含 name/count/amount
- **状态:** ✅

---

## 2. 订单管理 — Order

### REQ-ORDER-001: 字段定义
- **接口:** `GET /api/orders?page=1&pageSize=5`
- **验证:** 返回字段包含 code/type/partnerId/amount/status/orderTime
- **状态:** ✅

### REQ-ORDER-002: 新增订单
- **接口:** `POST /api/orders`
- **请求:** `{"name":"测试","type":"中间业务","partnerId":1,"amount":10000}`
- **验证:** 返回 200，订单自动生成 code、orderTime，status 自动设为"待审核"
- **状态:** ✅

### REQ-ORDER-003: 编辑订单(仅待审核)
- **前置:** 新增一条待审核订单
- **接口:** `PUT /api/orders/{id}`
- **验证:** 待审核订单可修改，已结清订单返回 400
- **状态:** ✅

### REQ-ORDER-004: 删除订单(仅待审核)
- **前置:** 新增一条待审核订单
- **接口:** `DELETE /api/orders/{id}`
- **验证:** 待审核可删除，已下单/已结清返回 400
- **状态:** ✅

### REQ-ORDER-005: 筛选
- **接口:** `GET /api/orders?code=&type=中间业务&partner=&status=`
- **验证:** 按订单类型筛选后只返回匹配结果
- **状态:** ✅

### REQ-ORDER-006: 导出
- **接口:** `GET /api/orders/export`
- **验证:** 返回 Content-Type 为 Excel 文件流
- **状态:** ❌ (前端 XML 导出，后端未实现)

### SCEN-ORDER-001: 新增订单流程
- **步骤:** POST → GET 列表 → 新订单出现在列表中
- **状态:** ✅

### SCEN-ORDER-002: 编辑待审核订单
- **步骤:** 找到待审核订单 → PUT → 字段更新
- **状态:** ✅

### SCEN-ORDER-003: 不可编辑已结清
- **步骤:** 找到已结清订单 → PUT → 400
- **状态:** ✅

### SCEN-ORDER-004: 删除待审核订单
- **步骤:** 找到待审核订单 → DELETE → 列表移除
- **状态:** ✅

### SCEN-ORDER-005: 筛选订单
- **步骤:** GET 加筛选参数 → 列表只返回匹配项
- **状态:** ✅

### SCEN-ORDER-006: 导出订单
- **步骤:** 前端点击导出按钮 → 浏览器下载 .xls 文件
- **状态:** ✅ (前端功能)

---

## 3. 合作方管理 — Partner

### REQ-PARTNER-001: 字段定义
- **接口:** `GET /api/partners?page=1&pageSize=5`
- **验证:** 返回字段含 code/name/contact/phone/remark
- **状态:** ✅

### REQ-PARTNER-002: 新增合作方
- **接口:** `POST /api/partners`
- **请求:** `{"name":"测试合作方X","contact":"测试人","phone":"13800138000"}`
- **验证:** 返回 200，code 自动生成 PARTNER-{NNN}
- **状态:** ✅

### REQ-PARTNER-003: 编辑合作方
- **接口:** `PUT /api/partners/{id}`
- **验证:** 全字段可修改
- **状态:** ✅

### REQ-PARTNER-004: 删除(有关联订单不可删)
- **前置:** 合作方A(id=1) 有 3 条订单
- **接口:** `DELETE /api/partners/1`
- **验证:** 返回 400，提示"存在关联订单，无法删除"
- **状态:** ✅

### REQ-PARTNER-005: 筛选
- **接口:** `GET /api/partners?code=PARTNER-001&name=`
- **验证:** 按编号/名称/联系人筛选
- **状态:** ✅

### REQ-PARTNER-006: 导出
- **接口:** `GET /api/partners/export`
- **验证:** 返回 Excel 文件流
- **状态:** ❌ (后端未实现导出接口)

### SCEN-PARTNER-001: 新增合作方
- **状态:** ✅

### SCEN-PARTNER-002: 编辑合作方
- **状态:** ✅

### SCEN-PARTNER-003: 删除无订单合作方
- **状态:** ✅

### SCEN-PARTNER-004: 删除有订单合作方
- **状态:** ✅

### SCEN-PARTNER-005: 筛选合作方
- **状态:** ✅

### SCEN-PARTNER-006: 导出合作方
- **状态:** ❌ (后端未实现)

---

## 4. 报表统计 — Report

### 订单类型统计
- **接口:** `GET /api/reports/type`
- **验证:** 返回各类型订单数量+金额，支持按订单类型筛选
- **状态:** ✅

### 合作方统计
- **接口:** `GET /api/reports/partner`
- **验证:** 返回各合作方订单数量+金额，按金额降序
- **状态:** ✅

### 订单状态统计
- **接口:** `GET /api/reports/status`
- **验证:** 返回各状态(待审核/已下单/已结清)的订单数量+金额
- **状态:** ✅

---

## 汇总

| 模块 | 案例数 | 通过 | 失败 | 未实现 |
|:----|:-----:|:----:|:----:|:------:|
| 首页 | 5 | 5 | 0 | 0 |
| 订单管理 | 12 | 11 | 0 | 1(导出API) |
| 合作方管理 | 7 | 6 | 0 | 1(导出API) |
| 报表统计 | 3 | 3 | 0 | 0 |
| **合计** | **27** | **25** | **0** | **2** |

> **遗留问题:** 订单导出/合作方导出 后端API未实现（前端使用浏览器端XML导出，后端未提供文件流接口）
