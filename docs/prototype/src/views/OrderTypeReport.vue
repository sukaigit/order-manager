<template>
  <div class="page-body">
    <div class="filter-bar">
      <div class="form-group"><label class="form-label">订单类型</label><select class="form-select" v-model="fType"><option>全部</option><option>中间业务</option><option>数据开发</option><option>应用开发</option></select></div>
      <button class="btn btn-primary" @click="query">查询</button>
      <button class="btn btn-secondary" @click="resetQuery">重置</button>
      <div style="flex:1"></div>
      <button class="btn btn-secondary" @click="doExport">导出</button>
    </div>
    <table class="data-table">
      <tr><th>订单类型</th><th>订单数量</th><th>订单金额</th></tr>
      <tr v-for="t in stats" :key="t.name"><td>{{ t.name }}</td><td>{{ t.count }} 单</td><td>¥{{ t.amount.toLocaleString() }}</td></tr>
    </table>
  </div>
</template>
<script>
export default {
  data:()=>({fType:'全部',orders:[{type:'中间业务',amount:86000},{type:'数据开发',amount:120000},{type:'应用开发',amount:95000},{type:'中间业务',amount:45000},{type:'数据开发',amount:200000},{type:'中间业务',amount:32000},{type:'应用开发',amount:180000},{type:'数据开发',amount:95000},{type:'中间业务',amount:55000},{type:'应用开发',amount:130000},{type:'中间业务',amount:78000},{type:'数据开发',amount:165000}]}),
  computed:{
    fl(){return this.fType==='全部'?this.orders:this.orders.filter(o=>o.type===this.fType)},
    stats(){const t={};this.fl.forEach(o=>{if(!t[o.type])t[o.type]={name:o.type,count:0,amount:0};t[o.type].count++;t[o.type].amount+=o.amount});return Object.values(t)},
  },
  methods:{
    query(){},resetQuery(){this.fType='全部'},
    doExport(){const d=[['订单类型','订单数量','订单金额']];this.stats.forEach(t=>d.push([t.name,t.count+' 单','¥'+t.amount.toLocaleString()]));const r='<table>'+d.map(r=>'<tr>'+r.map(c=>'<td>'+c+'</td>').join('')+'</tr>').join('')+'</table>';const x='<?xml version="1.0" encoding="UTF-8"?><?mso-application progid="Excel.Sheet"?><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"><Worksheet ss:Name="订单类型统计"><Table>'+d.map(r=>'<Row>'+r.map(c=>'<Cell><Data ss:Type="String">'+String(c).replace(/&/g,'&amp;').replace(/</g,'&lt;')+'</Data></Cell>').join('')+'</Row>').join('')+'</Table></Worksheet></Workbook>';const b=new Blob([x],{type:'application/vnd.ms-excel'}),u=URL.createObjectURL(b),a=document.createElement('a');a.href=u;a.download='订单类型统计_'+new Date().toISOString().slice(0,10)+'.xls';a.click();URL.revokeObjectURL(u)},
  }
}
</script>