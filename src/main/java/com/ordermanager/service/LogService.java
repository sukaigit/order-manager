package com.ordermanager.service;

import com.ordermanager.common.PageResult;
import com.ordermanager.entity.OperationLog;
import com.ordermanager.mapper.LogMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final LogMapper logMapper;

    public LogService(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    public PageResult<OperationLog> page(String user, String action, String target,
                                          int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<OperationLog> list = logMapper.selectList(user, action, target, offset, pageSize);
        long total = logMapper.selectCount(user, action, target);
        return new PageResult<>(list, total);
    }

    public int create(OperationLog log) {
        return logMapper.insert(log);
    }
}
