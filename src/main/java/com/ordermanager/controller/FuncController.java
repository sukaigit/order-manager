package com.ordermanager.controller;

import com.ordermanager.common.PageResult;
import com.ordermanager.common.Result;
import com.ordermanager.entity.Function;
import com.ordermanager.service.FuncService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/functions")
public class FuncController {

    private final FuncService funcService;

    public FuncController(FuncService funcService) {
        this.funcService = funcService;
    }

    @GetMapping
    public Result<PageResult<Function>> page(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String menu,
            @RequestParam(required = false) String perm,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(funcService.page(code, name, menu, perm, page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Function> getById(@PathVariable Long id) {
        Function func = funcService.getById(id);
        if (func == null) return Result.error("功能不存在");
        return Result.success(func);
    }

    @PostMapping
    public Result<Void> create(@RequestBody Function func) {
        funcService.create(func);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Function func) {
        func.setId(id);
        funcService.update(func);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        funcService.delete(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Function>> list() {
        return Result.success(funcService.getAll());
    }
}
