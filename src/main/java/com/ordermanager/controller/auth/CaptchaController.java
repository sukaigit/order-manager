package com.ordermanager.controller.auth;

import com.ordermanager.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class CaptchaController {

    @GetMapping("/captcha")
    public Result<Map<String, String>> captcha() {
        // Placeholder: In production, generate real captcha image
        Map<String, String> data = new LinkedHashMap<>();
        data.put("captchaId", UUID.randomUUID().toString());
        data.put("image", "data:image/png;base64,placeholder"); // base64 PNG
        return Result.success(data);
    }
}
