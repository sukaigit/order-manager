package com.ordermanager.controller;

import com.ordermanager.common.PageResult;
import com.ordermanager.common.Result;
import com.ordermanager.entity.Order;
import com.ordermanager.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
        Order existing = orderService.getById(id);
        if (existing == null) return Result.badRequest("订单不存在");
        if (!"待审核".equals(existing.getStatus())) return Result.badRequest("仅待审核订单可修改");
        order.setId(id);
        orderService.update(order);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return Result.success();
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @RequestParam(required = false) String code,
                       @RequestParam(required = false) String type,
                       @RequestParam(required = false) String partner,
                       @RequestParam(required = false) String status) throws IOException {
        List<Order> list = orderService.exportList(code, type, partner, status);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=orders.xls");
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            .append("<?mso-application progid=\"Excel.Sheet\"?>")
            .append("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"")
            .append(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">")
            .append("<Worksheet ss:Name=\"订单\"><Table>");
        xml.append("<Row>");
        String[] headers = {"订单编号","订单名称","订单类型","合作方","金额","状态","订单时间","备注"};
        for (String h : headers) {
            xml.append("<Cell><Data ss:Type=\"String\">").append(escapeXml(h)).append("</Data></Cell>");
        }
        xml.append("</Row>");
        for (Order o : list) {
            xml.append("<Row>");
            xml.append(cell(o.getCode())).append(cell(o.getName())).append(cell(o.getType()));
            xml.append(cell(o.getPartnerName() != null ? o.getPartnerName() : ""));
            xml.append(cell(o.getAmount() != null ? o.getAmount().toString() : "0"));
            xml.append(cell(o.getStatus()));
            xml.append(cell(o.getOrderTime() != null ? o.getOrderTime().toString().replace("T"," ") : ""));
            xml.append(cell(o.getRemark() != null ? o.getRemark() : ""));
            xml.append("</Row>");
        }
        xml.append("</Table></Worksheet></Workbook>");
        response.getWriter().write(xml.toString());
    }

    private String cell(String v) {
        return "<Cell><Data ss:Type=\"String\">" + escapeXml(v) + "</Data></Cell>";
    }

    private String escapeXml(String v) {
        if (v == null) return "";
        return v.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
