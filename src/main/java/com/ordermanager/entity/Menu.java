package com.ordermanager.entity;

import java.time.LocalDateTime;

public class Menu {
    private Long id;
    private String code;
    private String label;
    private String route;
    private String type;
    private String parent;
    private String remark;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getParent() { return parent; }
    public void setParent(String parent) { this.parent = parent; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
