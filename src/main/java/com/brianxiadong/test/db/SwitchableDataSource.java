package com.brianxiadong.test.db;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 可动态切换的数据源包装器
 * 在测试运行时动态切换底层数据源
 */
public class SwitchableDataSource implements DataSource {

    private volatile DataSource currentDataSource;

    /**
     * 切换到新的数据源
     */
    public void switchTo(String jdbcUrl, String username, String password, String driverClassName) {
        // 关闭旧的数据源
        if (currentDataSource instanceof HikariDataSource) {
            ((HikariDataSource) currentDataSource).close();
        }

        // 创建新的数据源
        HikariDataSource newDataSource = new HikariDataSource();
        newDataSource.setJdbcUrl(jdbcUrl);
        newDataSource.setUsername(username);
        newDataSource.setPassword(password);
        newDataSource.setDriverClassName(driverClassName);
        newDataSource.setMaximumPoolSize(5);
        newDataSource.setMinimumIdle(1);

        this.currentDataSource = newDataSource;

        System.out.println("数据源已切换到: " + jdbcUrl);
    }

    private DataSource getCurrentDataSource() {
        if (currentDataSource == null) {
            throw new IllegalStateException("No data source configured. Call switchTo() first.");
        }
        return currentDataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getCurrentDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getCurrentDataSource().getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return getCurrentDataSource().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        getCurrentDataSource().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        getCurrentDataSource().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return getCurrentDataSource().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return getCurrentDataSource().getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getCurrentDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getCurrentDataSource().isWrapperFor(iface);
    }
}