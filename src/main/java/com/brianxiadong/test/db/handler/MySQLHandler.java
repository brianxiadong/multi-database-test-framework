package com.brianxiadong.test.db.handler;

import com.brianxiadong.test.db.DatabaseInfo;
import com.brianxiadong.test.db.DatabaseType;
import org.testcontainers.containers.MySQLContainer;

/**
 * MySQL数据库处理器
 */
public class MySQLHandler extends AbstractDatabaseHandler {

    private MySQLContainer<?> container;

    @Override
    public DatabaseInfo startContainer() throws Exception {
        System.out.println("\n========================================");
        System.out.println("开始启动 MySQL 容器");
        System.out.println("========================================");

        // 启动 MySQL 容器
        container = new MySQLContainer<>(DatabaseType.MYSQL.getDefaultDockerImage())
                .withDatabaseName("security")
                .withUsername("root")
                .withPassword("password");

        container.start();

        DatabaseInfo dbInfo = new DatabaseInfo(
                DatabaseType.MYSQL,
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword());

        System.out.println("MySQL 容器启动成功");
        System.out.println("数据库URL: " + dbInfo.getJdbcUrl());

        return dbInfo;
    }

    @Override
    public void stopContainer() {
        if (container != null && container.isRunning()) {
            container.stop();
            System.out.println("MySQL 容器已关闭");
        }
    }

    @Override
    public boolean isRunning() {
        return container != null && container.isRunning();
    }
}