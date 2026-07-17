# 订单管理系统 — 实施 Plan

## 依赖图
```
系统管理表(tb_user/tb_role/...) → 系统管理 API → 前端原型(已有)
业务表(tb_partner/tb_order) → 业务 API → 前端对接
```

## Task 1: 项目脚手架 + 数据库
- **目标:** 创建 Spring Boot 项目，执行 DDL 建全部表
- **文件:** pom.xml, application.yml, sql/init.sql
- **路径:** com.ordermanager
- **预计耗时:** 20min
- **验证:** `mysql -u root -p -e "USE order_manager; SHOW TABLES;"`

## Task 2: 系统管理 — 用户管理 API
- **目标:** UserController + Service + Mapper，含分页/新增/编辑/删除/锁定/解锁/重置密码
- **文件:** controller/UserController.java, service/UserService.java, mapper/UserMapper.java
- **对接前端:** docs/prototype/src/views/UserManage.vue（已有原型）
- **预计耗时:** 25min

## Task 3: 系统管理 — 部门 + 机构 API
- **目标:** DeptController + OrgController
- **文件:** DeptController.java, OrgController.java + Service + Mapper
- **预计耗时:** 20min

## Task 4: 系统管理 — 角色 + 菜单 + 功能 + 日志 API
- **目标:** RoleController + MenuController + FuncController + LogController
- **文件:** controller/ + service/ + mapper/ 对应文件
- **预计耗时:** 30min

## Task 5: 合作方 CRUD API
- **目标:** PartnerController + Service + Mapper
- **对接API:** GET/POST/PUT/DELETE /api/partners
- **预计耗时:** 20min

## Task 6: 订单 CRUD API
- **目标:** OrderController + Service + Mapper
- **对接API:** GET/POST/PUT/DELETE /api/orders
- **预计耗时:** 25min

## Task 7: 首页 + 报表统计 API
- **目标:** DashboardController + ReportController
- **预计耗时:** 15min

## Task 8: 前端对接 — 业务页面
- **目标:** OrderManage.vue + PartnerManage.vue + Dashboard.vue + 3 Report 页面
- **操作:** 静态 data 替换为 axios API 调用
- **预计耗时:** 25min
