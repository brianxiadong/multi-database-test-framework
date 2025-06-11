package com.brianxiadong.test.db;

/**
 * 数据库连接信息
 */
public class DatabaseInfo {

    private final DatabaseType type;
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final String driverClassName;

    public DatabaseInfo(DatabaseType type, String jdbcUrl, String username, String password) {
        this.type = type;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.driverClassName = type.getDriverClassName();
    }

    public DatabaseInfo(DatabaseType type, String jdbcUrl, String username, String password, String driverClassName) {
        this.type = type;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.driverClassName = driverClassName;
    }

    public DatabaseType getType() {
        return type;
    }

    public String getName() {
        return type.getDisplayName();
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    @Override
    public String toString() {
        return String.format("DatabaseInfo{type=%s, url=%s, username=%s}",
                type.getDisplayName(), jdbcUrl, username);
    }
}