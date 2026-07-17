<template>
  <div class="page-body">
    <div class="filter-bar">
      <div class="form-group"><label class="form-label">功能编号</label><input class="form-input" v-model="fCode" placeholder="搜索" style="width:140px" /></div>
      <div class="form-group"><label class="form-label">功能名称</label><input class="form-input" v-model="fName" placeholder="搜索" /></div>
      <div class="form-group"><label class="form-label">所属菜单</label><select class="form-select" v-model="fMenu"><option>全部</option><option v-for="m in menus" :key="m">{{ m }}</option></select></div>
      <div class="form-group"><label class="form-label">权限标识</label><input class="form-input" v-model="fPerm" placeholder="搜索" style="width:160px" /></div>
      <button class="btn btn-primary" @click="query">查询</button>
      <button class="btn btn-secondary" @click="resetQuery">重置</button>
      <div style="flex:1"></div>
      <button class="btn btn-primary" @click="openAdd">新增功能</button>
    </div>
    <div v-if="loading" style="text-align:center;padding:40px;color:var(--color-text-muted)">加载中...</div>
    <table class="data-table" v-else>
      <tr><th>功能编号</th><th>功能名称</th><th>所属菜单</th><th>权限标识</th><th>备注</th><th>操作</th></tr>
      <tr v-for="f in pagedList" :key="f.id || f.code">
        <td style="color:var(--color-text-muted);font-size:12px">{{ f.code }}</td><td>{{ f.name }}</td><td>{{ f.menu }}</td><td><code style="background:var(--color-bg);padding:2px 6px;border-radius:4px;font-size:12px">{{ f.perm }}</code></td>
                <td style="font-size:12px;max-width:160px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ f.remark }}</td>
                <td><button class="btn btn-text btn-sm" @click="openEdit(f)">编辑</button><button class="btn btn-text btn-sm" style="color:var(--color-danger)" @click="doDelete(f)">删除</button></td>
      </tr>
    </table>
    <div class="pagination">
      <div style="display:flex;align-items:center;gap:8px;font-size:13px;color:var(--color-text-muted)">
        <select class="form-select" v-model.number="pageSize" @change="page=1" style="padding:4px 8px;font-size:12px;width:auto">
          <option :value="5">5条/页</option>
          <option :value="10">10条/页</option>
          <option :value="20">20条/页</option>
          <option :value="50">50条/页</option>
        </select>
        <span>显示第 {{ (page-1)*pageSize+1 }}-{{ Math.min(page*pageSize, funcList.length) }}条，共 {{ funcList.length }} 条</span>
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
        <div class="modal-title">{{ formMode==='add'?'新增功能':'编辑功能' }}</div>
        <div style="display:flex;flex-direction:column;gap:14px">
          <div v-if="formMode==='edit'"><label class="form-label">功能编号</label><input class="form-input" :value="form.code" disabled style="color:var(--color-text-muted)" /></div>
          <div><label class="form-label">功能名称 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.name" /></div>
          <div><label class="form-label">所属菜单 <span style="color:var(--color-danger)">*</span></label><select class="form-select" v-model="form.menu"><option v-if="formMode==='add'" value="">请选择</option><option v-for="m in menus" :key="m">{{ m }}</option></select></div>
          <div><label class="form-label">权限标识 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.perm" placeholder="例: supplier:query" /></div>
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
        <div style="font-size:14px;color:var(--color-text-secondary);margin-bottom:24px">确定要删除功能「{{ deleteTarget?.name }}」吗？<br>此操作不可撤销。</div>
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
    fName:'', fMenu:'全部', fCode:'', fPerm:'', showForm:false, showDelete:false, formMode:'add',
    form:{code:'',name:'',menu:'',perm:'',remark:''},
    page: 1, pageSize: 5, deleteTarget: null,
    funcs: [],
    menus: []
  }),
  computed:{
    totalPages() { return Math.ceil(this.funcList.length / this.pageSize) || 1 },
    pagedList() {
      const start = (this.page - 1) * this.pageSize
      return this.funcList.slice(start, start + this.pageSize)
    },
    funcList(){return this.funcs.filter(f=>{
      if(this.fName&&!f.name.includes(this.fName))return false
      if(this.fMenu!=='全部'&&f.menu!==this.fMenu)return false
      if(this.fCode&&!f.code.toLowerCase().includes(this.fCode.toLowerCase()))return false
      if(this.fPerm&&!f.perm.toLowerCase().includes(this.fPerm.toLowerCase()))return false
      return true
    })},
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
        const [funcsRes, menusRes] = await Promise.all([
          api.get('/functions'),
          api.get('/menus/tree')
        ])
        const funcsData = funcsRes.data?.list || funcsRes.data || funcsRes || []
        this.funcs = funcsData
        const menusData = menusRes.data?.list || menusRes.data || menusRes || []
        const flatten = (nodes, nameList) => {
          for (const n of nodes) {
            const label = n.label || n.name
            if (label && !nameList.includes(label)) nameList.push(label)
            if (n.children && n.children.length) flatten(n.children, nameList)
          }
          return nameList
        }
        this.menus = flatten(menusData, [])
      } catch (e) {
        this.$emit('toast',{msg:'加载数据失败: ' + (e.message || ''),type:'error'})
      } finally {
        this.loading = false
      }
    },
    query(){this.page=1},
    resetQuery(){this.fName='';this.fMenu='全部';this.fCode='';this.fPerm='';this.page=1},
    openAdd(){this.formMode='add';this.form={code:'',name:'',menu:'',perm:'',remark:''};this.showForm=true},
    openEdit(f){this.formMode='edit';this.form={...f};this.showForm=true},
    async saveForm(){
      if(!this.form.name||!this.form.perm||!this.form.menu){this.$emit('toast',{msg:'请填写完整信息',type:'warning'});return}
      try {
        const payload = {
          name: this.form.name,
          menu: this.form.menu,
          perm: this.form.perm,
          remark: this.form.remark || ''
        }
        if (!this.form.code) {
          payload.code = this.form.perm.replace(':','_').toUpperCase()
        } else {
          payload.code = this.form.code
        }
        if(this.formMode==='add'){
          await api.post('/functions', payload)
          this.$emit('toast',{msg:'新增功能成功',type:'success'})
        } else {
          const id = this.form.id || this.form.code
          await api.put('/functions/' + id, payload)
          this.$emit('toast',{msg:'编辑成功',type:'success'})
        }
        this.showForm=false
        await this.loadData()
      } catch (e) {
        this.$emit('toast',{msg:'操作失败: ' + (e.message || ''),type:'error'})
        this.showForm=false
      }
    },
    doDelete(f){this.deleteTarget=f;this.showDelete=true},
    async confirmDelete(){
      if(this.deleteTarget){
        try {
          const id = this.deleteTarget.id || this.deleteTarget.code
          await api.delete('/functions/' + id)
          this.funcs=this.funcs.filter(x=>(x.id||x.code)!==id)
          this.$emit('toast',{msg:'已删除功能「'+this.deleteTarget.name+'」',type:'success'})
        } catch (e) {
          this.$emit('toast',{msg:'删除失败: ' + (e.message || ''),type:'error'})
        }
      }
      this.showDelete=false; this.deleteTarget=null
    },
  }
}
</script>
