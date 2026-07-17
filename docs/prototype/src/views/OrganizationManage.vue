<template>
  <div class="page-body">
    <div class="filter-bar">
      <div class="form-group"><label class="form-label">机构编号</label><input class="form-input" v-model="fCode" placeholder="搜索" style="width:120px" /></div>
      <div class="form-group"><label class="form-label">机构名称</label><input class="form-input" v-model="fName" placeholder="搜索" /></div>
      <div class="form-group"><label class="form-label">机构简称</label><input class="form-input" v-model="fSname" placeholder="搜索" style="width:120px" /></div>
      <div class="form-group"><label class="form-label">机构层级</label><select class="form-select" v-model="fLevel"><option>全部</option><option>总行</option><option>一级分行</option><option>二级分行</option><option>一级支行</option><option>二级支行</option></select></div>
      <button class="btn btn-primary" @click="query">查询</button>
      <button class="btn btn-secondary" @click="resetQuery">重置</button>
      <div style="flex:1"></div>
      <button class="btn btn-primary" @click="openAdd">新增机构</button>
    </div>
    <div v-if="loading" style="text-align:center;padding:40px;color:var(--color-text-muted)">加载中...</div>
    <table class="data-table" v-else>
      <tr><th>机构编号</th><th>机构名称</th><th>机构简称</th><th>机构层级</th><th>联系人</th><th>联系电话</th><th>所在地区</th><th>详细地址</th><th>备注</th><th>操作</th></tr>
      <tr v-for="o in pagedList" :key="o.code">
        <td style="color:var(--color-text-muted);font-size:12px">{{ o.code }}</td>
        <td>
          <span v-if="o.level==='hq'" style="display:inline-flex;align-items:center;gap:4px;cursor:pointer" @click="toggleExpand(o)">
            <span v-if="hasChildren(o)" style="font-size:10px;color:var(--color-text-muted);width:14px;text-align:center;transition:transform 0.15s">{{ expanded[o.code]||(fName||fCode||fSname||fLevel!=='全部')?'▾':'▸' }}</span>
            <span v-else style="width:14px"></span>
            <span style="font-weight:600">{{ o.label }}</span>
          </span>
          <span v-else-if="o.level==='branch1'" style="display:inline-flex;align-items:center;gap:4px;padding-left:24px;cursor:pointer" @click="toggleExpand(o)">
            <span v-if="hasChildren(o)" style="font-size:10px;color:var(--color-text-muted);width:14px;text-align:center">{{ expanded[o.code]||(fName||fCode||fSname||fLevel!=='全部')?'▾':'▸' }}</span>
            <span v-else style="width:14px"></span>
            {{ o.label }}
          </span>
          <span v-else-if="o.level==='branch2'" style="display:inline-flex;align-items:center;gap:4px;padding-left:48px;cursor:pointer" @click="toggleExpand(o)">
            <span v-if="hasChildren(o)" style="font-size:10px;color:var(--color-text-muted);width:14px;text-align:center">{{ expanded[o.code]||(fName||fCode||fSname||fLevel!=='全部')?'▾':'▸' }}</span>
            <span v-else style="width:14px"></span>
            {{ o.label }}
          </span>
          <span v-else-if="o.level==='sub1'" style="display:inline-flex;align-items:center;gap:4px;padding-left:72px;cursor:pointer" @click="toggleExpand(o)">
            <span v-if="hasChildren(o)" style="font-size:10px;color:var(--color-text-muted);width:14px;text-align:center">{{ expanded[o.code]||(fName||fCode||fSname||fLevel!=='全部')?'▾':'▸' }}</span>
            <span v-else style="width:14px"></span>
            <span style="color:var(--color-text-secondary)">{{ o.label }}</span>
          </span>
          <span v-else style="padding-left:96px;color:var(--color-text-muted);font-size:13px">{{ o.label }}</span>
        </td>
        <td style="font-size:13px">{{ o.sname || '—' }}</td>
        <td><span class="badge" :class="badgeClass(o.level)" style="font-size:11px">{{ levelLabel(o.level) }}</span></td>
        <td style="font-size:13px">{{ o.contact || '—' }}</td>
        <td style="font-size:13px">{{ o.phone || '—' }}</td>
        <td style="font-size:13px">{{ o.region || '—' }}</td>
        <td style="font-size:13px;max-width:180px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ o.address || '—' }}</td>
        <td>{{ o.remark }}</td>
        <td style="display:flex;flex-wrap:wrap;gap:4px;justify-content:center">
          <button v-if="nextLevel[o.level]" class="btn btn-text btn-sm" @click="openAddChild(o)">新增下级</button>
          <button class="btn btn-text btn-sm" @click="openEdit(o)">编辑</button>
          <button class="btn btn-text btn-sm" style="color:var(--color-danger)" @click="doDelete(o)">删除</button>
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
        <span>显示第 {{ (page-1)*pageSize+1 }}-{{ Math.min(page*pageSize, totalOrgs) }}条，共 {{ totalOrgs }} 条</span>
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
      <div class="modal" style="min-width:560px">
        <div class="modal-title">{{ formMode==='add'?'新增机构':'编辑机构' }}</div>
        <div style="display:flex;flex-direction:column;gap:14px">
          <div v-if="formMode==='edit'" style="display:flex;gap:16px">
            <div style="flex:1"><label class="form-label">机构编号</label><input class="form-input" :value="form.code" disabled style="color:var(--color-text-muted)" /></div>
            <div style="flex:1"><label class="form-label">机构层级</label><input class="form-input" :value="levelLabel(form.level)" disabled style="color:var(--color-text-muted)" /></div>
          </div>
          <div v-if="formMode==='add'" style="display:flex;gap:16px">
            <div style="flex:1"><label class="form-label">机构编号 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.code" placeholder="例: HQ001" /></div>
            <div style="flex:1"><label class="form-label">机构层级</label><input class="form-input" :value="levelLabel(form.level)" disabled style="color:var(--color-text-muted)" /></div>
          </div>
          <div v-if="form.parent && formMode==='add'"><label class="form-label">上级机构</label><input class="form-input" :value="parentName()" disabled style="color:var(--color-text-muted)" /></div>
          <div style="display:flex;gap:16px">
            <div style="flex:1"><label class="form-label">机构名称 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.label" /></div>
            <div style="flex:1"><label class="form-label">机构简称 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.sname" /></div>
          </div>
          <div style="display:flex;gap:16px">
            <div style="flex:1"><label class="form-label">联系人</label><input class="form-input" v-model="form.contact" placeholder="可选" /></div>
            <div style="flex:1"><label class="form-label">联系电话</label><input class="form-input" v-model="form.phone" placeholder="可选" /></div>
          </div>
          <div><label class="form-label">所在地区 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.region" placeholder="例: 北京市朝阳区" /></div>
          <div><label class="form-label">详细地址 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.address" placeholder="例: 建国路88号" /></div>
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
        <div style="font-size:14px;color:var(--color-text-secondary);margin-bottom:24px">确定要删除机构「{{ deleteTarget?.label }}」吗？<br>此操作不可撤销。</div>
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
const levelLabels = { hq:'总行', branch1:'一级分行', branch2:'二级分行', sub1:'一级支行', sub2:'二级支行' }
const nextLevel = { '':'hq', hq:'branch1', branch1:'branch2', branch2:'sub1', sub1:'sub2', sub2:'' }

