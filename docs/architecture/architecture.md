# 订单管理系统 — 系统架构

## 技术栈
- 后端: Java 17 + Spring Boot 3 + MyBatis
- 前端: Vue 3 + Vite 8
- 数据库: MySQL 5.7
- 构建: Maven (D:\softwareInstall\apache-maven-3.9.16)
- 前端构建: npm

## 分层结构
```
┌─────────────────────────────┐
│    前端 (Vue 3 + Vite)      │
│  docs/prototype/            │
├─────────────────────────────┤
│  REST API (JSON)            │
├─────────────────────────────┤
│  Controller Layer           │
│  com.ordermanager.controller│
├─────────────────────────────┤
│  Service Layer              │
│  com.ordermanager.service   │
├─────────────────────────────┤
│  Mapper Layer (MyBatis)     │
│  com.ordermanager.mapper    │
├─────────────────────────────┤
│  MySQL 5.7                  │
│  业务表 + 系统管理表         │
└─────────────────────────────┘
```

## 模块划分

### 业务模块
| 模块 | 页面路由 | Controller | 说明 |
|:----|:---------|:-----------|:-----|
| 首页 | /dashboard | DashboardController | 统计卡片/订单分布/合作方排名/近期订单 |
| 订单管理 | /orders | OrderController | CRUD + 筛选 + 导出 |
| 合作方管理 | /partners | PartnerController | CRUD + 筛选 + 导出 |
| 订单类型统计 | /reports/type | ReportController | 按类型统计 |
| 合作方统计 | /reports/partner | ReportController | 按合作方统计 |
| 订单状态统计 | /reports/status | ReportController | 按状态统计 |

### 系统管理模块
| 模块 | 页面路由 | Controller | 说明 |
|:----|:---------|:-----------|:-----|
| 用户管理 | /users | UserController | RBAC 用户CRUD |
| 部门管理 | /departments | DeptController | 部门CRUD |
| 机构管理 | /organizations | OrgController | 5级树形机构 |
| 角色管理 | /roles | RoleController | 角色+权限分配 |
| 菜单管理 | /menus | MenuController | 菜单树CRUD |
| 功能管理 | /functions | FuncController | 功能权限CRUD |
| 操作日志 | /logs | LogController | 操作日志查询 |

## 数据流
1. 前端 Vue Router → 页面组件 → axios 调用 REST API
2. Controller 接收请求 → 参数校验 → 调用 Service
3. Service 处理业务逻辑 → 调用 Mapper
4. Mapper (MyBatis) → SQL → MySQL

## 数据库表总览
| 表名 | 说明 | 类型 |
|:----|:-----|:----|
| tb_user | 用户表 | 系统管理 |
| tb_department | 部门表 | 系统管理 |
| tb_organization | 机构表 | 系统管理 |
| tb_role | 角色表 | 系统管理 |
| tb_menu | 菜单表 | 系统管理 |
| tb_function | 功能权限表 | 系统管理 |
| tb_operation_log | 操作日志表 | 系统管理 |
| tb_partner | 合作方表 | 业务 |
| tb_order | 订单表 | 业务 |

## 关键决策
- ADR-001: 合作方编号使用 PARTNER- 前缀格式
- 订单金额使用 decimal(12,2) 避免浮点精度问题
- 前端导出使用浏览器端 XML Excel 生成（零依赖）
- 系统管理模块复用原型模板中的前端页面，后端新写 API
