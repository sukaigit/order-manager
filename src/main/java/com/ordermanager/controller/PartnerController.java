package com.ordermanager.controller;

import com.ordermanager.common.PageResult;
import com.ordermanager.common.Result;
import com.ordermanager.entity.Partner;
import com.ordermanager.service.PartnerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    public Result<PageResult<Partner>> page(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String contact,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(partnerService.page(code, name, contact, page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Partner> getById(@PathVariable Long id) {
        Partner partner = partnerService.getById(id);
        if (partner == null) return Result.error("合作方不存在");
        return Result.success(partner);
    }

    @PostMapping
    public Result<Void> create(@RequestBody Partner partner) {
        partnerService.create(partner);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Partner partner) {
        partner.setId(id);
        partnerService.update(partner);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        int result = partnerService.delete(id);
        if (result == -1) {
            return Result.badRequest("该合作方存在关联订单，无法删除");
        }
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Partner>> list() {
        return Result.success(partnerService.getAll());
    }
}
