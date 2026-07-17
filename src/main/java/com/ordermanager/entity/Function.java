package com.ordermanager.entity;

import java.time.LocalDateTime;

public class Function {
    private Long id;
    private String code;
    private String name;
    private String menu;
    private String perm;
    private String remark;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMenu() { return menu; }
    public void setMenu(String menu) { this.menu = menu; }
    public String getPerm() { return perm; }
    public void setPerm(String perm) { this.perm = perm; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
