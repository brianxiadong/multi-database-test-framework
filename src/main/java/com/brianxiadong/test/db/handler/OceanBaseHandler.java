package com.brianxiadong.test.db.handler;

import com.brianxiadong.test.db.DatabaseInfo;
import com.brianxiadong.test.db.DatabaseType;
import org.testcontainers.oceanbase.OceanBaseCEContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;

/**
 * OceanBase数据库处理器
 */
public class OceanBaseHandler extends AbstractDatabaseHandler {

    private OceanBaseCEContainer container;

    @Override
    public DatabaseInfo startContainer() throws Exception {
        System.out.println("\n========================================");
        System.out.println("开始启动 OceanBase 容器");
        System.out.println("========================================");

        // 启动 OceanBase 容器
        container = new OceanBaseCEContainer(DockerImageName.parse(DatabaseType.OCEANBASE.getDefaultDockerImage()))
                .withStartupTimeout(Duration.ofMinutes(5));

        container.start();

        // 等待 OceanBase 准备就绪
        waitForReady();

        // 创建 security 数据库
        String oceanbaseJdbcUrl = container.getJdbcUrl().replace("/test", "/security");
        DatabaseInfo dbInfo = new DatabaseInfo(
                DatabaseType.OCEANBASE,
                oceanbaseJdbcUrl,
                container.getUsername(),
                container.getPassword());

        System.out.println("OceanBase 容器启动成功");
        System.out.println("数据库URL: " + dbInfo.getJdbcUrl());

        return dbInfo;
    }

    @Override
    public void waitForReady() throws Exception {
        // 等待 OceanBase 的 test 租户变为 NORMAL 状态
        String sysUrl = String.format("jdbc:oceanbase://%s:%d/oceanbase",
                container.getHost(), container.getMappedPort(2881));
        String sysUser = "root@sys";
        String sysPassword = container.getPassword();

        boolean tenantReady = false;
        for (int i = 0; i < 120; i++) {
            try (Connection sysConnection = DriverManager.getConnection(sysUrl, sysUser, sysPassword);
                    Statement statement = sysConnection.createStatement();
                    ResultSet rs = statement.executeQuery(
                            "select status from oceanbase.DBA_OB_TENANTS where tenant_name = 'test'")) {

                if (rs.next()) {
                    String status = rs.getString(1);
                    if ("NORMAL".equalsIgnoreCase(status)) {
                        tenantReady = true;
                        break;
                    }
                }

            } catch (Exception e) {
                // 忽略异常，继续重试
            }

            Thread.sleep(5000); // 等待5秒后重试
        }

        if (!tenantReady) {
            throw new RuntimeException("OceanBase tenant 'test' failed to become ready");
        }

        // 创建 security 数据库
        try (Connection connection = DriverManager.getConnection(
                container.getJdbcUrl(), container.getUsername(), container.getPassword());
                Statement statement = connection.createStatement()) {

            statement.execute("CREATE DATABASE IF NOT EXISTS security");
            System.out.println("OceanBase security 数据库创建成功");
        }
    }

    @Override
    public void stopContainer() {
        if (container != null && container.isRunning()) {
            container.stop();
            System.out.println("OceanBase 容器已关闭");
        }
    }

    @Override
    public boolean isRunning() {
        return container != null && container.isRunning();
    }
}