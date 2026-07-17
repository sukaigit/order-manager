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
      <tr v-for="p in pagedList" :key="p.code">
        <td style="font-size:12px;color:var(--color-text-muted)">{{ p.code }}</td><td>{{ p.name }}</td><td>{{ p.contact }}</td><td>{{ p.phone }}</td>
        <td style="font-size:12px;max-width:140px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ p.remark }}</td>
        <td style="display:flex;flex-wrap:wrap;gap:4px;justify-content:center">
          <button class="btn btn-text btn-sm" @click="openEdit(p)">编辑</button>
          <button class="btn btn-text btn-sm" style="color:var(--color-danger)" @click="doDelete(p)">删除</button>
        </td>
      </tr>
    </table>
    <div class="pagination">
      <div style="display:flex;align-items:center;gap:8px;font-size:13px;color:var(--color-text-muted)">
        <select class="form-select" v-model.number="pageSize" @change="page=1" style="padding:4px 8px;font-size:12px;width:auto">
          <option :value="5">5条/页</option><option :value="10">10条/页</option><option :value="20">20条/页</option><option :value="50">50条/页</option>
        </select>
        <span>显示第 {{ (page-1)*pageSize+1 }}-{{ Math.min(page*pageSize,totalCount) }} 条，共 {{ totalCount }} 条</span>
      </div>
      <div style="display:flex;gap:4px">
        <button class="page-btn" :disabled="page<=1" @click="page=Math.max(1,page-1)">上一页</button>
        <button class="page-btn" v-for="n in pageNumbers" :key="n" :class="{active:n===page}" @click="typeof n==='number'&&(page=n)" v-text="n"></button>
        <button class="page-btn" :disabled="page>=totalPages" @click="page=Math.min(totalPages,page+1)">下一页</button>
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
        <div style="font-size:14px;color:var(--color-text-secondary);margin-bottom:24px">
          <div>{{ deleteMsg }}</div>
          <div v-if="!deleteBlocked" style="margin-top:6px;font-size:13px">此操作不可撤销。</div>
        </div>
        <div class="modal-footer" style="justify-content:center">
          <button class="btn btn-secondary" @click="showDelete=false">取消</button>
          <button v-if="!deleteBlocked" class="btn btn-danger" @click="confirmDelete">确认删除</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data: () => ({
    fCode:'', fName:'', fContact:'',
    showForm:false, showDelete:false, formMode:'add', deleteTarget:null, deleteBlocked:false,
    form:{code:'',name:'',contact:'',phone:'',remark:''},
    page:1, pageSize:5,
    partners:[
      {code:'PARTNER-001',name:'合作方A',contact:'张三',phone:'13800138001',remark:''},
      {code:'PARTNER-002',name:'合作方B',contact:'李四',phone:'13800138002',remark:''},
      {code:'PARTNER-003',name:'合作方C',contact:'王五',phone:'13800138003',remark:''},
      {code:'PARTNER-004',name:'合作方D',contact:'赵六',phone:'13800138004',remark:''},
      {code:'PARTNER-005',name:'合作方E',contact:'孙七',phone:'13800138005',remark:''},
      {code:'PARTNER-006',name:'合作方F',contact:'周八',phone:'13800138006',remark:''},
      {code:'PARTNER-007',name:'合作方G',contact:'吴九',phone:'13800138007',remark:''},
      {code:'PARTNER-008',name:'合作方H',contact:'郑十',phone:'13800138008',remark:''},
    ],
    // 模拟有订单的合作方
    partnersWithOrders: ['合作方A','合作方B','合作方C','合作方D'],
  }),
  computed:{
    totalCount(){return this.filteredList.length},
    totalPages(){return Math.ceil(this.filteredList.length/this.pageSize)||1},
    pagedList(){const s=(this.page-1)*this.pageSize;return this.filteredList.slice(s,s+this.pageSize)},
    filteredList(){return this.partners.filter(p=>{if(this.fCode&&!p.code.includes(this.fCode.toUpperCase()))return false;if(this.fName&&!p.name.includes(this.fName))return false;if(this.fContact&&!p.contact.includes(this.fContact))return false;return true})},
    pageNumbers(){const tp=this.totalPages,cp=this.page;if(tp<=7)return Array.from({length:tp},(_,i)=>i+1);const p=[1];if(cp>3)p.push('...');for(let i=Math.max(2,cp-1);i<=Math.min(tp-1,cp+1);i++)p.push(i);if(cp<tp-2)p.push('...');p.push(tp);return p},
    deleteMsg(){return this.deleteBlocked?'该合作方已有订单，不可删除':'确定要删除合作方「'+this.deleteTarget?.name+'」吗？'},
  },
  methods:{
    query(){this.page=1},
    resetQuery(){this.fCode='';this.fName='';this.fContact='';this.page=1},
    openAdd(){this.formMode='add';this.form={code:'',name:'',contact:'',phone:'',remark:''};this.showForm=true},
    openEdit(p){this.formMode='edit';this.form={...p};this.showForm=true},
    saveForm(){if(!this.form.name||!this.form.contact||!this.form.phone){this.$emit('toast',{msg:'请填写必填信息',type:'warning'});return};if(!/^1\d{10}$/.test(this.form.phone)){this.$emit('toast',{msg:'请输入正确的11位手机号',type:'warning'});return};if(this.formMode==='add'){const n=String(this.partners.length+1).padStart(3,'0');this.partners.push({...this.form,code:'PARTNER-'+String(1000+this.partners.length+1)});this.$emit('toast',{msg:'新增合作方成功',type:'success'})}else{const i=this.partners.findIndex(x=>x.code===this.form.code);if(i>=0)this.partners.splice(i,1,{...this.form});this.$emit('toast',{msg:'编辑成功',type:'success'})};this.showForm=false},
    doDelete(p){this.deleteTarget=p;this.deleteBlocked=this.partnersWithOrders.includes(p.name);this.showDelete=true},
    confirmDelete(){if(!this.deleteBlocked&&this.deleteTarget){this.partners=this.partners.filter(x=>x.code!==this.deleteTarget.code);this.$emit('toast',{msg:'已删除合作方「'+this.deleteTarget.name+'」',type:'success'})};this.showDelete=false;this.deleteTarget=null},
    doExport(){
      const d=this.filteredList.map(p=>[p.code,p.name,p.contact,p.phone,p.remark])
      d.unshift(['合作方编号','合作方名称','联系人','联系电话','备注'])
      const rows=d.map(r=>'<Row>'+r.map(c=>'<Cell><Data ss:Type="String">'+String(c).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;')+'</Data></Cell>').join('')+'</Row>').join('')
      const xml='<?xml version="1.0" encoding="UTF-8"?><?mso-application progid="Excel.Sheet"?><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"><Worksheet ss:Name="合作方列表"><Table>'+rows+'</Table></Worksheet></Workbook>'
      const b=new Blob([xml],{type:'application/vnd.ms-excel;charset=utf-8'}),u=URL.createObjectURL(b),a=document.createElement('a')
      a.href=u;a.download='合作方列表_'+new Date().toISOString().slice(0,10)+'.xls';document.body.appendChild(a);a.click();document.body.removeChild(a);URL.revokeObjectURL(u)
      this.$emit('toast',{msg:'导出成功：'+d.length+' 条记录',type:'success'})
    },
  }
}
</script>