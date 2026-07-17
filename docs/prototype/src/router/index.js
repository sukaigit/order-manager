import { createRouter, createWebHistory } from 'vue-router'

import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import Dashboard from '../views/Dashboard.vue'
import UserManage from '../views/UserManage.vue'
import DepartmentManage from '../views/DepartmentManage.vue'
import RoleManage from '../views/RoleManage.vue'
import MenuManage from '../views/MenuManage.vue'
import FuncManage from '../views/FuncManage.vue'
import ExamplePage from '../views/ExamplePage.vue'
import OperationLog from '../views/OperationLog.vue'
import OrganizationManage from '../views/OrganizationManage.vue'
import ChangePassword from '../views/ChangePassword.vue'

const routes = [
  { path: '/login', component: Login },
  { path: '/force-password', component: ChangePassword },
  {
    path: '/',
    component: Layout,
    children: [
      { path: '', redirect: '/dashboard' },
      { path: 'dashboard', component: Dashboard },
      { path: 'users', component: UserManage },
      { path: 'departments', component: DepartmentManage },
      { path: 'organizations', component: OrganizationManage },
      { path: 'roles', component: RoleManage },
      { path: 'menus', component: MenuManage },
      { path: 'functions', component: FuncManage },
      { path: 'logs', component: OperationLog },
      { path: 'example', component: ExamplePage },
      { path: 'change-password', component: ChangePassword },
    ]
  }
]

export default createRouter({ history: createWebHistory(), routes })
