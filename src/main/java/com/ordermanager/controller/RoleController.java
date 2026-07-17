package com.ordermanager.controller;

import com.ordermanager.common.PageResult;
import com.ordermanager.common.Result;
import com.ordermanager.entity.Role;
import com.ordermanager.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public Result<PageResult<Role>> page(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(roleService.page(code, name, page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        if (role == null) return Result.error("角色不存在");
        return Result.success(role);
    }

    @PostMapping
    public Result<Void> create(@RequestBody Role role) {
        roleService.create(role);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        roleService.update(role);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}/permissions")
    public Result<List<String>> getPermissions(@PathVariable Long id) {
        return Result.success(roleService.getPermissions(id));
    }

    @PutMapping("/{id}/permissions")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody Map<String, List<String>> body) {
        List<String> funcCodes = body.get("funcCodes");
        roleService.assignPermissions(id, funcCodes);
        return Result.success();
    }
}
