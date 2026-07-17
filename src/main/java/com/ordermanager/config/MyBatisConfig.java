package com.ordermanager.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ordermanager.mapper")
public class MyBatisConfig {
}
