package com.ordermanager.entity;

import java.time.LocalDateTime;

public class Organization {
    private Long id;
    private String code;
    private String label;
    private String sname;
    private String level;
    private String parent;
    private String contact;
    private String phone;
    private String region;
    private String address;
    private String remark;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getSname() { return sname; }
    public void setSname(String sname) { this.sname = sname; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getParent() { return parent; }
    public void setParent(String parent) { this.parent = parent; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
