# 测试问题清单

## 已修复 Bug 历史追溯

| 模块 | 问题描述 | 发现阶段 | 修复方式 | 状态 |
|:----|:---------|:--------:|:---------|:----:|
| 全局 | 数据库编码 `utf8mb4` 导致中文乱码 | 全栈联调 | JDBC URL 改为 `characterEncoding=utf8`，重建数据 | ✅ 已修复 |
| 合作方管理 | 新增合作方未自动生成编号，`code=null` 导致 500 | E2E 集成测试 | `PartnerService.create()` 增加 `selectMaxId` + 自动编号 | ✅ 已修复 |
| 合作方管理 | 合作方名称重复时返回 HTTP 500（Spring 默认错误） | E2E 集成测试 | 后端返回 400 + 友好提示 | ✅ 已修复 |
| 合作方管理 | 删除有订单合作方返回 HTTP 200（body 含 code=400） | E2E 集成测试 | 改为 `ResponseEntity.status(400)` | ✅ 已修复 |
| 订单管理 | 新增订单未生成 `code` 和 `orderTime`，`null` 导致 500 | E2E 集成测试 | `OrderService.create()` 增加自动编号+时间填充 | ✅ 已修复 |
| 订单管理 | `PUT /api/orders/{id}` 未校验状态，已结清订单可被编辑 | 反向案例测试 | `OrderController.update()` 增加 `status=待审核` 检查 | ✅ 已修复 |
| 首页 | `recentOrders` 后端返回 `partnerId` 和 `orderTime`，前端期望 `partner` 和 `date` | 全栈联调 | `DashboardService` 映射字段并增加 `statusCls` | ✅ 已修复 |
| 订单管理 | `POST /api/orders` 请求体字段名 `partner` 不匹配后端 `partnerId` | E2E 集成测试 | 前端使用 `partnerId` 参数 | ✅ 已修复 |
| 报表统计 | 报表筛选中文字段在 curl 测试时编码异常 | E2E 集成测试 | 使用英文标识符测试，前端页面正常 | ✅ 已修复 |
| 报表统计 | 3个报表页后端返回 `{data:{data:[...]}}` 双层嵌套，前端解包不对导致表格空白 | 用户测试 | 前端改为 `res.data.data || res.data` | ✅ 已修复 |

## 待改进项

| 模块 | 建议 | 优先级 | 状态 |
|:----|:------|:------:|:----:|
| 全局 | 错误码应统一返回标准的 HTTP 状态码（如 400/404 等），而非全返回 200 | 低 | ⏳ 待处理 |
| 全局 | 接口字段校验 (Order金额/Partner手机号格式) | 中 | ✅ 已修复 |
| 系统管理 | 机构/菜单/功能 种子数据 | 中 | ✅ 已修复 |
