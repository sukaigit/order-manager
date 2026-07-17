<template>
  <div class="page-body">
    <div class="filter-bar">
      <div class="form-group"><label class="form-label">用户编号</label><input class="form-input" v-model="fCode" placeholder="搜索" /></div>
      <div class="form-group"><label class="form-label">用户名</label><input class="form-input" v-model="fName" placeholder="搜索" /></div>
      <div class="form-group"><label class="form-label">角色</label><select class="form-select" v-model="fRole"><option>全部</option><option v-for="r in roles" :key="r">{{ r }}</option></select></div>
      <div class="form-group"><label class="form-label">部门</label><select class="form-select" v-model="fDept"><option>全部</option><option v-for="d in deptList" :key="d.code">{{ d.name }}</option></select></div>
      <div class="form-group"><label class="form-label">机构</label><select class="form-select" v-model="fOrg"><option>全部</option><option v-for="o in orgList" :key="o.code">{{ o.label }}</option></select></div>
      <div class="form-group"><label class="form-label">状态</label><select class="form-select" v-model="fStatus"><option>全部</option><option>启用</option><option>禁用</option><option>锁定</option></select></div>
      <button class="btn btn-primary" @click="query">查询</button>
      <button class="btn btn-secondary" @click="resetQuery">重置</button>
      <div style="flex:1"></div>
      <button class="btn btn-primary" @click="openAdd">新增用户</button>
    </div>
    <div v-if="loading" style="text-align:center;padding:40px;color:var(--color-text-muted)">加载中...</div>
    <table class="data-table" v-else>
      <tr><th>用户编号</th><th>用户名</th><th>角色</th><th>部门</th><th>机构</th><th>状态</th><th>创建时间</th><th>备注</th><th>操作</th></tr>
      <tr v-for="u in pagedList" :key="u.id || u.code">
        <td style="color:var(--color-text-muted);font-size:12px">{{ u.code }}</td><td>{{ u.name }}</td>
        <td>{{ u.role }}</td>
        <td style="font-size:13px">{{ u.dept }}</td>
        <td style="font-size:13px">{{ orgLabel(u.org) }}</td>
        <td>
          <span v-if="u.locked" class="badge badge-disabled" style="background:#fef2f2;color:#dc2626;border-color:#fecaca">锁定</span>
          <span v-else class="badge" :class="u.active?'badge-active':'badge-disabled'">{{ u.active?'启用':'禁用' }}</span>
        </td>
        <td>{{ u.date }}</td>
                <td style="font-size:12px;max-width:160px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ u.remark }}</td>
                <td style="display:flex;flex-wrap:wrap;gap:4px;justify-content:center">
          <button class="btn btn-text btn-sm" @click="openEdit(u)">编辑</button>
          <button v-if="u.locked" class="btn btn-text btn-sm" style="color:var(--color-primary)" @click="unlockUser(u)">解锁</button>
          <button v-else class="btn btn-text btn-sm" :style="{color:u.active?'var(--color-danger)':'var(--color-primary)'}" @click="toggleStatus(u)">{{ u.active?'禁用':'启用' }}</button>
          <button class="btn btn-text btn-sm" @click="openResetPwd(u)">重置密码</button>
          <button class="btn btn-text btn-sm" style="color:var(--color-danger)" @click="doDelete(u)">删除</button>
        </td>
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
        <div class="modal-title">{{ formMode==='add'?'新增用户':'编辑用户' }}</div>
        <div style="display:flex;flex-direction:column;gap:14px">
          <div v-if="formMode==='edit'"><label class="form-label">用户编号</label><input class="form-input" :value="form.code" disabled style="color:var(--color-text-muted)" /></div>
          <div><label class="form-label">用户名 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.name" /></div>
          <div><label class="form-label">角色 <span style="color:var(--color-danger)">*</span></label><select class="form-select" v-model="form.role"><option v-if="formMode==='add'" value="">请选择</option><option v-for="r in roles" :key="r">{{ r }}</option></select></div>
          <div><label class="form-label">部门 <span style="color:var(--color-danger)">*</span></label><select class="form-select" v-model="form.dept"><option v-if="formMode==='add'" value="">请选择</option><option v-for="d in deptList" :key="d.code">{{ d.name }}</option></select></div>
          <div><label class="form-label">机构 <span style="color:var(--color-danger)">*</span></label>
            <div style="position:relative">
              <div class="form-input" style="display:flex;align-items:center;cursor:pointer;user-select:none" @click="orgOpen=!orgOpen;orgSearch=''">
                <span style="flex:1;color:form.org?null:'var(--color-text-muted)'">{{ form.org ? orgName : '请选择机构' }}</span>
                <span style="font-size:10px;color:var(--color-text-muted)">{{ orgOpen ? '▾' : '▸' }}</span>
              </div>
              <div v-if="orgOpen" style="position:absolute;top:100%;left:0;right:0;z-index:50;background:var(--color-bg-white);border:1px solid var(--color-border);border-radius:8px;box-shadow:0 4px 12px rgba(0,0,0,0.1);margin-top:4px;max-height:320px;display:flex;flex-direction:column">
                <input class="form-input" v-model="orgSearch" placeholder="搜索机构..." style="margin:8px;width:auto;font-size:13px" @click.stop />
                <div style="overflow-y:auto;flex:1;max-height:260px">
                  <div v-for="o in orgTree" :key="o.code" style="display:flex;align-items:center;gap:4px;padding:6px 10px;cursor:pointer;font-size:13px;transition:background 0.1s"
                    :style="{paddingLeft:(o.depth*20+10)+'px',background:form.org===o.code?'var(--color-bg)':'transparent'}"
                    @click="selectOrg(o);orgOpen=false" @mouseenter="$event.target.style.background='var(--color-bg)'" @mouseleave="$event.target.style.background=form.org===o.code?'var(--color-bg)':'transparent'">
                    <span v-if="o.hasChildren" style="font-size:9px;color:var(--color-text-muted);width:12px;text-align:center;cursor:pointer" @click.stop="toggleOrgTree(o)">{{ orgExpanded[o.code]?'▾':'▸' }}</span>
                    <span v-else style="width:12px"></span>
                    <span :style="{fontWeight:o.depth===0?'600':'400',color:o.depth===0?'var(--color-text)':'var(--color-text-secondary)'}">{{ o.label }}</span>
                  </div>
                  <div v-if="orgTree.length===0" style="padding:12px;text-align:center;font-size:13px;color:var(--color-text-muted)">无匹配机构</div>
                </div>
              </div>
            </div>
          </div>
          <div>
            <label class="form-label">状态</label>
            <div style="display:flex;gap:0;border:1px solid var(--color-border);border-radius:8px;overflow:hidden;width:fit-content">
              <div :style="{padding:'8px 20px',cursor:'pointer',fontSize:'13px',background:form.active&&!form.locked?'var(--color-primary)':'transparent',color:form.active&&!form.locked?'white':'var(--color-text)',transition:'all 0.15s'}" @click="form.active=true;form.locked=false">启用</div>
              <div :style="{padding:'8px 20px',cursor:'pointer',fontSize:'13px',background:!form.active&&!form.locked?'var(--color-danger)':'transparent',color:!form.active&&!form.locked?'white':'var(--color-text)',transition:'all 0.15s',borderLeft:'1px solid var(--color-border)'}" @click="form.active=false;form.locked=false">禁用</div>
            </div>
          </div>
          <div><label class="form-label">备注</label><textarea class="form-input" v-model="form.remark" placeholder="可选" rows="2" style="resize:vertical"></textarea></div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showForm=false">取消</button>
          <button class="btn btn-primary" @click="saveForm">保存</button>
        </div>
      </div>
    </div>
    <!-- 重置密码 -->
    <div class="modal-overlay" v-if="showResetPwd" @click.self="showResetPwd=false">
      <div class="modal" style="min-width:380px;text-align:center">
        <svg width="44" height="44" viewBox="0 0 24 24" fill="none" stroke="var(--color-primary)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" style="margin-bottom:12px">
          <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/>
        </svg>
        <div class="modal-title" style="text-align:center">重置密码</div>
        <div style="font-size:14px;color:var(--color-text-secondary);margin-bottom:8px">用户「{{ resetPwdTarget?.name }}」的密码将重置为：</div>
        <div style="font-size:18px;font-weight:700;color:var(--color-primary);margin-bottom:8px;background:var(--color-bg);padding:10px;border-radius:8px;font-family:monospace">Uu888888!</div>
        <div style="font-size:12px;color:var(--color-text-muted);margin-bottom:24px">该用户下次登录将强制修改密码</div>
        <div class="modal-footer" style="justify-content:center">
          <button class="btn btn-secondary" @click="showResetPwd=false">取消</button>
          <button class="btn btn-primary" @click="confirmResetPwd">确认重置</button>
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
        <div style="font-size:14px;color:var(--color-text-secondary);margin-bottom:24px">确定要删除用户「{{ deleteTarget?.name }}」吗？<br>此操作不可撤销。</div>
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
    fName:'', fCode:'', fRole:'全部', fDept:'全部', fOrg:'全部', fStatus:'全部',
    showForm:false, showResetPwd:false, showDelete:false, formMode:'add',
    form:{code:'',name:'',role:'',active:true,locked:false,remark:'',dept:'',org:''},
    resetPwdTarget: null, deleteTarget: null,
    orgOpen: false, orgSearch: '', orgExpanded: {},
    page: 1, pageSize: 5,
    users: [], roles: [], deptList: [], orgList: []
  }),
  computed:{
    totalPages() { return Math.ceil(this.filteredList.length / this.pageSize) || 1 },
    filteredList() {
      return this.users.filter(u => {
        if (this.fName && !u.name.includes(this.fName)) return false
        if (this.fCode && !u.code.includes(this.fCode.toUpperCase())) return false
        if (this.fRole !== '全部' && u.role !== this.fRole) return false
        if (this.fDept !== '全部' && u.dept !== this.fDept) return false
        if (this.fOrg !== '全部' && u.org !== this.fOrg) return false
        if (this.fStatus !== '全部') {
          if (this.fStatus === '锁定' && !u.locked) return false
          if (this.fStatus === '启用' && (!u.active || u.locked)) return false
          if (this.fStatus === '禁用' && (u.active || u.locked)) return false
        }
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
    },
    orgName() {
      const o = this.orgList.find(x => x.code === this.form.org)
      return o ? o.label : ''
    },
    orgTree() {
      const search = this.orgSearch ? this.orgSearch.toLowerCase() : ''
      const matchCodes = new Set()
      if (search) {
        for (const o of this.orgList) {
          if (o.label.toLowerCase().includes(search)) {
            matchCodes.add(o.code)
            let p = o.parent
            while (p) { matchCodes.add(p); const po = this.orgList.find(x=>x.code===p); p = po ? po.parent : '' }
          }
        }
      }
      const build = (parentCode, depth) => {
        const kids = this.orgList.filter(o => o.parent === parentCode && (!search || matchCodes.has(o.code)))
        const result = []
        for (const k of kids) {
          const hasChildren = this.orgList.some(o => o.parent === k.code)
          result.push({...k, depth, hasChildren})
          if (this.orgExpanded[k.code] || search) {
            result.push(...build(k.code, depth + 1))
          }
        }
        return result
      }
      return build('', 0)
    }
  },
  mounted() {
    this.loadData()
  },
  methods:{
    async loadData() {
      this.loading = true
      try {
        const [usersRes, deptRes, orgRes, roleRes] = await Promise.all([
          api.get('/users'),
          api.get('/departments/list'),
          api.get('/organizations/tree'),
          api.get('/roles')
        ])
        this.users = (usersRes.data?.list || usersRes.data || usersRes || [])
        this.deptList = (deptRes.data?.list || deptRes.data || deptRes || [])
        const orgData = orgRes.data?.list || orgRes.data || orgRes || []
        this.orgList = orgData
        const roleData = roleRes.data?.list || roleRes.data || roleRes || []
        this.roles = roleData.map(r => r.name)
      } catch (e) {
        this.$emit('toast', {msg: '加载数据失败: ' + (e.message || ''), type: 'error'})
      } finally {
        this.loading = false
      }
    },
    query(){this.page=1},
    resetQuery(){this.fName='';this.fCode='';this.fRole='全部';this.fDept='全部';this.fOrg='全部';this.fStatus='全部';this.page=1},
    openAdd(){this.formMode='add';this.form={code:'',name:'',role:'',active:true,locked:false,remark:'',dept:'',org:''};this.showForm=true},
    openEdit(u){this.formMode='edit';this.form={...u};this.showForm=true},
    selectOrg(o){this.form.org=o.code},
    orgLabel(code){const o=this.orgList.find(x=>x.code===code);return o?o.label:'—'},
    toggleOrgTree(o){this.orgExpanded[o.code]=!this.orgExpanded[o.code]},
    async saveForm(){
      if(!this.form.name||!this.form.role||!this.form.dept){this.$emit('toast',{msg:'请填写完整信息',type:'warning'});return}
      try {
        if(this.formMode==='add'){
          await api.post('/users', { name: this.form.name, role: this.form.role, dept: this.form.dept, org: this.form.org, active: this.form.active })
          this.$emit('toast',{msg:'新增用户成功，默认密码 Uu888888!（首次登录需修改）',type:'success'})
          await this.loadData()
        } else {
          const id = this.form.id || this.form.code
          await api.put('/users/' + id, { name: this.form.name, role: this.form.role, dept: this.form.dept, org: this.form.org, active: this.form.active, remark: this.form.remark })
          this.$emit('toast',{msg:'编辑成功',type:'success'})
          await this.loadData()
        }
      } catch (e) {
        this.$emit('toast',{msg:'操作失败: ' + (e.message || ''),type:'error'})
      }
      this.showForm=false
    },
    async toggleStatus(u){
      try {
        const id = u.id || u.code
        if (u.active) {
          await api.put('/users/' + id + '/lock?locked=true')
        } else {
          await api.put('/users/' + id + '/lock?locked=false')
        }
        u.active = !u.active
        this.$emit('toast',{msg:u.active?'已启用':'已禁用',type:'success'})
      } catch (e) {
        this.$emit('toast',{msg:'操作失败: ' + (e.message || ''),type:'error'})
      }
    },
    async unlockUser(u){
      try {
        const id = u.id || u.code
        await api.put('/users/' + id + '/lock?locked=false')
        u.locked=false; u.active=true
        this.$emit('toast',{msg:'用户「'+u.name+'」已解锁',type:'success'})
      } catch (e) {
        this.$emit('toast',{msg:'操作失败: ' + (e.message || ''),type:'error'})
      }
    },
    openResetPwd(u){this.resetPwdTarget=u;this.showResetPwd=true},
    async confirmResetPwd(){
      try {
        const id = this.resetPwdTarget.id || this.resetPwdTarget.code
        await api.put('/users/' + id + '/reset-pwd')
        this.$emit('toast',{msg:'用户「'+this.resetPwdTarget.name+'」密码已重置为 Uu888888!，下次登录需修改',type:'success'})
      } catch (e) {
        this.$emit('toast',{msg:'操作失败: ' + (e.message || ''),type:'error'})
      }
      this.showResetPwd=false; this.resetPwdTarget=null
    },
    doDelete(u){this.deleteTarget=u;this.showDelete=true},
    async confirmDelete(){
      if(this.deleteTarget){
        try {
          const id = this.deleteTarget.id || this.deleteTarget.code
          await api.delete('/users/' + id)
          this.users=this.users.filter(x=>(x.id||x.code)!==id)
          this.$emit('toast',{msg:'已删除用户「'+this.deleteTarget.name+'」',type:'success'})
        } catch (e) {
          this.$emit('toast',{msg:'删除失败: ' + (e.message || ''),type:'error'})
        }
      }
      this.showDelete=false; this.deleteTarget=null
    },
  }
}
</script>
