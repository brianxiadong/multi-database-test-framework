package com.brianxiadong.test.db;

import com.brianxiadong.test.db.handler.DatabaseHandler;
import com.brianxiadong.test.db.handler.DatabaseHandlerFactory;

/**
 * 多数据库测试执行器
 * 使用策略模式和工厂模式，支持灵活的数据库扩展
 */
public class MultiDbTestRunner {

    /**
     * 测试逻辑接口，提供数据库信息
     */
    @FunctionalInterface
    public interface DatabaseTestLogic {
        void test(DatabaseInfo dbInfo) throws Exception;
    }

    /**
     * 执行多数据库测试（兼容旧接口）
     * 
     * @param mysqlInitScript     MySQL初始化脚本
     * @param oceanBaseInitScript OceanBase初始化脚本
     * @param testLogic           测试逻辑
     */
    public static void runMultiDbTest(String mysqlInitScript, String oceanBaseInitScript,
            DatabaseTestLogic testLogic) {
        DatabaseTestConfig config = DatabaseTestConfig.create()
                .withInitScript(DatabaseType.MYSQL, mysqlInitScript)
                .withInitScript(DatabaseType.OCEANBASE, oceanBaseInitScript);

        runMultiDbTest(config, testLogic);
    }

    /**
     * 执行多数据库测试（新接口）
     * 
     * @param config    数据库测试配置
     * @param testLogic 测试逻辑
     */
    public static void runMultiDbTest(DatabaseTestConfig config, DatabaseTestLogic testLogic) {
        SwitchableDataSource switchableDataSource = MultiDbHelper.getSwitchableDataSource();

        System.out.println("\n========================================");
        System.out.println("开始多数据库测试");
        System.out.println("========================================");

        // 遍历所有配置的数据库类型
        for (DatabaseType dbType : config.getConfiguredTypes()) {
            testWithDatabase(dbType, config.getInitScript(dbType), testLogic, switchableDataSource);
        }

        System.out.println("\n========================================");
        System.out.println("所有数据库测试完成！");
        System.out.println("========================================");
    }

    /**
     * 执行指定数据库类型的测试
     * 
     * @param dbType    数据库类型
     * @param testLogic 测试逻辑
     */
    public static void runSingleDbTest(DatabaseType dbType, String initScript, DatabaseTestLogic testLogic) {
        SwitchableDataSource switchableDataSource = MultiDbHelper.getSwitchableDataSource();
        testWithDatabase(dbType, initScript, testLogic, switchableDataSource);
    }

    /**
     * 使用指定数据库执行测试
     * 
     * @param dbType               数据库类型
     * @param initScript           初始化脚本
     * @param testLogic            测试逻辑
     * @param switchableDataSource 可切换数据源
     */
    private static void testWithDatabase(DatabaseType dbType, String initScript,
            DatabaseTestLogic testLogic,
            SwitchableDataSource switchableDataSource) {

        DatabaseHandler handler = DatabaseHandlerFactory.createHandler(dbType);

        try {
            // 启动数据库容器
            DatabaseInfo dbInfo = handler.startContainer();

            // 执行初始化脚本
            if (initScript != null && !initScript.trim().isEmpty()) {
                handler.executeInitScript(dbInfo, initScript);
            }

            // 切换Spring数据源
            switchableDataSource.switchTo(
                    dbInfo.getJdbcUrl(),
                    dbInfo.getUsername(),
                    dbInfo.getPassword(),
                    dbInfo.getDriverClassName());

            // 执行测试逻辑
            testLogic.test(dbInfo);

            System.out.println("数据库 " + dbInfo.getName() + " 测试完成 ✓");

        } catch (Exception e) {
            System.err.println("数据库 " + dbType.getDisplayName() + " 测试失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(dbType.getDisplayName() + " test failed", e);
        } finally {
            // 关闭数据库容器
            handler.stopContainer();
        }
    }
}