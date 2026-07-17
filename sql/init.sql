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
