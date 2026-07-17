-- 订单管理系统 DDL
CREATE DATABASE IF NOT EXISTS order_manager DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE order_manager;


-- ========== 系统管理表 ==========

DROP TABLE IF EXISTS tb_role_function;
DROP TABLE IF EXISTS tb_operation_log;
DROP TABLE IF EXISTS tb_function;
DROP TABLE IF EXISTS tb_menu;
DROP TABLE IF EXISTS tb_role;
DROP TABLE IF EXISTS tb_organization;
DROP TABLE IF EXISTS tb_department;
DROP TABLE IF EXISTS tb_user;
DROP TABLE IF EXISTS tb_order;
DROP TABLE IF EXISTS tb_partner;

CREATE TABLE tb_user (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(30)  NOT NULL UNIQUE COMMENT '用户编号',
    name        VARCHAR(50)  NOT NULL COMMENT '用户名',
    password    VARCHAR(200) NOT NULL COMMENT '密码(bcrypt)',
    role        VARCHAR(20)  NOT NULL COMMENT '角色',
    dept        VARCHAR(50)  DEFAULT '' COMMENT '部门',
    org         VARCHAR(20)  DEFAULT '' COMMENT '机构编码',
    active      TINYINT(1)   DEFAULT 1 COMMENT '启用/禁用',
    locked      TINYINT(1)   DEFAULT 0 COMMENT '锁定',
    first_login TINYINT(1)   DEFAULT 1 COMMENT '首次登录',
    remark      VARCHAR(500) DEFAULT '' COMMENT '备注',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

CREATE TABLE tb_department (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(20)  NOT NULL UNIQUE COMMENT '部门编号',
    name        VARCHAR(50)  NOT NULL COMMENT '部门名称',
    remark      VARCHAR(500) DEFAULT '' COMMENT '备注',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门';

CREATE TABLE tb_organization (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(20)  NOT NULL UNIQUE COMMENT '机构编号',
    label       VARCHAR(100) NOT NULL COMMENT '机构名称',
    sname       VARCHAR(50)  NOT NULL COMMENT '机构简称',
    level       VARCHAR(10)  NOT NULL COMMENT '层级: hq/branch1/branch2/sub1/sub2',
    parent      VARCHAR(20)  DEFAULT '' COMMENT '父级编码',
    contact     VARCHAR(50)  DEFAULT '' COMMENT '联系人',
    phone       VARCHAR(20)  DEFAULT '' COMMENT '联系电话',
    region      VARCHAR(100) NOT NULL COMMENT '所在地区',
    address     VARCHAR(200) NOT NULL COMMENT '详细地址',
    remark      VARCHAR(500) DEFAULT '' COMMENT '备注',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构';

CREATE TABLE tb_role (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(30)  NOT NULL UNIQUE COMMENT '角色编号',
    name        VARCHAR(50)  NOT NULL COMMENT '角色名称',
    remark      VARCHAR(500) DEFAULT '' COMMENT '备注',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色';

CREATE TABLE tb_role_function (
    id        BIGINT      AUTO_INCREMENT PRIMARY KEY,
    role_id   BIGINT      NOT NULL COMMENT '角色ID',
    func_code VARCHAR(50) NOT NULL COMMENT '功能编码',
    CONSTRAINT fk_rf_role FOREIGN KEY (role_id) REFERENCES tb_role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联';

CREATE TABLE tb_menu (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(30)  NOT NULL UNIQUE COMMENT '菜单编号',
    label       VARCHAR(50)  NOT NULL COMMENT '菜单名称',
    route       VARCHAR(50)  DEFAULT '' COMMENT '路由路径',
    type        VARCHAR(10)  NOT NULL COMMENT 'level1/level2',
    parent      VARCHAR(30)  DEFAULT '' COMMENT '父级菜单编码',
    remark      VARCHAR(500) DEFAULT '' COMMENT '备注',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单';

CREATE TABLE tb_function (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(50)  NOT NULL UNIQUE COMMENT '功能编码',
    name        VARCHAR(50)  NOT NULL COMMENT '功能名称',
    menu        VARCHAR(50)  NOT NULL COMMENT '所属菜单',
    perm        VARCHAR(50)  NOT NULL UNIQUE COMMENT '权限标识',
    remark      VARCHAR(500) DEFAULT '' COMMENT '备注',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='功能权限';

CREATE TABLE tb_operation_log (
    id     BIGINT       AUTO_INCREMENT PRIMARY KEY,
    user   VARCHAR(50)  NOT NULL COMMENT '操作人',
    action VARCHAR(50)  NOT NULL COMMENT '操作类型',
    target VARCHAR(100) NOT NULL COMMENT '操作目标',
    ip     VARCHAR(50)  DEFAULT '' COMMENT 'IP地址',
    time   DATETIME     NOT NULL COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';

-- ========== 业务表 ==========

CREATE TABLE tb_partner (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(20)  NOT NULL UNIQUE COMMENT '合作方编号',
    name        VARCHAR(100) NOT NULL UNIQUE COMMENT '合作方名称',
    contact     VARCHAR(50)  NOT NULL COMMENT '联系人',
    phone       VARCHAR(11)  NOT NULL COMMENT '联系电话',
    remark      VARCHAR(500) DEFAULT '' COMMENT '备注',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合作方';

CREATE TABLE tb_order (
    id          BIGINT        AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(30)   NOT NULL UNIQUE COMMENT '订单编号',
    name        VARCHAR(200)  NOT NULL COMMENT '订单名称',
    type        VARCHAR(20)   NOT NULL COMMENT '订单类型',
    partner_id  BIGINT        NOT NULL COMMENT '合作方ID',
    amount      DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '订单金额',
    status      VARCHAR(10)   NOT NULL DEFAULT 'pending' COMMENT '状态',
    order_time  DATETIME      NOT NULL COMMENT '订单时间',
    remark      VARCHAR(500)  DEFAULT '' COMMENT '备注',
    create_time DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_partner FOREIGN KEY (partner_id) REFERENCES tb_partner(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单';

-- 索引
CREATE INDEX idx_order_code ON tb_order(code);
CREATE INDEX idx_order_type ON tb_order(type);
CREATE INDEX idx_order_status ON tb_order(status);
CREATE INDEX idx_order_partner ON tb_order(partner_id);
CREATE INDEX idx_order_time ON tb_order(order_time);
CREATE INDEX idx_partner_code ON tb_partner(code);
CREATE INDEX idx_partner_name ON tb_partner(name);

-- ========== 预置数据 ==========

-- 角色
INSERT INTO tb_role (code, name) VALUES
('SYSTEM_ADMIN', '系统管理员'),
('OPERATOR', '操作员'),
('APPROVER', '审批员'),
('NORMAL_USER', '普通用户');

-- 用户（密码: Uu888888! bcrypt）
INSERT INTO tb_user (code, name, password, role, dept, first_login) VALUES
('USER_ADMIN', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '系统管理员', '研发部', 0),
('USER_ZHANGSAN', 'zhangsan', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '普通用户', '研发部', 1);

-- 部门
INSERT INTO tb_department (code, name) VALUES
('DEPT_RD', '研发部'), ('DEPT_SALES', '销售部'), ('DEPT_FINANCE', '财务部'),
('DEPT_HR', '人事部'), ('DEPT_AFTERSALE', '售后部'), ('DEPT_MARKETING', '市场部'),
('DEPT_ADMIN', '行政部'), ('DEPT_LOGISTICS', '物流部');

-- 合作方
INSERT INTO tb_partner (code, name, contact, phone) VALUES
('PARTNER-001', '合作方A', '张三', '13800138001'),
('PARTNER-002', '合作方B', '李四', '13800138002'),
('PARTNER-003', '合作方C', '王五', '13800138003'),
('PARTNER-004', '合作方D', '赵六', '13800138004'),
('PARTNER-005', '合作方E', '孙七', '13800138005'),
('PARTNER-006', '合作方F', '周八', '13800138006'),
('PARTNER-007', '合作方G', '吴九', '13800138007'),
('PARTNER-008', '合作方H', '郑十', '13800138008');

-- 机构（5级树形）
INSERT INTO tb_organization (code, name, short_name, level, parent_id, contact, phone, region, address) VALUES
('HQ', '威高集团', '集团', 1, NULL, '王总', '021-88888888', '上海市', '上海市浦东新区'),
('SH_BRANCH', '上海分公司', '上海分', 2, 1, '李总', '021-88888881', '上海市', '上海市浦东新区'),
('SH_SUB', '上海二级分行', '上二分', 3, 2, '刘总', '021-88888882', '上海市', '上海市浦东新区'),
('SH_ORG1', '上海一级支行', '上支行', 4, 3, '陈行', '021-88888883', '上海市', '上海市浦东新区'),
('SH_SUBORG1', '上海二级支行', '上二支', 5, 4, '周主', '021-88888884', '上海市', '上海市浦东新区');

-- 菜单（二级）
INSERT INTO tb_menu (code, label, path, level) VALUES
('MENU_DASHBOARD', '首页', '/dashboard', 1),
('MENU_ORDERS', '订单管理', '/orders', 1),
('MENU_PARTNERS', '合作方管理', '/partners', 1),
('MENU_REPORTS', '报表统计', NULL, 1),
('MENU_SYS', '系统管理', NULL, 1);
INSERT INTO tb_menu (code, label, path, level, parent_id) VALUES
('MENU_REPORT_TYPE', '订单类型统计', '/reports/type', 2, 4),
('MENU_REPORT_PARTNER', '合作方统计', '/reports/partner', 2, 4),
('MENU_REPORT_STATUS', '订单状态统计', '/reports/status', 2, 4),
('MENU_USER', '用户管理', '/users', 2, 5),
('MENU_ROLE', '角色管理', '/roles', 2, 5),
('MENU_DEPT', '部门管理', '/departments', 2, 5),
('MENU_ORG', '机构管理', '/organizations', 2, 5),
('MENU_MENU', '菜单管理', '/menus', 2, 5),
('MENU_FUNC', '功能管理', '/functions', 2, 5),
('MENU_LOG', '操作日志', '/logs', 2, 5);

-- 功能权限
INSERT INTO tb_function (code, name, menu_id, perm) VALUES
('FUNC_DASHBOARD', '查看首页', 1, 'dashboard:view'),
('FUNC_ORDER_VIEW', '查看订单', 2, 'order:view'),
('FUNC_ORDER_ADD', '新增订单', 2, 'order:add'),
('FUNC_ORDER_EDIT', '编辑订单', 2, 'order:edit'),
('FUNC_ORDER_DELETE', '删除订单', 2, 'order:delete'),
('FUNC_PARTNER_VIEW', '查看合作方', 3, 'partner:view'),
('FUNC_PARTNER_ADD', '新增合作方', 3, 'partner:add'),
('FUNC_PARTNER_EDIT', '编辑合作方', 3, 'partner:edit'),
('FUNC_PARTNER_DELETE', '删除合作方', 3, 'partner:delete'),
('FUNC_USER_VIEW', '查看用户', 9, 'user:view'),
('FUNC_USER_ADD', '新增用户', 9, 'user:add'),
('FUNC_USER_EDIT', '编辑用户', 9, 'user:edit'),
('FUNC_USER_DELETE', '删除用户', 9, 'user:delete'),
('FUNC_ROLE_VIEW', '查看角色', 10, 'role:view'),
('FUNC_SYS_ADMIN', '系统管理', 5, 'sys:admin');
