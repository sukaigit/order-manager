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
      <tr v-for="t in stats" :key="t.name"><td>{{ t.name }}</td><td>{{ t.count }} 单</td><td>¥{{ (t.amount || 0).toLocaleString() }}</td></tr>
    </table>
  </div>
</template>
<script>
import api from '../utils/api'
export default {
  data:()=>({fType:'全部', stats:[]}),
  mounted(){ this.loadData() },
  methods:{
    async loadData(){
      try {
        const params = {}
        if (this.fType !== '全部') params.type = this.fType
        const res = await api.get('/reports/type', { params })
        this.stats = res.data || res
      } catch (e) {
        this.$emit('toast', { msg: '加载统计失败：' + e.message, type: 'error' })
      }
    },
    query(){ this.loadData() },
    resetQuery(){ this.fType='全部'; this.loadData() },
    doExport(){
      const d=[['订单类型','订单数量','订单金额']]
      this.stats.forEach(t=>d.push([t.name,t.count+' 单','¥'+(t.amount||0).toLocaleString()]))
      const x='<?xml version="1.0" encoding="UTF-8"?><?mso-application progid="Excel.Sheet"?><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"><Worksheet ss:Name="订单类型统计"><Table>'+d.map(r=>'<Row>'+r.map(c=>'<Cell><Data ss:Type="String">'+String(c).replace(/&/g,'&amp;').replace(/</g,'&lt;')+'</Data></Cell>').join('')+'</Row>').join('')+'</Table></Worksheet></Workbook>'
      const b=new Blob([x],{type:'application/vnd.ms-excel'}),u=URL.createObjectURL(b),a=document.createElement('a');a.href=u;a.download='订单类型统计_'+new Date().toISOString().slice(0,10)+'.xls';a.click();URL.revokeObjectURL(u)
    },
  }
}
</script>
