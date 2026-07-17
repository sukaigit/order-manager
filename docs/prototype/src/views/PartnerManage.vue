<template>
  <div class="page-body">
    <div class="filter-bar">
      <div class="form-group"><label class="form-label">合作方编号</label><input class="form-input" v-model="fCode" placeholder="搜索" style="width:120px" /></div>
      <div class="form-group"><label class="form-label">合作方名称</label><input class="form-input" v-model="fName" placeholder="搜索" /></div>
      <div class="form-group"><label class="form-label">联系人</label><input class="form-input" v-model="fContact" placeholder="搜索" style="width:120px" /></div>
      <button class="btn btn-primary" @click="query">查询</button>
      <button class="btn btn-secondary" @click="resetQuery">重置</button>
      <div style="flex:1"></div>
      <button class="btn btn-primary" @click="openAdd">新增合作方</button>
      <button class="btn btn-secondary" @click="doExport"><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="vertical-align:middle;margin-right:4px"><path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></svg>导出</button>
    </div>
    <table class="data-table">
      <tr><th>合作方编号</th><th>合作方名称</th><th>联系人</th><th>联系电话</th><th>备注</th><th>操作</th></tr>
      <tr v-for="p in pagedList" :key="p.id || p.code">
        <td style="font-size:12px;color:var(--color-text-muted)">{{ p.code }}</td><td>{{ p.name }}</td><td>{{ p.contact }}</td><td>{{ p.phone }}</td>
        <td style="font-size:12px;max-width:140px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ p.remark }}</td>
        <td style="display:flex;flex-wrap:wrap;gap:4px;justify-content:center">
          <button class="btn btn-text btn-sm" @click="openEdit(p)">编辑</button>
          <button class="btn btn-text btn-sm" style="color:var(--color-danger)" @click="doDelete(p)">删除</button>
        </td>
      </tr>
      <tr v-if="partners.length===0"><td :colspan="6" style="text-align:center;padding:32px;color:var(--color-text-muted)">暂无数据</td></tr>
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
      <div class="modal" style="min-width:480px">
        <div class="modal-title">{{ formMode==='add'?'新增合作方':'编辑合作方' }}</div>
        <div style="display:flex;flex-direction:column;gap:14px">
          <div v-if="formMode==='edit'"><label class="form-label">合作方编号</label><input class="form-input" :value="form.code" disabled style="color:var(--color-text-muted)" /></div>
          <div><label class="form-label">合作方名称 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.name" /></div>
          <div style="display:flex;gap:16px">
            <div style="flex:1"><label class="form-label">联系人 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.contact" /></div>
            <div style="flex:1"><label class="form-label">联系电话 <span style="color:var(--color-danger)">*</span></label><input class="form-input" v-model="form.phone" placeholder="11位手机号" /></div>
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
        <div style="font-size:14px;color:var(--color-text-secondary);margin-bottom:24px">确定要删除合作方「{{ deleteTarget?.name }}」吗？<br>此操作不可撤销。</div>
        <div class="modal-footer" style="justify-content:center">
          <button class="btn btn-secondary" @click="showDelete=false">取消</button>
          <button class="btn btn-danger" :disabled="deleting" @click="confirmDelete">确认删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data: () => ({
    partners: [],
    totalCount: 0,
    loading: false,
    deleting: false,
    page: 1,
    pageSize: 5,
    fCode: '',
    fName: '',
    fContact: '',
    showForm: false,
    showDelete: false,
    formMode: 'add',
    deleteTarget: null,
    form: { code: '', name: '', contact: '', phone: '', remark: '' }
  }),
  computed: {
    totalPages () {
      return Math.ceil(this.totalCount / this.pageSize) || 1
    },
    startRecord () {
      return this.totalCount === 0 ? 0 : (this.page - 1) * this.pageSize + 1
    },
    endRecord () {
      return Math.min(this.page * this.pageSize, this.totalCount)
    },
    pagedList () {
      const s = (this.page - 1) * this.pageSize
      return this.partners.slice(s, s + this.pageSize)
    },
    pageNumbers () {
      const tp = this.totalPages
      const cp = this.page
      if (tp <= 7) return Array.from({ length: tp }, (_, i) => i + 1)
      const p = [1]
      if (cp > 3) p.push('...')
      for (let i = Math.max(2, cp - 1); i <= Math.min(tp - 1, cp + 1); i++) p.push(i)
      if (cp < tp - 2) p.push('...')
      p.push(tp)
      return p
    }
  },
  mounted () {
    this.loadData()
  },
  methods: {
    async loadData () {
      this.loading = true
      try {
        const params = { page: this.page, pageSize: this.pageSize }
        if (this.fCode) params.code = this.fCode
        if (this.fName) params.name = this.fName
        if (this.fContact) params.contact = this.fContact
        const res = await window.api.get('/partners', { params })
        const body = res.data || res
        this.partners = body.list || body.data?.list || []
        this.totalCount = body.total || body.data?.total || 0
      } catch (e) {
        this.$emit('toast', { msg: '加载合作方失败：' + (e.response?.data?.msg || e.message), type: 'error' })
      } finally {
        this.loading = false
      }
    },
    query () {
      this.page = 1
      this.loadData()
    },
    resetQuery () {
      this.fCode = ''
      this.fName = ''
      this.fContact = ''
      this.page = 1
      this.loadData()
    },
    openAdd () {
      this.formMode = 'add'
      this.form = { code: '', name: '', contact: '', phone: '', remark: '' }
      this.showForm = true
    },
    openEdit (p) {
      this.formMode = 'edit'
      this.form = { ...p }
      this.showForm = true
    },
    async saveForm () {
      if (!this.form.name || !this.form.contact || !this.form.phone) {
        this.$emit('toast', { msg: '请填写必填信息', type: 'warning' })
        return
      }
      if (!/^1\d{10}$/.test(this.form.phone)) {
        this.$emit('toast', { msg: '请输入正确的11位手机号', type: 'warning' })
        return
      }
      const data = { name: this.form.name, contact: this.form.contact, phone: this.form.phone, remark: this.form.remark }
      try {
        if (this.formMode === 'add') {
          await window.api.post('/partners', data)
          this.$emit('toast', { msg: '新增合作方成功', type: 'success' })
        } else {
          await window.api.put('/partners/' + this.form.id, data)
          this.$emit('toast', { msg: '编辑成功', type: 'success' })
        }
        this.showForm = false
        await this.loadData()
      } catch (e) {
        this.$emit('toast', { msg: '操作失败：' + (e.response?.data?.msg || '服务器错误'), type: 'error' })
      }
    },
    doDelete (p) {
      this.deleteTarget = p
      this.showDelete = true
    },
    async confirmDelete () {
      if (!this.deleteTarget) return
      this.deleting = true
      try {
        await window.api.delete('/partners/' + this.deleteTarget.id)
        this.$emit('toast', { msg: '已删除合作方「' + this.deleteTarget.name + '」', type: 'success' })
        this.showDelete = false
        this.deleteTarget = null
        await this.loadData()
      } catch (e) {
        const msg = e.response?.data?.msg || e.message
        this.$emit('toast', { msg: '删除失败：' + msg, type: 'error' })
      } finally {
        this.deleting = false
      }
    },
    doExport () {
      try {
        const d = this.partners.map(p => [p.code, p.name, p.contact, p.phone, p.remark])
        d.unshift(['合作方编号', '合作方名称', '联系人', '联系电话', '备注'])
        const rows = d.map(r =>
          '<Row>' + r.map(c =>
            '<Cell><Data ss:Type="String">' + String(c == null ? '' : c).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;') + '</Data></Cell>'
          ).join('') + '</Row>'
        ).join('')
        const xml = '<?xml version="1.0" encoding="UTF-8"?><?mso-application progid="Excel.Sheet"?><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"><Worksheet ss:Name="合作方列表"><Table>' + rows + '</Table></Worksheet></Workbook>'
        const b = new Blob([xml], { type: 'application/vnd.ms-excel;charset=utf-8' })
        const u = URL.createObjectURL(b)
        const a = document.createElement('a')
        a.href = u
        a.download = '合作方列表_' + new Date().toISOString().slice(0, 10) + '.xls'
        document.body.appendChild(a)
        a.click()
        document.body.removeChild(a)
        URL.revokeObjectURL(u)
        this.$emit('toast', { msg: '导出成功：' + d.length + ' 条记录', type: 'success' })
      } catch (e) {
        this.$emit('toast', { msg: '导出失败：' + (e.response?.data?.msg || e.message), type: 'error' })
      }
    }
  }
}
</script>
