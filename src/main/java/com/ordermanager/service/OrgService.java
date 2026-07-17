package com.ordermanager.service;

import com.ordermanager.entity.Organization;
import com.ordermanager.mapper.OrgMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrgService {

    private final OrgMapper orgMapper;

    public OrgService(OrgMapper orgMapper) {
        this.orgMapper = orgMapper;
    }

    public List<Organization> tree(String code, String label, String sname, String level) {
        return orgMapper.selectList(code, label, sname, level);
    }

    public Organization getById(Long id) {
        return orgMapper.selectById(id);
    }

    public Organization getByCode(String code) {
        return orgMapper.selectByCode(code);
    }

    public int create(Organization org) {
        org.setCreateTime(LocalDateTime.now());
        return orgMapper.insert(org);
    }

    public int update(Organization org) {
        return orgMapper.update(org);
    }

    public int delete(Long id) {
        Organization org = orgMapper.selectById(id);
        if (org == null) return 0;
        List<Organization> children = orgMapper.selectChildren(org.getCode());
        if (!children.isEmpty()) {
            return -1; // has children, cannot delete
        }
        return orgMapper.deleteById(id);
    }

    public List<Organization> getAll() {
        return orgMapper.selectAll();
    }
}
