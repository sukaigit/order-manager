<template>
  <div class="page-body">
    <div style="display:grid;grid-template-columns:repeat(4,1fr);gap:16px;margin-bottom:24px">
      <div class="card"><div class="card-title">累计订单数</div><div style="font-size:32px;font-weight:700;color:var(--color-primary)">128</div></div>
      <div class="card"><div class="card-title">累计订单金额</div><div style="font-size:32px;font-weight:700;color:var(--color-primary)">¥1,286,500.00</div></div>
      <div class="card"><div class="card-title">累计已结清订单数</div><div style="font-size:32px;font-weight:700;color:#34c759">48</div></div>
      <div class="card"><div class="card-title">累计已结清订单金额</div><div style="font-size:32px;font-weight:700;color:#34c759">¥583,000.00</div></div>
    </div>
    <div class="card" style="padding:16px;margin-bottom:24px">
      <div class="card-title" style="padding:0;margin-bottom:12px">订单分布</div>
      <div style="font-size:13px">
        <div style="display:flex;align-items:center;padding:10px 0;border-bottom:1px solid var(--color-border-light)">
          <span style="width:80px;font-weight:600">中间业务</span>
          <span style="width:80px;color:var(--color-text-muted)">48 单</span>
          <div style="flex:1;height:14px;background:var(--color-bg);border-radius:4px;overflow:hidden;margin:0 12px"><div style="height:100%;width:100%;background:var(--color-primary);border-radius:4px"></div></div>
          <span style="width:90px;text-align:right;font-weight:600">¥486,000</span>
        </div>
        <div style="display:flex;align-items:center;padding:10px 0;border-bottom:1px solid var(--color-border-light)">
          <span style="width:80px;font-weight:600">数据开发</span>
          <span style="width:80px;color:var(--color-text-muted)">32 单</span>
          <div style="flex:1;height:14px;background:var(--color-bg);border-radius:4px;overflow:hidden;margin:0 12px"><div style="height:100%;width:67%;background:#34c759;border-radius:4px"></div></div>
          <span style="width:90px;text-align:right;font-weight:600">¥320,000</span>
        </div>
        <div style="display:flex;align-items:center;padding:10px 0">
          <span style="width:80px;font-weight:600">应用开发</span>
          <span style="width:80px;color:var(--color-text-muted)">40 单</span>
          <div style="flex:1;height:14px;background:var(--color-bg);border-radius:4px;overflow:hidden;margin:0 12px"><div style="height:100%;width:83%;background:#ff9500;border-radius:4px"></div></div>
          <span style="width:90px;text-align:right;font-weight:600">¥410,000</span>
        </div>
      </div>
    </div>
    <div style="display:grid;grid-template-columns:2fr 1fr;gap:16px;margin-bottom:24px">
      <div class="card"><div class="card-title">近期订单</div>
      <table class="data-table"><tr><th>订单编号</th><th>订单类型</th><th>合作方</th><th>金额</th><th>状态</th><th>时间</th></tr>
        <tr v-for="o in recentOrders" :key="o.code"><td style="font-size:12px;color:var(--color-text-muted)">{{ o.code }}</td><td>{{ o.type }}</td><td>{{ o.partner }}</td><td>¥{{ o.amount.toLocaleString() }}</td><td><span class="badge" :class="'badge-'+o.statusCls">{{ o.status }}</span></td><td>{{ o.date }}</td></tr>
      </table>
      </div>
      <div class="card"><div class="card-title">合作方排名</div>
        <div v-for="(p,i) in partnerRank" :key="p.name" style="display:flex;justify-content:space-between;padding:8px 0;border-bottom:1px solid var(--color-border-light);font-size:13px">
          <span>{{ i+1 }}. {{ p.name }}</span><span style="color:var(--color-text-muted)">{{ p.count }} 单 / ¥{{ p.amount.toLocaleString() }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data: () => ({
    recentOrders: [
      {code:'ORD-20240606-005',type:'中间业务',partner:'合作方A',amount:86000,status:'待审核',statusCls:'pending',date:'2024-06-06 14:30'},
      {code:'ORD-20240606-004',type:'数据开发',partner:'合作方B',amount:120000,status:'已下单',statusCls:'active',date:'2024-06-06 11:20'},
      {code:'ORD-20240605-003',type:'应用开发',partner:'合作方C',amount:95000,status:'已结清',statusCls:'approved',date:'2024-06-05 16:45'},
      {code:'ORD-20240605-002',type:'中间业务',partner:'合作方A',amount:45000,status:'已结清',statusCls:'approved',date:'2024-06-05 10:00'},
      {code:'ORD-20240604-001',type:'数据开发',partner:'合作方D',amount:200000,status:'已下单',statusCls:'active',date:'2024-06-04 09:15'},
    ]
  }),
  computed:{
    partnerRank(){const t={};const all=[{partner:'合作方A',amount:280000},{partner:'合作方A',amount:45000},{partner:'合作方B',amount:120000},{partner:'合作方B',amount:78000},{partner:'合作方C',amount:95000},{partner:'合作方C',amount:165000},{partner:'合作方D',amount:200000},{partner:'合作方E',amount:32000},{partner:'合作方F',amount:95000},{partner:'合作方G',amount:55000},{partner:'合作方H',amount:130000}];all.forEach(o=>{if(!t[o.partner])t[o.partner]={name:o.partner,count:0,amount:0};t[o.partner].count++;t[o.partner].amount+=o.amount});return Object.values(t).sort((a,b)=>b.amount-a.amount)}
  }
}
</script>