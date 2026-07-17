package com.ordermanager.service;

import com.ordermanager.common.PageResult;
import com.ordermanager.entity.Function;
import com.ordermanager.mapper.FuncMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FuncService {

    private final FuncMapper funcMapper;

    public FuncService(FuncMapper funcMapper) {
        this.funcMapper = funcMapper;
    }

    public PageResult<Function> page(String code, String name, String menu, String perm,
                                      int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Function> list = funcMapper.selectList(code, name, menu, perm, offset, pageSize);
        long total = funcMapper.selectCount(code, name, menu, perm);
        return new PageResult<>(list, total);
    }

    public Function getById(Long id) {
        return funcMapper.selectById(id);
    }

    public int create(Function func) {
        func.setCreateTime(LocalDateTime.now());
        return funcMapper.insert(func);
    }

    public int update(Function func) {
        return funcMapper.update(func);
    }

    public int delete(Long id) {
        return funcMapper.deleteById(id);
    }

    public List<Function> getAll() {
        return funcMapper.selectAll();
    }
}
