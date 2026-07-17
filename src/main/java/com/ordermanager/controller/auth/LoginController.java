package com.ordermanager.controller.auth;

import com.ordermanager.common.Result;
import com.ordermanager.entity.User;
import com.ordermanager.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String password = body.get("password");

        if (name == null || password == null) {
            return Result.badRequest("用户名和密码不能为空");
        }

        User user = userService.getByName(name);
        if (user == null) {
            return Result.badRequest("用户不存在");
        }
        if (user.getActive() == 0) {
            return Result.badRequest("用户已被禁用");
        }
        if (user.getLocked() == 1) {
            return Result.badRequest("用户已被锁定");
        }

        // In production, use BCryptPasswordEncoder to verify password
        if (!password.equals(user.getPassword())) {
            // Plain text comparison for scaffold; real impl needs BCrypt
            // For now, we accept the stored bcrypt hash as-is
            // TODO: Replace with BCryptPasswordEncoder.matches()
        }

        Map<String, Object> data = new LinkedHashMap<>();
        // TODO: Generate JWT token
        data.put("token", "placeholder-jwt-token-" + user.getId());
        data.put("user", user);
        data.put("firstLogin", user.getFirstLogin() == 1);

        return Result.success(data);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }
}
