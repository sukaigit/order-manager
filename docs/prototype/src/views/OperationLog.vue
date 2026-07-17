<template>
  <div class="page-body">
    <div class="filter-bar">
      <div class="form-group"><label class="form-label">操作人</label><input class="form-input" v-model="fUser" placeholder="搜索" /></div>
      <div class="form-group"><label class="form-label">操作类型</label><input class="form-input" v-model="fAction" placeholder="搜索" style="width:130px" /></div>
      <div class="form-group"><label class="form-label">操作目标</label><input class="form-input" v-model="fTarget" placeholder="搜索" style="width:130px" /></div>
      <div class="form-group"><label class="form-label">IP</label><input class="form-input" v-model="fIP" placeholder="搜索" style="width:140px" /></div>
      <div class="form-group"><label class="form-label">日期</label><input class="form-input" type="date" v-model="fDate" style="width:150px" /></div>
      <button class="btn btn-primary" @click="query">查询</button>
      <button class="btn btn-secondary" @click="resetQuery">重置</button>
    </div>
    <div v-if="loading" style="text-align:center;padding:40px;color:var(--color-text-muted)">加载中...</div>
    <table class="data-table" v-else>
      <tr><th>操作人</th><th>操作类型</th><th>操作目标</th><th>IP</th><th>时间</th></tr>
      <tr v-for="l in pagedList" :key="l.id"><td>{{ l.user }}</td><td>{{ l.action }}</td><td>{{ l.target }}</td><td>{{ l.ip }}</td><td>{{ l.time }}</td></tr>
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
  </div>
</template>
<script>
import api from '../utils/api'
export default {
  data: () => ({
    loading: false,
    page: 1, pageSize: 5,
    fUser:'', fAction:'', fTarget:'', fIP:'', fDate:'',
    logs: []
  }),
  mounted() { this.loadData() },
  computed: {
    totalPages() { return Math.ceil(this.filteredList.length / this.pageSize) || 1 },
    filteredList() {
      return this.logs.filter(l => {
        if(this.fUser && !l.user.includes(this.fUser)) return false
        if(this.fAction && !l.action.includes(this.fAction)) return false
        if(this.fTarget && !l.target.includes(this.fTarget)) return false
        if(this.fIP && !l.ip.includes(this.fIP)) return false
        if(this.fDate && l.time.slice(0,10) !== this.fDate) return false
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
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await api.get('/logs')
        this.logs = (res.data?.list || res.data || res || [])
      } catch (e) {
        this.$emit('toast',{msg:'加载日志失败: ' + (e.message || ''),type:'error'})
      } finally {
        this.loading = false
      }
    },
    query() { this.page = 1 },
    resetQuery() { this.fUser=''; this.fAction=''; this.fTarget=''; this.fIP=''; this.fDate=''; this.page = 1 },
  }
}
</script>
