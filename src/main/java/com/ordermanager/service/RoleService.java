package com.ordermanager.service;

import com.ordermanager.common.PageResult;
import com.ordermanager.entity.Role;
import com.ordermanager.entity.RoleFunction;
import com.ordermanager.mapper.RoleFunctionMapper;
import com.ordermanager.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleService {

    private final RoleMapper roleMapper;
    private final RoleFunctionMapper roleFunctionMapper;

    public RoleService(RoleMapper roleMapper, RoleFunctionMapper roleFunctionMapper) {
        this.roleMapper = roleMapper;
        this.roleFunctionMapper = roleFunctionMapper;
    }

    public PageResult<Role> page(String code, String name, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Role> list = roleMapper.selectList(code, name, offset, pageSize);
        long total = roleMapper.selectCount(code, name);
        return new PageResult<>(list, total);
    }

    public Role getById(Long id) {
        return roleMapper.selectById(id);
    }

    public int create(Role role) {
        role.setCreateTime(LocalDateTime.now());
        return roleMapper.insert(role);
    }

    public int update(Role role) {
        return roleMapper.update(role);
    }

    public int delete(Long id) {
        return roleMapper.deleteById(id);
    }

    public List<Role> getAll() {
        return roleMapper.selectAll();
    }

    public List<String> getPermissions(Long roleId) {
        return roleFunctionMapper.selectFuncCodesByRoleId(roleId);
    }

    @Transactional
    public int assignPermissions(Long roleId, List<String> funcCodes) {
        roleFunctionMapper.deleteByRoleId(roleId);
        if (funcCodes != null && !funcCodes.isEmpty()) {
            List<RoleFunction> list = funcCodes.stream().map(code -> {
                RoleFunction rf = new RoleFunction();
                rf.setRoleId(roleId);
                rf.setFuncCode(code);
                return rf;
            }).toList();
            return roleFunctionMapper.insertBatch(list);
        }
        return 0;
    }
}
