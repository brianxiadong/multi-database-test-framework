package com.brianxiadong.test.db.handler;

import com.brianxiadong.test.db.DatabaseType;

/**
 * 数据库处理器工厂
 * 根据数据库类型创建对应的处理器实例
 */
public class DatabaseHandlerFactory {

    /**
     * 创建数据库处理器
     * 
     * @param type 数据库类型
     * @return 对应的数据库处理器
     */
    public static DatabaseHandler createHandler(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return new MySQLHandler();
            case OCEANBASE:
                return new OceanBaseHandler();
            default:
                throw new UnsupportedOperationException("不支持的数据库类型: " + type);
        }
    }

    /**
     * 获取所有支持的数据库类型
     * 
     * @return 数据库类型数组
     */
    public static DatabaseType[] getSupportedTypes() {
        return DatabaseType.values();
    }
}