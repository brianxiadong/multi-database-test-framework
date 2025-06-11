package com.brianxiadong.test.db;

/**
 * 支持的数据库类型枚举
 */
public enum DatabaseType {

    MYSQL("MySQL", "com.mysql.cj.jdbc.Driver", "mysql:8.0"),
    OCEANBASE("OceanBase", "com.oceanbase.jdbc.Driver", "oceanbase/oceanbase-ce:4.3.5-lts");

    private final String displayName;
    private final String driverClassName;
    private final String defaultDockerImage;

    DatabaseType(String displayName, String driverClassName, String defaultDockerImage) {
        this.displayName = displayName;
        this.driverClassName = driverClassName;
        this.defaultDockerImage = defaultDockerImage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getDefaultDockerImage() {
        return defaultDockerImage;
    }
}