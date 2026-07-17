# 管理系统原型模板

Vue 3 + Vite，Apple 风格设计。包含系统管理套件 + 产品管理示例页。

## 快速开始
```bash
cp -r /d/hermes/softwareWorkFlow/prototype ./my-project
cd my-project
npm install
npm run dev
```

## 目录结构
```
src/
├── assets/design.css        # Apple 风格 CSS（主色 #0066cc）
├── docs/design-guide.md     # 设计指南
├── store/deptStore.js       # 共享数据示例
├── views/
│   ├── Login.vue            # 登录页（验证码/密码规则/锁定/首次改密）
│   ├── Layout.vue           # 侧边栏布局
│   ├── Dashboard.vue        # 首页
│   ├── ExamplePage.vue      # CRUD 示例页（增删改查/筛选/分页/导入/导出）
│   ├── UserManage.vue       # 系统管理套件
│   ├── DepartmentManage.vue
│   ├── OrganizationManage.vue
│   ├── RoleManage.vue
│   ├── MenuManage.vue
│   ├── FuncManage.vue
│   ├── OperationLog.vue
│   └── ChangePassword.vue
└── router/index.js
```

## 创建新业务页面
1. 参考 `ExamplePage.vue` 的代码模式
2. 复制并在 router 添加路由
3. 修改字段名、接口数据、表单验证

## 技术栈
- Vue 3 (Options API)
- Vite 8
- Vue Router 4
- 无 UI 框架，纯 CSS
