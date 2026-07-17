<template>
  <div class="page-body">
    <div class="filter-bar">
      <div class="form-group"><label class="form-label">订单编号</label><input class="form-input" v-model="fCode" placeholder="搜索" style="width:120px" /></div>
      <div class="form-group"><label class="form-label">订单类型</label><select class="form-select" v-model="fType"><option>全部</option><option>中间业务</option><option>数据开发</option><option>应用开发</option></select></div>
      <div class="form-group"><label class="form-label">合作方</label><select class="form-select" v-model="fPartner"><option>全部</option><option v-for="p in partnerOptions" :key="p">{{ p }}</option></select></div>
      <div class="form-group"><label class="form-label">状态</label><select class="form-select" v-model="fStatus"><option>全部</option><option>待审核</option><option>已下单</option><option>已结清</option></select></div>
      <button class="btn btn-primary" @click="query">查询</button>
      <button class="btn btn-secondary" @click="resetQuery">重置</button>
      <div style="flex:1"></div>
      <button class="btn btn-primary" @click="openAdd">新增订单</button>
      <button class="btn btn-secondary" @click="doExport"><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="vertical-align:middle;margin-right:4px"><path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></svg>导出</button>
    </div>
    <table class="data-table">
      <tr><th>订单编号</th><th>订单名称</th><th>订单类型</th><th>合作方</th><th>订单金额</th><th>订单时间</th><th>状态</th><th>备注</th><th>操作</th></tr>
      <tr v-for="o in orderList" :key="o.id || o.code">
        <td style="font-size:12px;color:var(--color-text-muted)">{{ o.code }}</td><td>{{ o.name }}</td><td>{{ o.type }}</td><td>{{ o.partner }}</td><td>¥{{ (o.amount || 0).toLocaleString() }}</td><td style="font-size:12px">{{ o.date }}</td>
        <td><span class="badge" :class="'badge-'+o.statusCls">{{ o.status }}</span></td>
        <td style="font-size:12px;max-width:140px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ o.remark }}</td>
        <td style="display:flex;flex-wrap:wrap;gap:4px;justify-content:center">
          <button class="btn btn-text btn-sm" @click="openEdit(o)">编辑</button>
          <button v-if="o.status==='待审核'" class="btn btn-text btn-sm" style="color:var(--color-danger)" @click="doDelete(o)">删除</button>
        </td>
      </tr>
    </table>
    <div class="pagination">
      <div style="display:flex;align-items:center;gap:8px;font-size:13px;color:var(--color-text-muted)">
        <select class="form-select" v-model.number="pageSize" @change="page=1; loadData()" style="padding:4px 8px;font-size:12px;width:auto">
          <option :value="5">5条/页</option><option :value="10">10条/页</option><option :value="20">20条/页</option><option :value="50">50条/页</option>
        </select>
        <span>显示第 {{ startRecord }}-{{ endRecord }} 条，共 {{ totalCount }} 条</span>
      </div>
      <div style="display:flex;gap:4px">
        <button class="page-btn" :disabled="page<=1" @click="page=Math.max(1,page-1); loadData()">上一页</button>
        <button class="page-btn" v-for="n in pageNumbers" :key="n" :class="{active:n===page}" @click="typeof n==='number'&&(page=n)&&loadData()" v-text="n"></button>
        <button class="page-btn" :disabled="page>=totalPages" @click="page=Math.min(totalPages,page+1); loadData()">下一页</button>
      </div>
    </div>

    <div class="modal-overlay" v-if="showForm" @click.self="showForm=false">
      <div class="modal" style="min-width:520px">
        <div class="modal-title">{{ formMode==='add'?'新增订单':'编辑订单' }}</div>
        <div style="display:flex;flex-direction:column;gap:14px">
          <div v-if="formMode==='edit'" style="display:flex;gap:16px">
            <div style="flex:1"><label class="form-label">订单编号</label><input class="form-input" :value="form.code" disabled style="color:var(--color-text-muted)" /></div>
            <div style="flex:1"><label class="form-label">订单时间</label><input class="form-input" :value="form.date" disabled style="color:var(--color-text-muted)" /></div>
          </div>
          <div><label class="form-label">订单名称 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.name" /></div>
          <div style="display:flex;gap:16px">
            <div style="flex:1"><label class="form-label">订单类型 <span style="color:var(--color-danger)">*</span></label><select class="form-select" v-model="form.type"><option value="">请选择</option><option>中间业务</option><option>数据开发</option><option>应用开发</option></select></div>
            <div style="flex:1"><label class="form-label">合作方 <span style="color:var(--color-danger)">*</span></label><select class="form-select" v-model="form.partner"><option value="">请选择</option><option v-for="p in partnerOptions" :key="p">{{ p }}</option></select></div>
          </div>
          <div style="display:flex;gap:16px">
            <div style="flex:1"><label class="form-label">订单金额 <span style="color:var(--color-danger)">*</span></label><input class="form-input" type="number" min="0.01" step="0.01" v-model.number="form.amount" /></div>
          </div>
          <div><label class="form-label">备注</label><textarea class="form-input" v-model="form.remark" placeholder="可选" rows="2" style="resize:vertical"></textarea></div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showForm=false">取消</button>
          <button class="btn btn-primary" @click="saveForm">保存</button>
        </div>
      </div>
    </div>

    <div class="modal-overlay" v-if="showDelete" @click.self="showDelete=false">
      <div class="modal" style="min-width:380px;text-align:center">
        <svg width="44" height="44" viewBox="0 0 24 24" fill="none" stroke="var(--color-danger)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" style="margin-bottom:12px"><path d="M3 6h18"/><path d="M8 6V4a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1v2"/><path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/><path d="M10 11v6"/><path d="M14 11v6"/></svg>
        <div class="modal-title" style="text-align:center">确认删除</div>
        <div style="font-size:14px;color:var(--color-text-secondary);margin-bottom:24px">确定要删除订单「{{ deleteTarget?.code }}」吗？<br>此操作不可撤销。</div>
        <div class="modal-footer" style="justify-content:center">
          <button class="btn btn-secondary" @click="showDelete=false">取消</button>
          <button class="btn btn-danger" @click="confirmDelete">确认删除</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import api from '../utils/api'
