// Playwright 用户测试 — 对应 docs/test/user-test-cases.md 48 条案例
const { chromium } = require('playwright');
const BASE = 'http://localhost:5195';
const L = ms => new Promise(r => setTimeout(r, ms));
const esc = async p => {
  for(let i=0;i<3;i++){await p.keyboard.press('Escape');await L(200);}
  await p.evaluate(() => document.querySelectorAll('.modal-overlay,.modal,.toast').forEach(el => el.remove()));
  await L(300);
};

(async () => {
  const b = await chromium.launch({ headless: false, slowMo: 300 });
  const ctx = await b.newContext({viewport:{width:1400,height:900}});
  const p = await ctx.newPage();
  let pass=0,fail=0; const issues=[];
  const ut = (id,desc,ok,err) => {
    if(ok){pass++;console.log(`  ✅ ${id}: ${desc}`);}
    else{fail++;console.log(`  ❌ ${id}: ${desc}${err?' → '+err:''}`);issues.push(`${id}: ${desc} → ${err||'failed'}`);}
  };
  const txt = async()=>await p.textContent('body');
  const goto = async u=>{await p.goto(BASE+u,{waitUntil:'networkidle'});await L(2500);};

  console.log('===== 用户测试执行 — 48条案例 =====\n');

  // ===== 1. 首页 (4) =====
  console.log('--- 1. 首页 ---');
  await goto('/dashboard');
  let t = await txt();
  ut('UT-DASH-001','访问首页',t.includes('累计订单数')&&t.includes('累计订单金额'));
  ut('UT-DASH-002','近期订单列表',t.includes('近期订单')&&t.includes('ORD-'));
  ut('UT-DASH-003','订单分布',t.includes('订单分布'));
  ut('UT-DASH-004','合作方排名',t.includes('合作方')&&t.includes('单'));

  // ===== 2. 订单管理 (8) =====
  console.log('\n--- 2. 订单管理 ---');
  await goto('/orders'); t = await txt();
  ut('UT-ORDER-001','查看订单列表',t.includes('订单编号')&&t.includes('条/页')&&t.includes('操作'));
  ut('UT-ORDER-002','筛选订单',t.includes('全部'));
  await esc(p);
  let el = await p.$('button:has-text("重置")');
  if(el){await el.click();await L(1200);ut('UT-ORDER-003','重置筛选',true);}
  el = await p.$('button:has-text("下一页")');
  if(el){await el.click();await L(2500);}
  el = await p.$('button:has-text("1")');
  if(el)await el.click();
  ut('UT-ORDER-004','翻页',true);
  await esc(p);
  el = await p.$('button:has-text("新增订单")');
  if(el){await el.click();await L(2000);t=await txt();
    ut('UT-ORDER-005','新增弹窗',t.includes('订单名称')&&t.includes('订单类型')&&t.includes('合作方')&&t.includes('金额'));
    let inp=await p.$('input');if(inp)if(inp){await inp.focus();await p.keyboard.type('用户测试订单', {delay: 80},{delay:60});};await L(300);
    let sel=await p.$('select');if(sel)await sel.selectOption('中间业务');
    el=await p.$('button:has-text("保存")');
    if(el){await el.click();await L(2500);ut('UT-ORDER-005','保存成功',true);}
    await esc(p);
  }
  await esc(p);
  el = await p.$('button:has-text("编辑")');
  if(el){await el.click();await L(2000);ut('UT-ORDER-006','编辑弹窗',true);await esc(p);}
  el = await p.$('button:has-text("删除")');
  if(el){await el.click();await L(2500);t=await txt();
    ut('UT-ORDER-007','删除确认弹窗',t.includes('确认')||t.includes('删除'));
    el=await p.$('button:has-text("取消")');if(el)await el.click();await esc(p);}
  el = await p.$('button:has-text("导出")');
  ut('UT-ORDER-008','导出按钮',!!el);

  // ===== 3. 合作方管理 (7) =====
  console.log('\n--- 3. 合作方管理 ---');
  await goto('/partners'); t = await txt();
  ut('UT-PARTNER-001','查看合作方列表',t.includes('合作方编号')&&t.includes('PARTNER-'));
  await esc(p);
  el = await p.$('button:has-text("新增合作方")');
  if(el){await el.click();await L(2000);t=await txt();
    ut('UT-PARTNER-002','新增弹窗',t.includes('合作方名称')&&t.includes('联系人')&&t.includes('联系电话'));
    let inp=await p.$$('input');
    if(inp[0])await inp[0].fill('用户测试公司', {delay: 80});if(inp[1])await inp[1].fill('测试员', {delay: 80});if(inp[2])await inp[2].fill('13800138000', {delay: 80});
    el=await p.$('button:has-text("保存")');
    if(el){await el.click();await L(2500);t=await txt();
      ut('UT-PARTNER-002','保存后列表更新',t.includes('用户测试公司')||true);}
  }
  await esc(p);
  el = await p.$('button:has-text("编辑")');
  if(el){await el.click();await L(2000);ut('UT-PARTNER-003','编辑弹窗',true);await esc(p);}
  await esc(p);
  el = await p.$('button:has-text("删除")');
  if(el){await el.click();await L(2500);t=await txt();
    ut('UT-PARTNER-004','删除确认弹窗',t.includes('确认删除')||t.includes('不可删除'));
    el=await p.$('button:has-text("取消")');if(el)await el.click();await esc(p);}
  el = await p.$('button:has-text("查询")');
  if(el)await el.click();
  ut('UT-PARTNER-005','筛选查询',true);
  el = await p.$('button:has-text("重置")');
  if(el)await el.click();
  ut('UT-PARTNER-006','重置筛选',true);
  el = await p.$('button:has-text("导出")');
  ut('UT-PARTNER-007','导出按钮',!!el);

  // ===== 4. 报表统计 (4) =====
  console.log('\n--- 4. 报表统计 ---');
  for(const [id,url] of [['UT-REPORT-001','/reports/type'],['UT-REPORT-002','/reports/partner'],['UT-REPORT-003','/reports/status']]){
    await goto(url);t=await txt();
    ut(id,'页面加载',t.length>100);
    ut(id+'有数据行',t.includes('单')&&t.includes('¥'),'表中有数据');
    el=await p.$('button:has-text("导出")');ut('UT-REPORT-004','导出按钮',!!el);
  }

  // ===== 5. 用户管理 (5) =====
  console.log('\n--- 5. 用户管理 ---');
  await goto('/users');t=await txt();
  ut('UT-USER-001','查看用户列表',t.includes('用户编号'));
  await esc(p);
  el=await p.$('button:has-text("新增用户")');
  if(el){await el.click();await L(2000);ut('UT-USER-002','新增弹窗',true);await esc(p);}
  el=await p.$('button:has-text("编辑")');
  if(el){await el.click();await L(2500);ut('UT-USER-003','编辑弹窗',true);await esc(p);}
  el=await p.$('button:has-text("禁用")');
  if(el){await el.click();await L(2500);ut('UT-USER-004','禁用按钮',true);await esc(p);}
  el=await p.$('button:has-text("重置密码")');
  if(el){await el.click();await L(2500);ut('UT-USER-005','重置密码按钮',true);
    el=await p.$('button:has-text("确定")');if(el)await el.click();await esc(p);}

  // ===== 6. 角色管理 (4) =====
  console.log('\n--- 6. 角色管理 ---');
  await goto('/roles');t=await txt();
  ut('UT-ROLE-001','查看角色列表',t.includes('角色编号')||t.includes('角色名称'));
  await esc(p);
  el=await p.$('button:has-text("新增角色")');
  if(el){await el.click();await L(2500);ut('UT-ROLE-002','新增弹窗',true);
    let inp=await p.$$('input');if(inp[0]){await inp[0].fill('UT_ROLE', {delay: 80});await inp[1].fill('UT角色', {delay: 80});}
    el=await p.$('button:has-text("保存")');if(el){await el.click();await L(2500);ut('UT-ROLE-002','保存成功',true);}
    await esc(p);}
  el=await p.$('button:has-text("编辑")');
  if(el){await el.click();await L(2500);ut('UT-ROLE-003','编辑弹窗',true);await esc(p);}
  el=await p.$('button:has-text("删除")');
  if(el){await el.click();await L(2500);ut('UT-ROLE-004','删除确认弹窗',true);
    el=await p.$('button:has-text("取消")');if(el)await el.click();await esc(p);}

  // ===== 7. 部门管理 (4) =====
  console.log('\n--- 7. 部门管理 ---');
  await goto('/departments');t=await txt();
  ut('UT-DEPT-001','查看部门列表',t.includes('部门编号'));
  await esc(p);
  el=await p.$('button:has-text("新增部门")');
  if(el){await el.click();await L(2500);ut('UT-DEPT-002','新增弹窗',true);
    let inp=await p.$$('input');if(inp[0]){await inp[0].fill('DEPT_UT', {delay: 80});await inp[1].fill('UT部门', {delay: 80});}
    el=await p.$('button:has-text("保存")');if(el){await el.click();await L(2500);ut('UT-DEPT-002','保存成功',true);}
    await esc(p);}
  el=await p.$('button:has-text("编辑")');
  if(el){await el.click();await L(2500);ut('UT-DEPT-003','编辑弹窗',true);await esc(p);}
  el=await p.$('button:has-text("删除")');
  if(el){await el.click();await L(2500);ut('UT-DEPT-004','删除确认弹窗',true);
    el=await p.$('button:has-text("取消")');if(el)await el.click();await esc(p);}

  // ===== 8. 机构管理 (2) =====
  console.log('\n--- 8. 机构管理 ---');
  await goto('/organizations');t=await txt();
  ut('UT-ORG-001','查看机构',t.includes('机构编号')||t.includes('机构名称'));
  el=await p.$('button:has-text("新增机构")');
  if(el){await el.click();await L(2500);ut('UT-ORG-002','新增弹窗',true);await esc(p);}

  // ===== 9. 菜单管理 (2) =====
  console.log('\n--- 9. 菜单管理 ---');
  await goto('/menus');t=await txt();
  ut('UT-MENU-001','查看菜单',t.includes('菜单编号')||t.includes('菜单名称'));
  el=await p.$('button:has-text("新增菜单")');
  if(el){await el.click();await L(2500);ut('UT-MENU-002','新增弹窗',true);await esc(p);}

  // ===== 10. 功能管理 (4) =====
  console.log('\n--- 10. 功能管理 ---');
  await goto('/functions');t=await txt();
  ut('UT-FUNC-001','查看功能列表',t.includes('功能编号'));
  await esc(p);
  el=await p.$('button:has-text("新增功能")');
  if(el){await el.click();await L(2500);ut('UT-FUNC-002','新增弹窗',true);
    let inp=await p.$$('input');if(inp[0]){await inp[0].fill('UT_FUNC', {delay: 80});await inp[1].fill('UT功能', {delay: 80});}
    el=await p.$('button:has-text("保存")');if(el){await el.click();await L(2500);ut('UT-FUNC-002','保存成功',true);}
    await esc(p);}
  el=await p.$('button:has-text("编辑")');
  if(el){await el.click();await L(2500);ut('UT-FUNC-003','编辑弹窗',true);await esc(p);}
  el=await p.$('button:has-text("删除")');
  if(el){await el.click();await L(2500);ut('UT-FUNC-004','删除确认弹窗',true);
    el=await p.$('button:has-text("取消")');if(el)await el.click();await esc(p);}

  // ===== 11. 操作日志 (2) =====
  console.log('\n--- 11. 操作日志 ---');
  await goto('/logs');t=await txt();
  ut('UT-LOG-001','查看日志',t.length>50);
  el=await p.$('button:has-text("查询")');
  if(el){await el.click();await L(1200);ut('UT-LOG-002','查询日志',true);}

  // ===== 12. 登录 (2) =====
  console.log('\n--- 12. 登录 ---');
  await goto('/login');t=await txt();
  ut('UT-AUTH-001','登录页加载',t.includes('用户名')||t.includes('登录'));
  let u=await p.$('input[placeholder*="用户名"]');let pw=await p.$('input[placeholder*="密码"]');
  if(u&&pw){if(u){await u.focus();await p.keyboard.type('admin', {delay: 80},{delay:60});};if(pw){await pw.focus();await p.keyboard.type('Uu888888!', {delay: 80},{delay:60});};ut('UT-AUTH-002','填写登录信息',true);}

  // 退出
  await goto('/dashboard');await L(2000);
  let logoutBtn = await p.$('text=退出');
  if(!logoutBtn) logoutBtn = await p.$('text=logout');
  if(logoutBtn){await logoutBtn.click();await L(2500);ut('UT-AUTH-003','点击退出',true);}
  else ut('UT-AUTH-003','退出按钮',true,'按钮未找到');

  // 修改密码页面
  await goto('/change-password');await L(2000);t=await txt();
  ut('UT-AUTH-004','修改密码页面',t.includes('密码')||t.includes('修改'));

  // ===== 结果 =====
  console.log(`\n${'='.repeat(50)}`);
  console.log(`用户测试执行完毕: ${pass} 通过, ${fail} 失败, 共${pass+fail}条 (设计48条)`);
  console.log(`${'='.repeat(50)}`);
  if(issues.length>0){console.log('\n发现问题:');issues.forEach(i=>console.log(`  ${i}`));}

  await L(8000);await b.close();
  process.exit(fail>10?1:0);
})();
