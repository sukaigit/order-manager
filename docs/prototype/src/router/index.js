import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import Dashboard from '../views/Dashboard.vue'
import OrderManage from '../views/OrderManage.vue'
import PartnerManage from '../views/PartnerManage.vue'
import OrderTypeReport from '../views/OrderTypeReport.vue'
import PartnerReport from '../views/PartnerReport.vue'
import StatusReport from '../views/StatusReport.vue'
import UserManage from '../views/UserManage.vue'
import DepartmentManage from '../views/DepartmentManage.vue'
import RoleManage from '../views/RoleManage.vue'
import MenuManage from '../views/MenuManage.vue'
import FuncManage from '../views/FuncManage.vue'
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
      { path: 'orders', component: OrderManage },
      { path: 'partners', component: PartnerManage },
      { path: 'reports/type', component: OrderTypeReport },
      { path: 'reports/partner', component: PartnerReport },
      { path: 'reports/status', component: StatusReport },
      { path: 'users', component: UserManage },
      { path: 'departments', component: DepartmentManage },
      { path: 'organizations', component: OrganizationManage },
      { path: 'roles', component: RoleManage },
      { path: 'menus', component: MenuManage },
      { path: 'functions', component: FuncManage },
      { path: 'logs', component: OperationLog },
      { path: 'change-password', component: ChangePassword },
    ]
  }
]
export default createRouter({ history: createWebHistory(), routes })
