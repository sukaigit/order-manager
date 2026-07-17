<template>
  <div class="page-body">
    <div class="filter-bar">
      <div class="form-group"><label class="form-label">部门编号</label><input class="form-input" v-model="fCode" placeholder="搜索" style="width:140px" /></div>
      <div class="form-group"><label class="form-label">部门名称</label><input class="form-input" v-model="fName" placeholder="搜索" /></div>
      <button class="btn btn-primary" @click="query">查询</button>
      <button class="btn btn-secondary" @click="resetQuery">重置</button>
      <div style="flex:1"></div>
      <button class="btn btn-primary" @click="openAdd">新增部门</button>
    </div>
    <div v-if="loading" style="text-align:center;padding:40px;color:var(--color-text-muted)">加载中...</div>
    <table class="data-table" v-else>
      <tr><th>部门编号</th><th>部门名称</th><th>备注</th><th>操作</th></tr>
      <tr v-for="d in pagedList" :key="d.id || d.code">
        <td style="color:var(--color-text-muted);font-size:12px">{{ d.code }}</td><td>{{ d.name }}</td>
        <td>{{ d.remark }}</td>
        <td><button class="btn btn-text btn-sm" @click="openEdit(d)">编辑</button><button class="btn btn-text btn-sm" style="color:var(--color-danger)" @click="doDelete(d)">删除</button></td>
      </tr>
      <tr v-if="departments.length===0"><td :colspan="4" style="text-align:center;padding:32px;color:var(--color-text-muted)">暂无数据</td></tr>
    </table>
    <div class="pagination">
      <div style="display:flex;align-items:center;gap:8px;font-size:13px;color:var(--color-text-muted)">
        <select class="form-select" v-model.number="pageSize" @change="page=1" style="padding:4px 8px;font-size:12px;width:auto">
          <option :value="5">5条/页</option>
          <option :value="10">10条/页</option>
          <option :value="20">20条/页</option>
          <option :value="50">50条/页</option>
        </select>
        <span>显示第 {{ (page-1)*pageSize+1 }}-{{ Math.min(page*pageSize, filteredList.length) }}条，共 {{ filteredList.length }} 条</span>
      </div>
      <div style="display:flex;align-items:center;gap:4px">
        <button class="page-btn" :disabled="page<=1" @click="page=Math.max(1,page-1)">上一页</button>
        <template v-for="(n,i) in pageNumbers" :key="i">
          <span v-if="n==='...'" style="padding:0 4px;color:var(--color-text-muted)">…</span>
          <button v-else class="page-btn" :class="{active:n===page}" @click="page=n">{{ n }}</button>
        </template>
        <button class="page-btn" :disabled="page>=totalPages" @click="page=Math.min(totalPages,page+1)">下一页</button>
      </div>
    </div>

    <div class="modal-overlay" v-if="showForm" @click.self="showForm=false">
      <div class="modal" style="min-width:420px">
        <div class="modal-title">{{ formMode==='add'?'新增部门':'编辑部门' }}</div>
        <div style="display:flex;flex-direction:column;gap:14px">
          <div v-if="formMode==='edit'"><label class="form-label">部门编号</label><input class="form-input" :value="form.code" disabled style="color:var(--color-text-muted)" /></div>
          <div><label class="form-label">部门名称 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.name" /></div>
          <div><label class="form-label">备注</label><textarea class="form-input" v-model="form.remark" placeholder="可选" rows="2" style="resize:vertical"></textarea></div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showForm=false">取消</button>
          <button class="btn btn-primary" @click="saveForm">保存</button>
        </div>
      </div>
    </div>
    <!-- 删除确认 -->
    <div class="modal-overlay" v-if="showDelete" @click.self="showDelete=false">
      <div class="modal" style="min-width:380px;text-align:center">
        <svg width="44" height="44" viewBox="0 0 24 24" fill="none" stroke="var(--color-danger)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" style="margin-bottom:12px">
          <path d="M3 6h18"/><path d="M8 6V4a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1v2"/><path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/><path d="M10 11v6"/><path d="M14 11v6"/>
        </svg>
        <div class="modal-title" style="text-align:center">确认删除</div>
        <div style="font-size:14px;color:var(--color-text-secondary);margin-bottom:24px">确定要删除部门「{{ deleteTarget?.name }}」吗？<br>此操作不可撤销。</div>
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
    loading: false,
    fName:'', fCode:'', showForm:false, showDelete:false, formMode:'add',
    form:{code:'',name:'',remark:''}, deleteTarget: null,
    page: 1, pageSize: 5,
    departments: []
  }),
  computed:{
    totalPages() { return Math.ceil(this.filteredList.length / this.pageSize) || 1 },
    filteredList() {
      return this.departments.filter(d => {
        if(this.fName && !d.name.includes(this.fName)) return false
        if(this.fCode && !d.code.includes(this.fCode.toUpperCase())) return false
        return true
      })
    },
    pagedList() {
      const start = (this.page - 1) * this.pageSize
      return this.filteredList.slice(start, start + this.pageSize)
    },
    pageNumbers() {
      const tp = this.totalPages, cp = this.page
      if (tp <= 7) return Array.from({length: tp}, (_,i) => i + 1)
      const pages = []
      pages.push(1)
      if (cp > 3) pages.push('...')
      for (let i = Math.max(2, cp - 1); i <= Math.min(tp - 1, cp + 1); i++) pages.push(i)
      if (cp < tp - 2) pages.push('...')
      pages.push(tp)
      return pages
    }
  },
  mounted() { this.loadData() },
  methods:{
    async loadData() {
      this.loading = true
      try {
        const res = await api.get('/departments')
        this.departments = (res.data?.list || res.data || res || [])
      } catch (e) {
        this.$emit('toast',{msg:'加载数据失败: ' + (e.message || ''),type:'error'})
      } finally {
        this.loading = false
      }
    },
    query(){this.page=1},
    resetQuery(){this.fName='';this.fCode='';this.page=1},
    openAdd(){this.formMode='add';this.form={code:'',name:'',remark:''};this.showForm=true},
    openEdit(d){this.formMode='edit';this.form={...d};this.showForm=true},
    async saveForm(){
      if(!this.form.name){this.$emit('toast',{msg:'请输入部门名称',type:'warning'});return}
      try {
        if(this.formMode==='add'){
          await api.post('/departments', {code: this.form.code, name: this.form.name, remark: this.form.remark})
          this.$emit('toast',{msg:'新增部门成功',type:'success'})
        } else {
          const id = this.form.id || this.form.code
          await api.put('/departments/' + id, {code: this.form.code, name: this.form.name, remark: this.form.remark})
          this.$emit('toast',{msg:'编辑成功',type:'success'})
        }
        this.showForm=false
        await this.loadData()
      } catch (e) {
        this.$emit('toast',{msg:'操作失败: ' + (e.message || ''),type:'error'})
        this.showForm=false
      }
    },
    doDelete(d){this.deleteTarget=d;this.showDelete=true},
    async confirmDelete(){
      if(this.deleteTarget){
        try {
          const id = this.deleteTarget.id || this.deleteTarget.code
          await api.delete('/departments/' + id)
          this.departments=this.departments.filter(x=>(x.id||x.code)!==id)
          this.$emit('toast',{msg:'已删除部门「'+this.deleteTarget.name+'」',type:'success'})
        } catch (e) {
          this.$emit('toast',{msg:'删除失败: ' + (e.message || ''),type:'error'})
        }
      }
      this.showDelete=false; this.deleteTarget=null
    },
  },
}
</script>
