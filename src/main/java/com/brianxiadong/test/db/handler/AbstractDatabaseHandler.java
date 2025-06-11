package com.brianxiadong.test.db.handler;

import com.brianxiadong.test.db.DatabaseInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * 数据库处理器抽象基类
 * 提供通用的脚本执行逻辑
 */
public abstract class AbstractDatabaseHandler implements DatabaseHandler {

    @Override
    public void executeInitScript(DatabaseInfo dbInfo, String scriptName) throws Exception {
        if (scriptName == null || scriptName.trim().isEmpty()) {
            return;
        }

        System.out.println("执行初始化脚本: " + scriptName);

        // 读取脚本内容
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(scriptName);
        if (inputStream == null) {
            throw new RuntimeException("找不到初始化脚本: " + scriptName);
        }

        StringBuilder sqlContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sqlContent.append(line).append("\n");
            }
        }

        // 清理SQL内容（移除注释等）
        String cleanSql = cleanSqlContent(sqlContent.toString());
        if (cleanSql.trim().isEmpty()) {
            System.out.println("脚本内容为空，跳过执行");
            return;
        }

        // 执行SQL
        try (Connection connection = DriverManager.getConnection(
                dbInfo.getJdbcUrl(), dbInfo.getUsername(), dbInfo.getPassword());
                Statement statement = connection.createStatement()) {

            // 分割SQL语句并执行
            String[] statements = cleanSql.split(";");
            for (String sql : statements) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    System.out.println("执行SQL: " + sql);
                    statement.execute(sql);
                }
            }

            System.out.println("初始化脚本执行完成 ✓");

        } catch (Exception e) {
            System.err.println("执行初始化脚本失败: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 清理SQL内容，移除注释和空行
     */
    private String cleanSqlContent(String sqlContent) {
        StringBuilder cleaned = new StringBuilder();
        String[] lines = sqlContent.split("\n");

        for (String line : lines) {
            line = line.trim();
            // 跳过注释行和空行
            if (!line.startsWith("--") && !line.isEmpty()) {
                cleaned.append(line).append("\n");
            }
        }

        return cleaned.toString();
    }
}