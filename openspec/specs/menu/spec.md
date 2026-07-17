# 菜单管理 - Menu

## Purpose
管理系统菜单的树形结构。

## Requirements

### REQ-MENU-001: 菜单树
The system SHALL display menus in a two-level tree: 一级菜单(业务模块) and 二级菜单(模块内功能).

### REQ-MENU-002: 菜单字段
The system SHALL support fields: 菜单编号(自动生成), 菜单名称(必填), 路由路径, 菜单级别(一级/二级), 上级菜单, 备注.

### REQ-MENU-003: 新增菜单
The system SHALL allow creating menus.

### REQ-MENU-004: 编辑菜单
The system SHALL allow editing menus.

### REQ-MENU-005: 删除菜单
The system SHALL allow deleting menus.

## Scenarios

### SCEN-MENU-001: 查看菜单树
- Given 系统有菜单数据
- When 用户进入菜单管理
- Then 显示两级菜单树

### SCEN-MENU-002: 新增菜单
- When 点击新增，填写菜单信息
- Then 菜单添加到树中

### SCEN-MENU-003: 编辑菜单
- When 点击编辑，修改菜单名称
- Then 菜单更新
