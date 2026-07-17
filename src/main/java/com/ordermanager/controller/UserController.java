package com.ordermanager.controller;

import com.ordermanager.common.PageResult;
import com.ordermanager.common.Result;
import com.ordermanager.entity.User;
import com.ordermanager.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Result<PageResult<User>> page(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String org,
            @RequestParam(required = false) Integer active,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(userService.page(code, name, role, dept, org, active, page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) return Result.error("用户不存在");
        return Result.success(user);
    }

    @PostMapping
    public Result<Void> create(@RequestBody User user) {
        userService.create(user);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}/lock")
    public Result<Void> toggleLock(@PathVariable Long id, @RequestParam boolean locked) {
        userService.toggleLock(id, locked);
        return Result.success();
    }

    @PutMapping("/{id}/reset-pwd")
    public Result<Void> resetPassword(@PathVariable Long id) {
        // In production: BCrypt.encode("Uu888888!")
        userService.resetPassword(id, "Uu888888!");
        return Result.success();
    }

    @PutMapping("/{id}/change-pwd")
    public Result<Void> changePassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String oldPwd = body.get("oldPwd");
        String newPwd = body.get("newPwd");
        if (oldPwd == null || newPwd == null) {
            return Result.badRequest("旧密码和新密码不能为空");
        }
        User user = userService.getById(id);
        if (user == null) return Result.error("用户不存在");
        // TODO: Verify old password with BCrypt
        userService.changePassword(id, newPwd);
        return Result.success();
    }
}
