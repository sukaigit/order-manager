package com.ordermanager.controller;

import com.ordermanager.common.Result;
import com.ordermanager.entity.Menu;
import com.ordermanager.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/tree")
    public Result<List<Menu>> tree(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String label,
            @RequestParam(required = false) String route,
            @RequestParam(required = false) String type) {
        return Result.success(menuService.tree(code, label, route, type));
    }

    @GetMapping("/{id}")
    public Result<Menu> getById(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        if (menu == null) return Result.error("菜单不存在");
        return Result.success(menu);
    }

    @PostMapping
    public Result<Void> create(@RequestBody Menu menu) {
        menuService.create(menu);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Menu menu) {
        menu.setId(id);
        menuService.update(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        int result = menuService.delete(id);
        if (result == -1) {
            return Result.badRequest("存在下级菜单，无法删除");
        }
        return Result.success();
    }
}
