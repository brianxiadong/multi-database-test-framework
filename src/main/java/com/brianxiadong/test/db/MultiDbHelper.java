package com.brianxiadong.test.db;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.DynamicPropertyRegistry;

import javax.sql.DataSource;

/**
 * 多数据库测试帮助类
 * 提供可动态切换的数据源
 */
public class MultiDbHelper {

    private static SwitchableDataSource switchableDataSource;

    /**
     * 配置数据源属性
     * 在测试类的 @DynamicPropertySource 方法中调用此方法
     */
    public static void configureProperties(DynamicPropertyRegistry registry) {
        // 禁用Spring Boot的自动数据源配置，我们手动提供
        registry.add("spring.datasource.url", () -> "");
        registry.add("spring.datasource.username", () -> "");
        registry.add("spring.datasource.password", () -> "");
        registry.add("spring.datasource.driver-class-name", () -> "");
    }

    /**
     * 获取可切换的数据源实例
     */
    public static SwitchableDataSource getSwitchableDataSource() {
        if (switchableDataSource == null) {
            switchableDataSource = new SwitchableDataSource();
        }
        return switchableDataSource;
    }

    /**
     * 测试配置类，提供可切换的数据源Bean
     */
    @TestConfiguration
    public static class TestDataSourceConfiguration {

        @Bean
        @Primary
        public DataSource dataSource() {
            return getSwitchableDataSource();
        }
    }
}