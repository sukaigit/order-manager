package com.ordermanager.entity;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String code;
    private String name;
    private String password;
    private String role;
    private String dept;
    private String org;
    private Integer active;
    private Integer locked;
    private Integer firstLogin;
    private String remark;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }
    public String getOrg() { return org; }
    public void setOrg(String org) { this.org = org; }
    public Integer getActive() { return active; }
    public void setActive(Integer active) { this.active = active; }
    public Integer getLocked() { return locked; }
    public void setLocked(Integer locked) { this.locked = locked; }
    public Integer getFirstLogin() { return firstLogin; }
    public void setFirstLogin(Integer firstLogin) { this.firstLogin = firstLogin; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
