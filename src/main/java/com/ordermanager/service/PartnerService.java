package com.ordermanager.service;

import com.ordermanager.common.PageResult;
import com.ordermanager.entity.Partner;
import com.ordermanager.mapper.PartnerMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartnerService {

    private final PartnerMapper partnerMapper;

    public PartnerService(PartnerMapper partnerMapper) {
        this.partnerMapper = partnerMapper;
    }

    public PageResult<Partner> page(String code, String name, String contact, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Partner> list = partnerMapper.selectList(code, name, contact, offset, pageSize);
        long total = partnerMapper.selectCount(code, name, contact);
        return new PageResult<>(list, total);
    }

    public Partner getById(Long id) {
        return partnerMapper.selectById(id);
    }

    public int create(Partner partner) {
        // Auto-generate partner code
        Long maxId = partnerMapper.selectMaxId();
        partner.setCode(String.format("PARTNER-%03d", (maxId == null ? 1 : maxId + 1)));
        partner.setCreateTime(LocalDateTime.now());
        partner.setUpdateTime(LocalDateTime.now());
        return partnerMapper.insert(partner);
    }

    public int update(Partner partner) {
        return partnerMapper.update(partner);
    }

    public int delete(Long id) {
        long orderCount = partnerMapper.selectOrderCount(id);
        if (orderCount > 0) {
            return -1; // has associated orders
        }
        return partnerMapper.deleteById(id);
    }

    public List<Partner> getAll() {
        return partnerMapper.selectAll();
    }
}
