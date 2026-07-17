package com.ordermanager.service;

import com.ordermanager.common.PageResult;
import com.ordermanager.entity.Department;
import com.ordermanager.mapper.DeptMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptService {

    private final DeptMapper deptMapper;

    public DeptService(DeptMapper deptMapper) {
        this.deptMapper = deptMapper;
    }

    public PageResult<Department> page(String code, String name, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Department> list = deptMapper.selectList(code, name, offset, pageSize);
        long total = deptMapper.selectCount(code, name);
        return new PageResult<>(list, total);
    }

    public Department getById(Long id) {
        return deptMapper.selectById(id);
    }

    public Department getByCode(String code) {
        return deptMapper.selectByCode(code);
    }

    public int create(Department dept) {
        dept.setCreateTime(LocalDateTime.now());
        return deptMapper.insert(dept);
    }

    public int update(Department dept) {
        return deptMapper.update(dept);
    }

    public int delete(Long id) {
        return deptMapper.deleteById(id);
    }

    public List<Department> getAll() {
        return deptMapper.selectAll();
    }
}
