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
      <tr v-for="p in stats" :key="p.name"><td>{{ p.name }}</td><td>{{ p.count }} 单</td><td>¥{{ (p.amount || 0).toLocaleString() }}</td></tr>
      <tr v-for="(p,i) in stats" :key="p.name"><td>{{ p.name }}</td><td>{{ p.count }} 单</td><td>¥{{ p.amount.toLocaleString() }}</td></tr>
      <tr v-if="stats.length===0"><td :colspan="3" style="text-align:center;padding:32px;color:var(--color-text-muted)">暂无数据</td></tr>
    </table>
  </div>
</template>
<script>
import api from '../utils/api'
export default {
  data:()=>({fPartner:'全部', partners:[], stats:[]}),
  mounted(){ this.loadData(); this.loadPartners() },
  methods:{
    async loadPartners(){
      try {
        const res = await api.get('/partners/list')
        const list = res.data || res
        this.partners = Array.isArray(list) ? list.map(p => p.name) : []
      } catch (e) {
        // fallback silently
      }
    },
    async loadData(){
      try {
        const params = {}
        if (this.fPartner !== '全部') params.partner = this.fPartner
        const res = await api.get('/reports/partner', { params })
        this.stats = ((res.data && res.data.data) || res.data || res || []).sort((a,b) => (b.amount||0) - (a.amount||0))
      } catch (e) {
        this.$emit('toast', { msg: '加载统计失败：' + e.message, type: 'error' })
      }
    },
    query(){ this.loadData() },
    resetQuery(){ this.fPartner='全部'; this.loadData() },
    doExport(){
      const d=[['合作方','订单数量','订单金额']]
      this.stats.forEach(p=>d.push([p.name,p.count+' 单','¥'+(p.amount||0).toLocaleString()]))
      const x='<?xml version="1.0" encoding="UTF-8"?><?mso-application progid="Excel.Sheet"?><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"><Worksheet ss:Name="合作方统计"><Table>'+d.map(r=>'<Row>'+r.map(c=>'<Cell><Data ss:Type="String">'+String(c).replace(/&/g,'&amp;').replace(/</g,'&lt;')+'</Data></Cell>').join('')+'</Row>').join('')+'</Table></Worksheet></Workbook>'
      const b=new Blob([x],{type:'application/vnd.ms-excel'}),u=URL.createObjectURL(b),a=document.createElement('a');a.href=u;a.download='合作方统计_'+new Date().toISOString().slice(0,10)+'.xls';a.click();URL.revokeObjectURL(u)
    },
}
}</script>
