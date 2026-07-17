package com.ordermanager.controller;

import com.ordermanager.common.PageResult;
import com.ordermanager.common.Result;
import com.ordermanager.entity.Partner;
import com.ordermanager.service.PartnerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @RequestParam(required = false) String code,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) String contact) throws IOException {
        List<Partner> list = partnerService.exportList(code, name, contact);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=partners.xls");
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            .append("<?mso-application progid=\"Excel.Sheet\"?>")
            .append("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"")
            .append(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">")
            .append("<Worksheet ss:Name=\"合作方\"><Table>");
        xml.append("<Row>");
        for (String h : new String[]{"合作方编号","合作方名称","联系人","联系电话","备注"}) {
            xml.append("<Cell><Data ss:Type=\"String\">").append(escXml(h)).append("</Data></Cell>");
        }
        xml.append("</Row>");
        for (Partner p : list) {
            xml.append("<Row>");
            xml.append(c(p.getCode())).append(c(p.getName())).append(c(p.getContact()));
            xml.append(c(p.getPhone())).append(c(p.getRemark()));
            xml.append("</Row>");
        }
        xml.append("</Table></Worksheet></Workbook>");
        response.getWriter().write(xml.toString());
    }

    private String c(String v) {
        return "<Cell><Data ss:Type=\"String\">" + escXml(v) + "</Data></Cell>";
    }

    private String escXml(String v) {
        if (v == null) return "";
        return v.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
