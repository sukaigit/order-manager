# 订单管理系统 — 测试案例

基于 `openspec/specs/` 逐条编写。每个功能含：正向案例、反向案例、边界值案例。

---

## 1. 首页 — Dashboard

### 正向案例

#### DASH-P-001: 首页完整数据
- **接口:** `GET /api/dashboard`
- **预期:** 返回 200，data 含 totalOrders/totalAmount/settledOrders/settledAmount/recentOrders/typeStats/partnerRank
- **验证:**
  - totalOrders > 0
  - totalAmount > 0
  - `recentOrders` 数组长度 ≤ 10
  - `typeStats` 数组含 中间业务/数据开发/应用开发
  - `partnerRank` 按金额降序排列

#### DASH-P-002: 近期订单字段完整
- **前置:** 首页有数据
- **验证:** 每条近期订单含 code/type/partner/amount/status/date/statusCls

#### DASH-P-003: 合作方排名排序正确
- **验证:** `partnerRank` 中每项金额 ≥ 下一项金额

### 反向案例

#### DASH-N-001: 无可访问接口时返回 401
- **接口:** `GET /api/dashboard`
- **请求头:** 无 token
- **预期:** 返回 401 (当前未实现鉴权，跳过)

---

## 2. 订单管理 — Order

### 正向案例

#### ORDER-P-001: 分页查询默认第1页
- **接口:** `GET /api/orders`
- **预期:** 返回 200，data.list 长度 ≤ 5，data.total > 0

#### ORDER-P-002: 分页查询第2页
- **接口:** `GET /api/orders?page=2&pageSize=5`
- **预期:** 返回 200，page=2 的数据与 page=1 不重叠

#### ORDER-P-003: 按订单类型筛选
- **接口:** `GET /api/orders?type=中间业务`
- **预期:** 返回列表中 type 全部为"中间业务"

#### ORDER-P-004: 按状态筛选
- **接口:** `GET /api/orders?status=待审核`
- **预期:** 返回列表中 status 全部为"待审核"

#### ORDER-P-005: 组合筛选
- **接口:** `GET /api/orders?type=数据开发&status=已下单`
- **预期:** 返回列表同时满足 type=数据开发 AND status=已下单

#### ORDER-P-006: 新增订单(全字段)
- **接口:** `POST /api/orders`
- **请求:** `{"name":"新增测试订单","type":"中间业务","partnerId":1,"amount":8888.88,"remark":"测试备注"}`
- **预期:** 返回 200，code 自动生成(格式ORD-{日期}-{序号})，orderTime 自动填充，status=待审核

#### ORDER-P-007: 新增订单(无备注)
- **接口:** `POST /api/orders`
- **请求:** `{"name":"无备注订单","type":"数据开发","partnerId":2,"amount":5000}`
- **预期:** 返回 200，remark 为 null 或空

#### ORDER-P-008: 编辑待审核订单
- **前置:** 先新增一条待审核订单
- **接口:** `PUT /api/orders/{id}`
- **请求:** `{"name":"修改后名称","type":"应用开发","partnerId":3,"amount":9999}`
- **预期:** 返回 200，再次查询该订单字段已更新

#### ORDER-P-009: 删除待审核订单
- **前置:** 先新增一条待审核订单
- **接口:** `DELETE /api/orders/{id}`
- **预期:** 返回 200，再次查询列表无此订单

#### ORDER-P-010: 导出全部订单
- **接口:** `GET /api/orders/export`
- **预期:** Content-Type=application/vnd.ms-excel，Content-Disposition=attachment，响应体为 XML Excel

#### ORDER-P-011: 导出筛选后订单
- **接口:** `GET /api/orders/export?type=中间业务`
- **预期:** 导出的 XML 中只包含中间业务的订单

### 反向案例

#### ORDER-N-001: 新增订单-金额为0
- **接口:** `POST /api/orders`
- **请求:** `{"name":"零金额","type":"中间业务","partnerId":1,"amount":0}`
- **预期:** 返回 200 或 400（视校验策略而定）

