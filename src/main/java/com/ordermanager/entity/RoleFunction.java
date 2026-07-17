package com.ordermanager.entity;

public class RoleFunction {
    private Long id;
    private Long roleId;
    private String funcCode;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public String getFuncCode() { return funcCode; }
    public void setFuncCode(String funcCode) { this.funcCode = funcCode; }
}
