package com.ordermanager.entity;

import java.time.LocalDateTime;

public class OperationLog {
    private Long id;
    private String user;
    private String action;
    private String target;
    private String ip;
    private LocalDateTime time;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
}
