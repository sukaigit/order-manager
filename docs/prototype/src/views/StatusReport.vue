<template>
  <div class="page-body">
    <div class="filter-bar">
      <div class="form-group"><label class="form-label">订单状态</label><select class="form-select" v-model="fStatus"><option>全部</option><option>待审核</option><option>已下单</option><option>已结清</option></select></div>
      <button class="btn btn-primary" @click="query">查询</button>
      <button class="btn btn-secondary" @click="resetQuery">重置</button>
      <div style="flex:1"></div>
      <button class="btn btn-secondary" @click="doExport">导出</button>
    </div>
    <table class="data-table">
      <tr><th>订单状态</th><th>订单数量</th><th>订单金额</th></tr>
      <tr v-for="s in stats" :key="s.name"><td>{{ s.name }}</td><td>{{ s.count }} 单</td><td>¥{{ s.amount.toLocaleString() }}</td></tr>
    </table>
  </div>
</template>
<script>
export default {
  data:()=>({fStatus:'全部',orders:[{amount:86000,status:'待审核'},{amount:120000,status:'已下单'},{amount:95000,status:'已结清'},{amount:45000,status:'已结清'},{amount:200000,status:'已下单'},{amount:32000,status:'待审核'},{amount:180000,status:'已结清'},{amount:95000,status:'已下单'},{amount:55000,status:'已结清'},{amount:130000,status:'待审核'},{amount:78000,status:'已结清'},{amount:165000,status:'已结清'}]}),
  computed:{
    fl(){return this.fStatus==='全部'?this.orders:this.orders.filter(o=>o.status===this.fStatus)},
    stats(){const t={};this.fl.forEach(o=>{if(!t[o.status])t[o.status]={name:o.status,count:0,amount:0};t[o.status].count++;t[o.status].amount+=o.amount});return Object.values(t)},
  },
  methods:{
    query(){},resetQuery(){this.fStatus='全部'},
    doExport(){const d=[['订单状态','订单数量','订单金额']];this.stats.forEach(s=>d.push([s.name,s.count+' 单','¥'+s.amount.toLocaleString()]));const x='<?xml version="1.0" encoding="UTF-8"?><?mso-application progid="Excel.Sheet"?><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"><Worksheet ss:Name="订单状态统计"><Table>'+d.map(r=>'<Row>'+r.map(c=>'<Cell><Data ss:Type="String">'+String(c).replace(/&/g,'&amp;').replace(/</g,'&lt;')+'</Data></Cell>').join('')+'</Row>').join('')+'</Table></Worksheet></Workbook>';const b=new Blob([x],{type:'application/vnd.ms-excel'}),u=URL.createObjectURL(b),a=document.createElement('a');a.href=u;a.download='订单状态统计_'+new Date().toISOString().slice(0,10)+'.xls';a.click();URL.revokeObjectURL(u)},
  }
}
</script>