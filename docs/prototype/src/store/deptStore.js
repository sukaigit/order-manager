// 共享部门数据，部门管理和用户管理共用同一个数据源
const depts = [
  {code:'DEPT_RD',name:'研发部',remark:''},
  {code:'DEPT_SALES',name:'销售部',remark:''},
  {code:'DEPT_FINANCE',name:'财务部',remark:''},
  {code:'DEPT_HR',name:'人事部',remark:''},
  {code:'DEPT_AFTERSALE',name:'售后部',remark:''},
  {code:'DEPT_MARKETING',name:'市场部',remark:''},
  {code:'DEPT_ADMIN',name:'行政部',remark:''},
  {code:'DEPT_LOGISTICS',name:'物流部',remark:''},
]

export function getDepts() { return depts }
export function addDept(d) { depts.push(d) }
export function updateDept(code, data) { const i=depts.findIndex(x=>x.code===code); if(i>=0) Object.assign(depts[i], data) }
export function deleteDept(code) { const i=depts.findIndex(x=>x.code===code); if(i>=0) depts.splice(i,1) }
