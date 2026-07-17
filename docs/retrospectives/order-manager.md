# 订单管理系统 — 开发回顾

**项目:** 订单管理系统 (order-manager)
**日期:** 2026-07-17
**耗时:** 约 8 小时

---

## 做了什么

完成全栈订单管理系统（Spring Boot 3 + Vue 3 + MySQL 5.7）：
- 12 个 OpenSpec spec 文件（4 业务 + 8 系统管理）
- 后端 49 个 Java 文件，12 个 Controller
- 前端 15 个页面，全部对接真实 API
- 57 条集成测试 + 50 条用户测试
- 10 个 bug 发现并修复
- PR 已合并到 master

---

## 踩坑记录

| # | 问题 | 方案 | 是否回流 |
|:-|:-----|:-----|:--------|
| 1 | **OpenSpec spec 覆盖不全** — 只写了 4 个业务模块的 spec，系统管理 8 个模块没写进 spec，导致后续架构/数据库设计遗漏 | 流程加门禁：router vs spec 逐页对照检查 | ☐ 回流 / ☐ 不留 |
| 2 | **mock 数据未清除** — Dashboard/OrderManage 等多页面 template 中硬编码数据未删除，首页一直显示 mock 数据 | 流程加规则：子代理转换 API 时，必须删除 data() 中的 mock 数组和 template 中的硬编码数值 | ☐ 回流 / ☐ 不留 |
| 3 | **vite.config.js 缺少 API 代理** — 前端请求 `/api` 时没代理到后端 8080，导致 API 调用返回 Vite HTML 页面 | 流程中模板的 vite.config.js 应预配好 proxy | ☐ 回流 / ☐ 不留 |
| 4 | **API 路径不一致** — 子代理用了 `api.get('/api/orders')` 但 baseURL 已是 `/api`，导致双 `/api` | 子代理模板应明确 baseURL 配置，避免路径重复 | ☐ 回流 / ☐ 不留 |
| 5 | **git stash 冲突破坏代码** — 从 feat 切 master 再切回时，stash pop 导致 13 个 Vue 文件出现冲突标记和重复代码段 | 切换分支前先提交/清理，避免 stash 冲突 | ☐ 回流 / ☐ 不留 |
| 6 | **测试数据未清理干净** — Playwright/API 测试后残留垃圾数据（测试订单/合作方），用户验收时数据不干净 | 流程步骤 4 强调：只保留 init.sql 种子数据，业务数据由用户验收时新增 | ☐ 回流 / ☐ 不留 |
| 7 | **SVG 自闭合标签编译报错** — Vue SFC 编译器不识别 `<path/>` 等 SVG 自闭合标签，导致 OrderManage 页面崩溃 | 原型模板中避免使用 SVG 路径图标，改用 emoji 或文字 | ☐ 回流 / ☐ 不留 |
| 8 | **报表后端返回双层 data** — `{data:{data:[...]}}` 前端解包不对导致表格空白 | 后端返回 List 而非 Map，或前端加 `res.data.data || res.data` 容错 | ☐ 回流 / ☐ 不留 |

---

## 下次改进

1. **原型模板中系统管理 seed 数据直接就写好**，用户确认时只需微调
2. **每个阶段结束做 router vs spec vs API vs test 覆盖检查**
3. **切换分支前先 `git stash push -u` + 提交 `start.sh` 等基础设施文件到 git**
4. **vite.config.js 的 proxy 直接写在模板中**
5. **SVG 图标统一用 emoji 或纯文字替代，避免 Vue 编译问题**
