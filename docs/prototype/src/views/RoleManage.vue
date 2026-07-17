<template>
  <div class="page-body">
    <div class="filter-bar">
      <div class="form-group"><label class="form-label">角色编号</label><input class="form-input" v-model="fCode" placeholder="搜索" style="width:150px" /></div>
      <div class="form-group"><label class="form-label">角色名称</label><input class="form-input" v-model="fName" placeholder="搜索" /></div>
      <button class="btn btn-primary" @click="query">查询</button>
      <button class="btn btn-secondary" @click="resetQuery">重置</button>
      <div style="flex:1"></div>
      <button class="btn btn-primary" @click="openAdd">新增角色</button>
    </div>
    <div v-if="loading" style="text-align:center;padding:40px;color:var(--color-text-muted)">加载中...</div>
    <table class="data-table" v-else>
      <tr><th>角色编号</th><th>角色名称</th><th>权限数</th><th>用户数</th><th>备注</th><th>操作</th></tr>
      <tr v-for="r in pagedList" :key="r.id || r.code">
        <td style="color:var(--color-text-muted);font-size:12px">{{ r.code }}</td><td>{{ r.name }}</td><td>{{ r.permCount }}</td><td>{{ r.userCount }}</td>
                <td style="font-size:12px;max-width:160px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ r.remark }}</td>
                <td><button class="btn btn-text btn-sm" @click="openEdit(r)">编辑</button><button class="btn btn-text btn-sm" @click="openPerm(r)">分配权限</button><button class="btn btn-text btn-sm" style="color:var(--color-danger)" @click="doDelete(r)">删除</button></td>
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
        <span>显示第 {{ (page-1)*pageSize+1 }}-{{ Math.min(page*pageSize, roleList.length) }}条，共 {{ roleList.length }} 条</span>
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
        <div class="modal-title">{{ formMode==='add'?'新增角色':'编辑角色' }}</div>
        <div style="display:flex;flex-direction:column;gap:14px">
          <div v-if="formMode==='edit'"><label class="form-label">角色编号</label><input class="form-input" :value="form.code" disabled style="color:var(--color-text-muted)" /></div>
          <div><label class="form-label">角色名称 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.name" /></div>
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
        <div style="font-size:14px;color:var(--color-text-secondary);margin-bottom:24px">确定要删除角色「{{ deleteTarget?.name }}」吗？<br>此操作不可撤销。</div>
        <div class="modal-footer" style="justify-content:center">
          <button class="btn btn-secondary" @click="showDelete=false">取消</button>
          <button class="btn btn-danger" @click="confirmDelete">确认删除</button>
        </div>
      </div>
    </div>
    <!-- 分配权限 -->
    <div class="modal-overlay" v-if="showPerm" @click.self="showPerm=false">
      <div class="modal" style="min-width:750px;max-height:85vh;overflow-y:auto">
        <div class="modal-title">分配权限 — {{ permRole?.name }}</div>
        <div style="display:flex;flex-direction:column;gap:16px">
          <div v-for="group in topGroups" :key="group.code" style="border:1px solid var(--color-border-light);border-radius:12px;overflow:hidden">
            <!-- 一级菜单头 -->
            <div style="display:flex;align-items:center;gap:10px;padding:10px 16px;background:var(--color-bg)" :style="{borderBottom: group.children ? '1px solid var(--color-border-light)' : 'none'}">
              <label style="display:flex;align-items:center;gap:6px;cursor:pointer;font-size:14px;font-weight:600;user-select:none">
                <input type="checkbox" :checked="groupMenuSelected(group)" @change="toggleGroupMenu(group)" style="accent-color:var(--color-primary)" />
                <span style="color:var(--color-text-muted);font-size:11px;font-weight:400">{{ group.code }}</span>
                {{ group.name }}
              </label>
              <span style="font-size:12px;color:var(--color-text-muted)">（{{ groupMenuCount(group) }} / {{ allGroupCount(group) }}）</span>
              <span v-if="group.children" style="font-size:10px;color:var(--color-text-muted);cursor:pointer;margin-left:auto" @click.stop="toggleSys()">{{ sysExpanded?'▾ 收起':'▸ 展开' }}</span>
            </div>
            <!-- 一级菜单的功能 -->
            <div v-if="group.funcs.length > 0" style="display:flex;flex-wrap:wrap;gap:6px;padding:10px 16px">
              <label v-for="f in group.funcs" :key="f.code" :style="{display:'flex',alignItems:'center',gap:'5px',padding:'5px 10px',border:'1px solid '+(selectedFuncs.includes(f.code)?'var(--color-primary)':'var(--color-border-light)'),borderRadius:'6px',cursor:'pointer',fontSize:'12px',userSelect:'none',background:selectedFuncs.includes(f.code)?'var(--color-primary-bg)':'transparent'}">
                <input type="checkbox" :value="f.code" v-model="selectedFuncs" style="accent-color:var(--color-primary)" />
                <span style="color:var(--color-text-muted);font-size:10px">{{ f.code }}</span>
                <span>{{ f.name }}</span>
              </label>
            </div>
            <!-- 二级子菜单（系统管理下属） -->
            <div v-if="group.children && sysExpanded" style="display:flex;flex-direction:column;gap:10px;padding:10px 16px 14px">
              <div v-for="child in group.children" :key="child.code" style="border:1px solid var(--color-border-light);border-radius:8px;overflow:hidden;margin-left:20px">
                <div style="display:flex;align-items:center;gap:8px;padding:8px 12px;background:var(--color-bg);border-bottom:1px solid var(--color-border-light)">
                  <label style="display:flex;align-items:center;gap:5px;cursor:pointer;font-size:13px;font-weight:500;user-select:none">
                    <input type="checkbox" :checked="childMenuSelected(child)" @change="toggleChildMenu(child)" style="accent-color:var(--color-primary)" />
                    <span style="color:var(--color-text-muted);font-size:10px;font-weight:400">{{ child.code }}</span>
                    {{ child.name }}
                  </label>
                  <span style="font-size:11px;color:var(--color-text-muted)">（{{ groupMenuCount(child) }} / {{ allGroupCount(child) }}）</span>
                </div>
                <div style="display:flex;flex-wrap:wrap;gap:5px;padding:8px 12px">
                  <label v-for="f in child.funcs" :key="f.code" :style="{display:'flex',alignItems:'center',gap:'4px',padding:'4px 8px',border:'1px solid '+(selectedFuncs.includes(f.code)?'var(--color-primary)':'var(--color-border-light)'),borderRadius:'5px',cursor:'pointer',fontSize:'11px',userSelect:'none',background:selectedFuncs.includes(f.code)?'var(--color-primary-bg)':'transparent'}">
                    <input type="checkbox" :value="f.code" v-model="selectedFuncs" style="accent-color:var(--color-primary)" />
                    <span style="color:var(--color-text-muted);font-size:9px">{{ f.code }}</span>
                    <span>{{ f.name }}</span>
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showPerm=false">取消</button>
          <button class="btn btn-primary" @click="savePerm">保存权限</button>
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
    fName:'', fCode:'', showForm:false, showDelete:false, showPerm:false, formMode:'add', sysExpanded:true,
    form:{code:'',name:'',remark:''}, permRole:null, selectedFuncs:[], selectedMenus:[],
    page: 1, pageSize: 5, deleteTarget: null,
    roles: [],
    allMenus: [],
    allFuncs: []
  }),
  computed:{
    totalPages() { return Math.ceil(this.roleList.length / this.pageSize) || 1 },
    pagedList() {
      const start = (this.page - 1) * this.pageSize
      return this.roleList.slice(start, start + this.pageSize)
    },
    roleList(){return this.roles.filter(r=>{if(this.fName&&!r.name.includes(this.fName))return false;if(this.fCode&&!r.code.includes(this.fCode.toUpperCase()))return false;return true})},
    topGroups(){
      const map = {}, codeMap = {}
      this.allMenus.forEach(m => { codeMap[m.name] = m.code })
      this.allFuncs.forEach(f => {
        if(!map[f.menu]) map[f.menu] = { code: codeMap[f.menu] || '', name: f.menu, funcs: [], children: null }
        map[f.menu].funcs.push(f)
      })
      this.allMenus.forEach(m => {
        if(!map[m.name]) map[m.name] = { code: m.code, name: m.name, funcs: [], children: null }
      })
      const sys = map['系统管理']
      if (sys) {
        sys.children = []
        this.allMenus.filter(m => m.parent === 'MENU_SYSTEM').forEach(child => {
          if (map[child.name]) { sys.children.push(map[child.name]); delete map[child.name] }
        })
      }
      return this.allMenus.filter(m => !m.parent).map(m => map[m.name]).filter(Boolean)
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
        const [rolesRes, menusRes, funcsRes] = await Promise.all([
          api.get('/roles'),
          api.get('/menus/tree'),
          api.get('/functions/list')
        ])
        const roleData = rolesRes.data?.list || rolesRes.data || rolesRes || []
        this.roles = roleData
        const menusData = menusRes.data?.list || menusRes.data || menusRes || []
        // Flatten menu tree into flat list for permission UI
        const flatten = (nodes, parent) => {
          const result = []
          for (const n of nodes) {
            const children = n.children || []
            result.push({ code: n.code, name: n.label || n.name, parent: parent || '' })
            if (children.length) result.push(...flatten(children, n.code))
          }
          return result
        }
        this.allMenus = flatten(menusData)
        const funcsData = funcsRes.data?.list || funcsRes.data || funcsRes || []
        this.allFuncs = funcsData.map(f => ({ code: f.code, name: f.name, menu: f.menu || '' }))
      } catch (e) {
        this.$emit('toast',{msg:'加载数据失败: ' + (e.message || ''),type:'error'})
      } finally {
        this.loading = false
      }
    },
    query(){this.page=1},
    resetQuery(){this.fName='';this.fCode='';this.page=1},
    openAdd(){this.formMode='add';this.form={code:'',name:''};this.showForm=true},
    openEdit(r){this.formMode='edit';this.form={...r};this.showForm=true},
    async saveForm(){
      if(!this.form.name){this.$emit('toast',{msg:'请输入角色名称',type:'warning'});return}
      try {
        if(this.formMode==='add'){
          const payload = { code: this.form.code, name: this.form.name, remark: this.form.remark || '' }
          await api.post('/roles', payload)
          this.$emit('toast',{msg:'新增角色成功',type:'success'})
        } else {
          const id = this.form.id || this.form.code
          await api.put('/roles/' + id, { code: this.form.code, name: this.form.name, remark: this.form.remark || '' })
          this.$emit('toast',{msg:'编辑成功',type:'success'})
        }
        this.showForm=false
        await this.loadData()
      } catch (e) {
        this.$emit('toast',{msg:'操作失败: ' + (e.message || ''),type:'error'})
        this.showForm=false
      }
    },
    doDelete(r){this.deleteTarget=r;this.showDelete=true},
    async confirmDelete(){
      if(this.deleteTarget){
        try {
          const id = this.deleteTarget.id || this.deleteTarget.code
          await api.delete('/roles/' + id)
          this.roles=this.roles.filter(x=>(x.id||x.code)!==id)
          this.$emit('toast',{msg:'已删除角色「'+this.deleteTarget.name+'」',type:'success'})
        } catch (e) {
          this.$emit('toast',{msg:'删除失败: ' + (e.message || ''),type:'error'})
        }
      }
      this.showDelete=false; this.deleteTarget=null
    },
    async openPerm(r){
      this.permRole=r
      this.selectedFuncs = []
      this.selectedMenus = []
      try {
        const id = r.id || r.code
        const res = await api.get('/roles/' + id + '/permissions')
        const permData = res.data || res || {}
        this.selectedFuncs = permData.funcCodes || permData.funcs || []
        this.selectedMenus = permData.menuCodes || permData.menus || []
      } catch (e) {
        // Gracefully handle - no permissions yet
      }
      this.showPerm=true
    },
    async savePerm(){
      const id = this.permRole.id || this.permRole.code
      try {
        await api.put('/roles/' + id + '/permissions', { funcCodes: this.selectedFuncs })
        const count = this.selectedMenus.length + this.selectedFuncs.length
        this.permRole.permCount = count
        this.$emit('toast',{msg:'已为「'+this.permRole.name+'」分配 '+count+' 项权限',type:'success'})
      } catch (e) {
        this.$emit('toast',{msg:'保存权限失败: ' + (e.message || ''),type:'error'})
      }
      this.showPerm=false
    },
    toggleSys(){ this.sysExpanded = !this.sysExpanded },
    allGroupCount(g){ return (g.funcs ? g.funcs.length : 0) + (g.children ? g.children.reduce((s,c)=>s+1+c.funcs.length,0) : 1) },
    groupMenuCount(g){
      let count = this.selectedMenus.includes(g.code) ? 1 : 0
      if (g.funcs) count += g.funcs.filter(f => this.selectedFuncs.includes(f.code)).length
      if (g.children) g.children.forEach(c => {
        if (this.selectedMenus.includes(c.code)) count += 1
        count += c.funcs.filter(f => this.selectedFuncs.includes(f.code)).length
      })
      return count
    },
    groupMenuSelected(g){ return this.selectedMenus.includes(g.code) },
    toggleGroupMenu(g){
      if (this.selectedMenus.includes(g.code)) {
        this.selectedMenus = this.selectedMenus.filter(c => c !== g.code)
        if (g.children) g.children.forEach(c => { this.deselectChild(c) })
        else if (g.funcs) g.funcs.forEach(f => { const i=this.selectedFuncs.indexOf(f.code); if(i>=0) this.selectedFuncs.splice(i,1) })
      } else {
        this.selectedMenus.push(g.code)
        if (g.children) g.children.forEach(c => { this.selectChild(c) })
        else if (g.funcs) g.funcs.forEach(f => { if(!this.selectedFuncs.includes(f.code)) this.selectedFuncs.push(f.code) })
      }
    },
    childMenuSelected(c){ return this.selectedMenus.includes(c.code) },
    toggleChildMenu(c){
      if (this.selectedMenus.includes(c.code)) {
        this.selectedMenus = this.selectedMenus.filter(x => x !== c.code)
        c.funcs.forEach(f => { const i=this.selectedFuncs.indexOf(f.code); if(i>=0) this.selectedFuncs.splice(i,1) })
      } else {
        this.selectedMenus.push(c.code)
        c.funcs.forEach(f => { if(!this.selectedFuncs.includes(f.code)) this.selectedFuncs.push(f.code) })
      }
    },
    selectChild(c){
      if(!this.selectedMenus.includes(c.code)) this.selectedMenus.push(c.code)
      c.funcs.forEach(f => { if(!this.selectedFuncs.includes(f.code)) this.selectedFuncs.push(f.code) })
    },
    deselectChild(c){
      this.selectedMenus = this.selectedMenus.filter(x => x !== c.code)
      c.funcs.forEach(f => { const i=this.selectedFuncs.indexOf(f.code); if(i>=0) this.selectedFuncs.splice(i,1) })
    },
  }
}
</script>
