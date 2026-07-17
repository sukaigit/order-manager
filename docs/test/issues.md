# 测试问题清单

## 页面结构检查结果

### 路由检查
| 路由 | 组件 | 状态 |
|:----|:----|:----:|
| /login | Login.vue | ✅ |
| /force-password | ChangePassword.vue | ✅ |
| / | Layout + children | ✅ |
| /dashboard | Dashboard.vue | ✅ |
| /orders | OrderManage.vue | ✅ |
| /partners | PartnerManage.vue | ✅ |
| /reports/type | OrderTypeReport.vue | ✅ |
| /reports/partner | PartnerReport.vue | ✅ |
| /reports/status | StatusReport.vue | ✅ |
| /users | UserManage.vue | ✅ |
| /departments | DepartmentManage.vue | ✅ |
| /organizations | OrganizationManage.vue | ✅ |
| /roles | RoleManage.vue | ✅ |
| /menus | MenuManage.vue | ✅ |
| /functions | FuncManage.vue | ✅ |
| /logs | OperationLog.vue | ✅ |
| /change-password | ChangePassword.vue | ✅ |

### API 对接检查
| 页面 | API 导入 | 后端端接口 | 状态 |
|:----|:--------:|:----------|:----:|
| Dashboard | ✅ | dashboard/orders/partners | ✅ |
| OrderManage | ✅ | orders | ✅ |
| PartnerManage | ✅ | partners | ✅ |
| OrderTypeReport | ✅ | reports/type | ✅ |
| PartnerReport | ✅ | reports/partner | ✅ |
| StatusReport | ✅ | reports/status | ✅ |
| UserManage | ✅ | users | ✅ |
| DepartmentManage | ✅ | departments | ✅ |
| OrganizationManage | ✅ | organizations/tree | ✅ |
| RoleManage | ✅ | roles | ✅ |
| MenuManage | ✅ | menus/tree | ✅ |
| FuncManage | ✅ | functions | ✅ |
| OperationLog | ✅ | logs | ✅ |
| Login | ✅ | auth/login | ✅ |

### 后端接口测试（16 端接口全部通过）
| 接口 | 状态 |
|:----|:----:|
| GET /api/partners | ✅ 200 |
| GET /api/partners/list | ✅ 200 |
| GET /api/partners/export | ✅ Excel |
| GET /api/orders | ✅ 200 |
| GET /api/orders/export | ✅ Excel |
| GET /api/dashboard | ✅ 200 |
| GET /api/reports/type/partner/status | ✅ 200 |
| GET /api/users | ✅ 200 |
| GET /api/departments | ✅ 200 |
| GET /api/roles | ✅ 200 |
| GET /api/functions | ✅ 200 |
| GET /api/logs | ✅ 200 |

### 样式检查
| 检查项 | 结果 |
|:-------|:----:|
| design.css 类名 | 63 个类定义 |
| 页面样式 | 438 处内联 style（原型阶段可接受） |
| 风格一致性 | 使用 Apple 风格设计规范 |
