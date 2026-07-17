package com.ordermanager.controller;

import com.ordermanager.common.Result;
import com.ordermanager.entity.Organization;
import com.ordermanager.service.OrgService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrgController {

    private final OrgService orgService;

    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @GetMapping("/tree")
    public Result<List<Organization>> tree(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String label,
            @RequestParam(required = false) String sname,
            @RequestParam(required = false) String level) {
        return Result.success(orgService.tree(code, label, sname, level));
    }

    @GetMapping("/{id}")
    public Result<Organization> getById(@PathVariable Long id) {
        Organization org = orgService.getById(id);
        if (org == null) return Result.error("机构不存在");
        return Result.success(org);
    }

    @PostMapping
    public Result<Void> create(@RequestBody Organization org) {
        orgService.create(org);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Organization org) {
        org.setId(id);
        orgService.update(org);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        int result = orgService.delete(id);
        if (result == -1) {
            return Result.badRequest("存在下级机构，无法删除");
        }
        return Result.success();
    }
}
