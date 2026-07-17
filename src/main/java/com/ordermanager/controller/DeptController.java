package com.ordermanager.controller;

import com.ordermanager.common.PageResult;
import com.ordermanager.common.Result;
import com.ordermanager.entity.Department;
import com.ordermanager.service.DeptService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DeptController {

    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @GetMapping
    public Result<PageResult<Department>> page(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(deptService.page(code, name, page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Department> getById(@PathVariable Long id) {
        Department dept = deptService.getById(id);
        if (dept == null) return Result.error("部门不存在");
        return Result.success(dept);
    }

    @PostMapping
    public Result<Void> create(@RequestBody Department dept) {
        deptService.create(dept);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Department dept) {
        dept.setId(id);
        deptService.update(dept);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        deptService.delete(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Department>> list() {
        return Result.success(deptService.getAll());
    }
}
