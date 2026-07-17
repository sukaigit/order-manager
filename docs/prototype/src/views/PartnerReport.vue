<template>
  <div class="page-body">
    <div class="filter-bar">
      <div class="form-group"><label class="form-label">合作方</label><select class="form-select" v-model="fPartner"><option>全部</option><option v-for="p in partners" :key="p">{{ p }}</option></select></div>
      <button class="btn btn-primary" @click="query">查询</button>
      <button class="btn btn-secondary" @click="resetQuery">重置</button>
      <div style="flex:1"></div>
      <button class="btn btn-secondary" @click="doExport">导出</button>
    </div>
    <table class="data-table">
      <tr><th>合作方</th><th>订单数量</th><th>订单金额</th></tr>
      <tr v-for="(p,i) in stats" :key="p.name"><td>{{ p.name }}</td><td>{{ p.count }} 单</td><td>¥{{ p.amount.toLocaleString() }}</td></tr>
    </table>
  </div>
</template>
<script>
export default {
  data:()=>({fPartner:'全部',partners:['合作方A','合作方B','合作方C','合作方D','合作方E','合作方F','合作方G','合作方H'],
    orders:[{partner:'合作方A',amount:280000},{partner:'合作方A',amount:45000},{partner:'合作方B',amount:120000},{partner:'合作方B',amount:78000},{partner:'合作方C',amount:95000},{partner:'合作方C',amount:165000},{partner:'合作方D',amount:200000},{partner:'合作方E',amount:32000},{partner:'合作方F',amount:95000},{partner:'合作方G',amount:55000},{partner:'合作方H',amount:130000}]}),
  computed:{
    fl(){return this.fPartner==='全部'?this.orders:this.orders.filter(o=>o.partner===this.fPartner)},
    stats(){const t={};this.fl.forEach(o=>{if(!t[o.partner])t[o.partner]={name:o.partner,count:0,amount:0};t[o.partner].count++;t[o.partner].amount+=o.amount});return Object.values(t).sort((a,b)=>b.amount-a.amount)},
  },
  methods:{
    query(){},resetQuery(){this.fPartner='全部'},
    doExport(){const d=[['合作方','订单数量','订单金额']];this.stats.forEach(p=>d.push([p.name,p.count+' 单','¥'+p.amount.toLocaleString()]));const r='<table>'+d.map(r=>'<tr>'+r.map(c=>'<td>'+c+'</td>').join('')+'</tr>').join('')+'</table>';const x='<?xml version="1.0" encoding="UTF-8"?><?mso-application progid="Excel.Sheet"?><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"><Worksheet ss:Name="合作方统计"><Table>'+d.map(r=>'<Row>'+r.map(c=>'<Cell><Data ss:Type="String">'+String(c).replace(/&/g,'&amp;').replace(/</g,'&lt;')+'</Data></Cell>').join('')+'</Row>').join('')+'</Table></Worksheet></Workbook>';const b=new Blob([x],{type:'application/vnd.ms-excel'}),u=URL.createObjectURL(b),a=document.createElement('a');a.href=u;a.download='合作方统计_'+new Date().toISOString().slice(0,10)+'.xls';a.click();URL.revokeObjectURL(u)},
  }
}
</script>