export default {
  data: () => ({
    fCode:'', fType:'全部', fPartner:'全部', fStatus:'全部',
    showForm:false, showDelete:false, formMode:'add', deleteTarget:null,
    form:{code:'',name:'',type:'',partner:'',amount:0,remark:''},
    page:1, pageSize:5,
    orderList:[], totalCount:0, partnerOptions:[]
  }),
  computed:{
    totalPages(){return Math.ceil(this.totalCount/this.pageSize)||1},
    startRecord(){return this.totalCount===0?0:(this.page-1)*this.pageSize+1},
    endRecord(){return Math.min(this.page*this.pageSize,this.totalCount)},
    pageNumbers(){const tp=this.totalPages,cp=this.page;if(tp<=7)return Array.from({length:tp},(_,i)=>i+1);const p=[1];if(cp>3)p.push('...');for(let i=Math.max(2,cp-1);i<=Math.min(tp-1,cp+1);i++)p.push(i);if(cp<tp-2)p.push('...');p.push(tp);return p}
  },
  mounted(){ this.loadData(); this.loadPartners() },
  methods:{
    async loadData(){
      try {
        const params = { page: this.page, pageSize: this.pageSize }
        if (this.fCode) params.code = this.fCode
        if (this.fType !== '全部') params.type = this.fType
        if (this.fPartner !== '全部') params.partner = this.fPartner
        if (this.fStatus !== '全部') params.status = this.fStatus
        const res = await api.get('/orders', { params })
        const data = res.data || res
        this.orderList = data.list || data
        this.totalCount = data.total || 0
      } catch (e) {
        this.$emit('toast', { msg: '加载订单失败：' + e.message, type: 'error' })
      }
    },
    async loadPartners(){
      try {
        const res = await api.get('/partners/list')
        const list = res.data || res
        this.partnerOptions = Array.isArray(list) ? list.map(p => p.name) : []
      } catch (e) { /* silent */ }
    },
    query(){ this.page=1; this.loadData() },
    resetQuery(){ this.fCode=''; this.fType='全部'; this.fPartner='全部'; this.fStatus='全部'; this.page=1; this.loadData() },
    openAdd(){this.formMode='add';this.form={code:'',name:'',type:'',partner:'',amount:0,remark:''};this.showForm=true},
    openEdit(o){if(o.status!=='待审核'){this.$emit('toast',{msg:'已'+o.status+'订单不可修改',type:'warning'});return};this.formMode='edit';this.form={...o};this.showForm=true},
    async saveForm(){
      if(!this.form.name||!this.form.type||!this.form.partner||!this.form.amount){this.$emit('toast',{msg:'请填写必填信息',type:'warning'});return}
      if(this.form.amount<0.01){this.$emit('toast',{msg:'金额不能小于0.01',type:'warning'});return}
      try {
        const payload = { name: this.form.name, type: this.form.type, partner: this.form.partner, amount: this.form.amount, remark: this.form.remark }
        if (this.formMode === 'add') {
          await api.post('/orders', payload)
          this.$emit('toast', { msg: '新增订单成功', type: 'success' })
        } else {
          await api.put('/orders/' + this.form.id, payload)
          this.$emit('toast', { msg: '编辑成功', type: 'success' })
        }
        this.showForm = false
        this.loadData()
      } catch (e) {
        this.$emit('toast', { msg: '保存失败：' + e.message, type: 'error' })
      }
    },
    doDelete(o){this.deleteTarget=o;this.showDelete=true},
    async confirmDelete(){
      if (!this.deleteTarget) return
      try {
        await api.delete('/orders/' + this.deleteTarget.id)
        this.$emit('toast', { msg: '已删除订单「' + this.deleteTarget.code + '」', type: 'success' })
        this.showDelete = false
        this.deleteTarget = null
        this.loadData()
      } catch (e) {
        this.$emit('toast', { msg: '删除失败：' + e.message, type: 'error' })
        this.showDelete = false
        this.deleteTarget = null
      }
    },
    async doExport(){
      try {
        const params = {}
        if (this.fCode) params.code = this.fCode
        if (this.fType !== '全部') params.type = this.fType
        if (this.fPartner !== '全部') params.partner = this.fPartner
        if (this.fStatus !== '全部') params.status = this.fStatus
        const res = await api.get('/orders/export', { params, responseType: 'blob' })
        const url = URL.createObjectURL(new Blob([res], { type: 'application/vnd.ms-excel' }))
        const a = document.createElement('a')
        a.href = url
        a.download = '订单列表_' + new Date().toISOString().slice(0, 10) + '.xls'
        document.body.appendChild(a)
        a.click()
        document.body.removeChild(a)
        URL.revokeObjectURL(url)
        this.$emit('toast', { msg: '导出成功', type: 'success' })
      } catch (e) {
        this.$emit('toast', { msg: '导出失败：' + e.message, type: 'error' })
      }
    }
  }
}
</script>