#### ORDER-N-002: 新增订单-金额为负数
- **接口:** `POST /api/orders`
- **请求:** `{"name":"负数金额","type":"中间业务","partnerId":1,"amount":-100}`
- **预期:** 返回 400

#### ORDER-N-003: 新增订单-不存在的合作方
- **接口:** `POST /api/orders`
- **请求:** `{"name":"无效合作方","type":"中间业务","partnerId":999,"amount":1000}`
- **预期:** 返回 400 或 500（外键约束）

#### ORDER-N-004: 编辑已结清订单
- **前置:** 找到一条 status=已结清的订单
- **接口:** `PUT /api/orders/{id}`
- **请求:** `{"name":"试图修改","amount":999}`
- **预期:** 返回 400

#### ORDER-N-005: 编辑已下单订单
- **前置:** 找到一条 status=已下单的订单
- **接口:** `PUT /api/orders/{id}`
- **预期:** 返回 400

#### ORDER-N-006: 删除已结清订单
- **前置:** 找到一条 status=已结清的订单
- **接口:** `DELETE /api/orders/{id}`
- **预期:** 返回 400

#### ORDER-N-007: 删除已下单订单
- **前置:** 找到一条 status=已下单的订单
- **接口:** `DELETE /api/orders/{id}`
- **预期:** 返回 400

#### ORDER-N-008: 删除不存在的订单
- **接口:** `DELETE /api/orders/99999`
- **预期:** 返回 404

#### ORDER-N-009: GET 不存在的订单
- **接口:** `GET /api/orders/99999`
- **预期:** 返回 404

### 边界值案例

#### ORDER-B-001: 金额最小值
- **接口:** `POST /api/orders`
- **请求:** `{"name":"边界金额","type":"中间业务","partnerId":1,"amount":0.01}`
- **预期:** 返回 200

#### ORDER-B-002: 订单名称超长
- **接口:** `POST /api/orders`
- **请求:** `{"name":"a".repeat(201),"type":"中间业务","partnerId":1,"amount":100}`
- **预期:** 返回 400（字段长度超限）

#### ORDER-B-003: 分页 pageSize=1
- **接口:** `GET /api/orders?pageSize=1`
- **预期:** 返回 1 条数据

#### ORDER-B-004: 分页 pageSize=50
- **接口:** `GET /api/orders?pageSize=50`
- **预期:** 返回全部数据（≤50 条）

#### ORDER-B-005: 分页超出范围
- **接口:** `GET /api/orders?page=999`
- **预期:** 返回空列表，total 仍正确

---

## 3. 合作方管理 — Partner

### 正向案例

#### PARTNER-P-001: 分页查询默认第1页
- **接口:** `GET /api/partners`
- **预期:** data.list 长度 ≤ 5

#### PARTNER-P-002: 按编号搜索
- **接口:** `GET /api/partners?code=PARTNER-001`
- **预期:** 只返回 PARTNER-001

#### PARTNER-P-003: 按名称模糊搜索
- **接口:** `GET /api/partners?name=合作方`
- **预期:** 返回所有名称含"合作方"的条目

#### PARTNER-P-004: 新增合作方
- **接口:** `POST /api/partners`
- **请求:** `{"name":"新合作方测试","contact":"测试人","phone":"13800000001"}`
- **预期:** 返回 200，code 自动生成 PARTNER-{NNN}
- **后置:** 删除此测试数据

#### PARTNER-P-005: 编辑合作方
- **接口:** `PUT /api/partners/1`
- **请求:** `{"name":"合作方A已修改","contact":"新联系人","phone":"13800000001","remark":"修改备注"}`
- **预期:** 返回 200，再次查询字段已更新
- **后置:** 恢复原始数据

#### PARTNER-P-006: 删除无订单合作方
- **前置:** 先新增一个无订单的合作方
- **接口:** `DELETE /api/partners/{newId}`
- **预期:** 返回 200

