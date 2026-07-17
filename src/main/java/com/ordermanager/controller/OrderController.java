package com.ordermanager.controller;

import com.ordermanager.common.PageResult;
import com.ordermanager.common.Result;
import com.ordermanager.entity.Order;
import com.ordermanager.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Result<PageResult<Order>> page(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long partnerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(orderService.page(code, type, partnerId, status, page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        Order order = orderService.getById(id);
        if (order == null) return Result.error("订单不存在");
        return Result.success(order);
    }

    @PostMapping
    public Result<Void> create(@RequestBody Order order) {
        orderService.create(order);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id);
        orderService.update(order);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return Result.success();
    }
}
