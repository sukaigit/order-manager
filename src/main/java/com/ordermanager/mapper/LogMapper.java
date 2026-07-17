package com.ordermanager.mapper;

import com.ordermanager.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogMapper {
    List<OperationLog> selectList(@Param("user") String user, @Param("action") String action,
                                  @Param("target") String target,
                                  @Param("offset") int offset, @Param("limit") int limit);

    long selectCount(@Param("user") String user, @Param("action") String action,
                     @Param("target") String target);

    OperationLog selectById(@Param("id") Long id);

    int insert(OperationLog log);
}