#### PARTNER-P-007: 获取合作方列表(下拉用)
- **接口:** `GET /api/partners/list`
- **预期:** 返回数组，每项含 code/name

#### PARTNER-P-008: 导出全部合作方
- **接口:** `GET /api/partners/export`
- **预期:** Content-Type=application/vnd.ms-excel，XML 含全部合作方

### 反向案例

#### PARTNER-N-001: 新增-名称已存在
- **接口:** `POST /api/partners`
- **请求:** `{"name":"合作方A","contact":"重复","phone":"13800000002"}`
- **预期:** 返回 400（名称唯一约束）

#### PARTNER-N-002: 新增-手机号非11位
- **接口:** `POST /api/partners`
- **请求:** `{"name":"短号测试","contact":"测试","phone":"12345"}`
- **预期:** 返回 400

#### PARTNER-N-003: 新增-手机号含非数字
- **接口:** `POST /api/partners`
- **请求:** `{"name":"非法手机","contact":"测试","phone":"13800abc001"}`
- **预期:** 返回 400

#### PARTNER-N-004: 删除有订单的合作方
- **接口:** `DELETE /api/partners/1`
- **预期:** 返回 400，提示"存在关联订单"

#### PARTNER-N-005: 删除不存在的合作方
- **接口:** `DELETE /api/partners/99999`
- **预期:** 返回 404

#### PARTNER-N-006: 编辑不存在的合作方
- **接口:** `PUT /api/partners/99999`
- **请求:** `{"name":"不存在"}`
- **预期:** 返回 404

#### PARTNER-N-007: 查询空结果
- **接口:** `GET /api/partners?name=不存在`
- **预期:** 返回空列表，total=0

### 边界值案例

#### PARTNER-B-001: 名称超长
- **接口:** `POST /api/partners`
- **请求:** `{"name":"a".repeat(101),"contact":"边界","phone":"13800000003"}`
- **预期:** 返回 400

#### PARTNER-B-002: 手机号11111111111
- **接口:** `POST /api/partners`
- **请求:** `{"name":"全1手机","contact":"边界","phone":"11111111111"}`
- **预期:** 返回 200（11位数字有效）

#### PARTNER-B-003: 手机号空字符串
- **接口:** `POST /api/partners`
- **请求:** `{"name":"空手机","contact":"边界","phone":""}`
- **预期:** 返回 400

#### PARTNER-B-004: 分页 pageSize=1
- **接口:** `GET /api/partners?pageSize=1`
- **预期:** 返回 1 条

#### PARTNER-B-005: 分页超出范围
- **接口:** `GET /api/partners?page=999`
- **预期:** 返回空列表

---

## 4. 报表统计 — Report

### 正向案例

#### REPORT-P-001: 订单类型统计
- **接口:** `GET /api/reports/type`
- **预期:** 返回各类型的 name/count/amount

#### REPORT-P-002: 订单类型统计-按类型筛选
- **接口:** `GET /api/reports/type?type=中间业务`
- **预期:** 只返回中间业务的数据

#### REPORT-P-003: 合作方统计
- **接口:** `GET /api/reports/partner`
- **预期:** 返回各合作方的 name/count/amount，按 amount 降序

#### REPORT-P-004: 合作方统计-按合作方筛选
- **接口:** `GET /api/reports/partner?partner=1`
- **预期:** 只返回该合作方

#### REPORT-P-005: 订单状态统计
- **接口:** `GET /api/reports/status`
- **预期:** 返回 待审核/已下单/已结清 的 count/amount

#### REPORT-P-006: 订单状态统计-按状态筛选
- **接口:** `GET /api/reports/status?status=已结清`
- **预期:** 只返回已结清统计

### 反向案例

#### REPORT-N-001: 不存在的筛选值
- **接口:** `GET /api/reports/type?type=不存在的类型`
- **预期:** 返回空列表或 0

### 边界值案例

#### REPORT-B-001: 无数据时统计
- **前置:** 清空订单表（不执行，仅设计）
- **预期:** 各统计返回 0


