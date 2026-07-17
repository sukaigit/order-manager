<template>
  <div class="page-body">
    <div style="display:grid;grid-template-columns:repeat(4,1fr);gap:16px;margin-bottom:24px">
      <div class="card"><div class="card-title">累计订单数</div><div style="font-size:32px;font-weight:700;color:var(--color-primary)">{{ stats.totalOrders || 0 }}</div></div>
      <div class="card"><div class="card-title">累计订单金额</div><div style="font-size:32px;font-weight:700;color:var(--color-primary)">¥{{ (stats.totalAmount || 0).toLocaleString() }}</div></div>
      <div class="card"><div class="card-title">累计已结清订单数</div><div style="font-size:32px;font-weight:700;color:#34c759">{{ stats.settledOrders || 0 }}</div></div>
      <div class="card"><div class="card-title">累计已结清订单金额</div><div style="font-size:32px;font-weight:700;color:#34c759">¥{{ (stats.settledAmount || 0).toLocaleString() }}</div></div>
    </div>
    <div v-if="stats.typeStats && stats.typeStats.length" class="card" style="padding:16px;margin-bottom:24px">
      <div class="card-title" style="padding:0;margin-bottom:12px">订单分布</div>
      <div style="font-size:13px">
        <div v-for="(s,i) in stats.typeStats" :key="s.name" style="display:flex;align-items:center;padding:10px 0" :style="i<stats.typeStats.length-1?'border-bottom:1px solid var(--color-border-light)':''">
          <span style="width:80px;font-weight:600">{{ s.name }}</span>
          <span style="width:80px;color:var(--color-text-muted)">{{ s.count }} 单</span>
          <div style="flex:1;height:14px;background:var(--color-bg);border-radius:4px;overflow:hidden;margin:0 12px"><div :style="{height:'100%',width:stats.totalAmount?Math.round(s.amount/stats.totalAmount*100)+'%':'0%',background:colors[i],borderRadius:'4px'}"></div></div>
          <span style="width:90px;text-align:right;font-weight:600">¥{{ (s.amount||0).toLocaleString() }}</span>
        </div>
      </div>
    </div>
    <div style="display:grid;grid-template-columns:2fr 1fr;gap:16px;margin-bottom:24px">
      <div class="card"><div class="card-title">近期订单</div>
      <table class="data-table"><tr><th>订单编号</th><th>订单类型</th><th>合作方</th><th>金额</th><th>状态</th><th>时间</th></tr>
        <tr v-if="!stats.recentOrders || !stats.recentOrders.length"><td :colspan="6" style="text-align:center;padding:32px;color:var(--color-text-muted)">暂无数据</td></tr>
        <tr v-for="o in stats.recentOrders || []" :key="o.code"><td style="font-size:12px;color:var(--color-text-muted)">{{ o.code }}</td><td>{{ o.type }}</td><td>{{ o.partner }}</td><td>¥{{ (o.amount || 0).toLocaleString() }}</td><td><span class="badge" :class="'badge-'+o.statusCls">{{ o.status }}</span></td><td>{{ o.date }}</td></tr>
      </table>
      </div>
      <div class="card"><div class="card-title">合作方排名</div>
        <div v-for="(p,i) in stats.partnerRank || []" :key="p.name" style="display:flex;justify-content:space-between;padding:8px 0;border-bottom:1px solid var(--color-border-light);font-size:13px">
          <span>{{ i+1 }}. {{ p.name }}</span><span style="color:var(--color-text-muted)">{{ p.count }} 单 / ¥{{ (p.amount || 0).toLocaleString() }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data: () => ({
    stats: { totalOrders:0, totalAmount:0, settledOrders:0, settledAmount:0, recentOrders:[], typeStats:[], partnerRank:[] },
    colors: ['var(--color-primary)','#34c759','#ff9500','#ff3b30','#5856d6']
  }),
  mounted() { this.loadData() },
  methods: {
    async loadData() {
      try {
        const res = await api.get('/dashboard')
        this.stats = res.data || res
      } catch(e) {
        this.$emit('toast', { msg:'加载失败', type:'error' })
      }
    }
  }
}
</script>