export default {
  data: () => ({
    loading: false,
    fName:'', fCode:'', fSname:'', fLevel:'全部', showForm:false, showDelete:false, formMode:'add',
    form:{code:'',label:'',sname:'',level:'',parent:'',contact:'',phone:'',region:'',address:'',remark:''},
    deleteTarget: null, expanded: {},
    page: 1, pageSize: 5,
    orgs: []
  }),
  computed:{
    rootOrgs() {
      const list = this.orgs
      const roots = list.filter(o => !o.parent)
      if (roots.length === 0 && (this.fName || this.fCode || this.fSname || this.fLevel !== '全部')) {
        const matchedNonRoots = list.filter(o => o.parent)
        const ancestorCodes = new Set()
        for (const o of matchedNonRoots) {
          let p = o.parent
          while (p) {
            ancestorCodes.add(p)
            const parent = list.find(x => x.code === p)
            p = parent ? parent.parent : ''
          }
        }
        return list.filter(o => ancestorCodes.has(o.code) && !o.parent)
      }
      return roots
    },
    totalOrgs() { return this.rootOrgs.length },
    totalPages() { return Math.ceil(this.totalOrgs / this.pageSize) || 1 },
    filteredOrgs() {
      return this.orgs.filter(o => {
        if(this.fName && !o.label.includes(this.fName)) return false
        if(this.fCode && !o.code.toUpperCase().includes(this.fCode.toUpperCase())) return false
        if(this.fSname && !o.sname.includes(this.fSname)) return false
        if(this.fLevel !== '全部') {
          const levelMap = {'总行':'hq','一级分行':'branch1','二级分行':'branch2','一级支行':'sub1','二级支行':'sub2'}
          if(o.level !== levelMap[this.fLevel]) return false
        }
        return true
      })
    },
    orgTree() {
      const list = this.orgs
      const visibleCodes = new Set()
      if (this.fName || this.fCode || this.fSname || this.fLevel !== '全部') {
        for (const o of this.filteredOrgs) {
          visibleCodes.add(o.code)
          let p = o.parent
          while (p) {
            visibleCodes.add(p)
            const parent = list.find(x => x.code === p)
            p = parent ? parent.parent : ''
          }
        }
      }
      const findChildren = (parentCode) => {
        const kids = list.filter(o => o.parent === parentCode && (!visibleCodes.size || visibleCodes.has(o.code)))
        const result = []
        for (const k of kids) {
          result.push(k)
          if (!visibleCodes.size || visibleCodes.has(k.code)) {
            result.push(...findChildren(k.code))
          }
        }
        return result
      }
      const roots = this.rootOrgs.slice((this.page-1)*this.pageSize, this.page*this.pageSize)
      const result = []
      for (const r of roots) {
        result.push(r)
        const showChildren = this.expanded[r.code] || (this.fName || this.fCode || this.fSname || this.fLevel !== '全部')
        if (showChildren) {
          result.push(...findChildren(r.code))
        }
      }
      return result
    },
    pagedList() { return this.orgTree },
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
    nextLevel() { return nextLevel }
  },
  mounted() { this.loadData() },
  methods:{
    async loadData() {
      this.loading = true
      try {
        const res = await api.get('/organizations/tree')
        const tree = (res.data?.list || res.data || res || [])
        // Flatten the tree into a flat list for the existing computed properties
        const flatten = (nodes) => {
          const result = []
          for (const n of nodes) {
            const children = n.children || []
            const flatNode = {...n}
            delete flatNode.children
            result.push(flatNode)
            if (children.length) result.push(...flatten(children))
          }
          return result
        }
        this.orgs = flatten(tree)
      } catch (e) {
        this.$emit('toast',{msg:'加载数据失败: ' + (e.message || ''),type:'error'})
      } finally {
        this.loading = false
      }
    },
    levelLabel(l) { return levelLabels[l] || '' },
    badgeClass(l) { return l==='hq'?'badge-primary':l==='branch1'?'badge-active':l==='branch2'?'badge-pending':'badge-disabled' },
    hasChildren(o) { return this.orgs.some(x => x.parent === o.code) },
    toggleExpand(o) {
      if (!this.hasChildren(o)) return
      this.expanded[o.code] = !this.expanded[o.code]
    },
    query(){this.page=1},
    resetQuery(){this.fName='';this.fCode='';this.fSname='';this.fLevel='全部';this.page=1},
    openAdd(){
      this.formMode='add'
      this.form={code:'',label:'',sname:'',level:'hq',parent:'',contact:'',phone:'',region:'',address:'',remark:''}
      this.showForm=true
    },
    openAddChild(parent){
      this.formMode='add'
      const next = nextLevel[parent.level]
      if (!next) { this.$emit('toast',{msg:'已达最末级，不可新增下级',type:'warning'}); return }
      this.form={code:'',label:'',sname:'',level:next,parent:parent.code,contact:'',phone:'',region:'',address:'',remark:''}
      this.showForm=true
    },
    openEdit(o){
      this.formMode='edit'
      this.form={...o}
      this.showForm=true
    },
    parentName() {
      const p = this.orgs.find(x => x.code === this.form.parent)
      return p ? p.label + ' (' + levelLabels[p.level] + ')' : ''
    },
    async saveForm(){
      if(!this.form.code||!this.form.label||!this.form.sname||!this.form.region||!this.form.address){
        this.$emit('toast',{msg:'请填写必填信息',type:'warning'});return
      }
      if(!/^[A-Za-z0-9]+$/.test(this.form.code)){this.$emit('toast',{msg:'机构编号只能包含字母和数字',type:'warning'});return}
      try {
        if(this.formMode==='add'){
          await api.post('/organizations', {...this.form})
          this.$emit('toast',{msg:'新增机构成功',type:'success'})
        } else {
          const id = this.form.id || this.form.code
          await api.put('/organizations/' + id, {...this.form})
          this.$emit('toast',{msg:'编辑成功',type:'success'})
        }
        this.showForm=false
        await this.loadData()
      } catch (e) {
        this.$emit('toast',{msg:'操作失败: ' + (e.message || ''),type:'error'})
        this.showForm=false
      }
    },
    doDelete(o){
      if (this.hasChildren(o)) { this.$emit('toast',{msg:'该机构下有下级机构，无法删除',type:'warning'}); return }
      this.deleteTarget=o; this.showDelete=true
    },
    async confirmDelete(){
      if(this.deleteTarget){
        try {
          const id = this.deleteTarget.id || this.deleteTarget.code
          await api.delete('/organizations/' + id)
          this.orgs=this.orgs.filter(x=>(x.id||x.code)!==id)
          this.$emit('toast',{msg:'已删除机构「'+this.deleteTarget.label+'」',type:'success'})
        } catch (e) {
          this.$emit('toast',{msg:'删除失败: ' + (e.message || ''),type:'error'})
        }
      }
      this.showDelete=false; this.deleteTarget=null
    },
  }
}
</script>
