# 机构管理 - Organization

## Purpose
管理5级树形结构的机构。

## Requirements

### REQ-ORG-001: 机构树
The system SHALL display organizations in a 5-level tree structure: 总行, 一级分行, 二级分行, 一级支行, 二级支行.

### REQ-ORG-002: 机构字段
The system SHALL support fields: 机构编号(必填), 机构名称(必填), 机构简称(必填), 层级, 上级机构, 联系人, 联系电话, 所在地区(必填), 详细地址(必填), 备注.

### REQ-ORG-003: 新增机构
The system SHALL allow creating organizations with all fields.

### REQ-ORG-004: 编辑机构
The system SHALL allow editing organizations.

### REQ-ORG-005: 删除机构
The system SHALL allow deleting organizations without child nodes.

## Scenarios

### SCEN-ORG-001: 查看机构树
- Given 系统有机构数据
- When 用户进入机构管理
- Then 显示树形结构

### SCEN-ORG-002: 新增机构
- When 点击新增，填写机构信息
- Then 机构添加到树中

### SCEN-ORG-003: 编辑机构
- When 点击编辑，修改字段
- Then 机构信息更新

### SCEN-ORG-004: 删除无下级机构
- Given 机构没有下级
- When 点击删除并确认
- Then 机构删除
