package com.brianxiadong.test.db;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库测试配置
 * 用于配置各数据库的初始化脚本和其他参数
 */
public class DatabaseTestConfig {

    private final Map<DatabaseType, String> initScripts;

    public DatabaseTestConfig() {
        this.initScripts = new HashMap<>();
    }

    /**
     * 设置数据库初始化脚本
     * 
     * @param type       数据库类型
     * @param scriptName 脚本文件名
     * @return 当前配置实例（支持链式调用）
     */
    public DatabaseTestConfig withInitScript(DatabaseType type, String scriptName) {
        this.initScripts.put(type, scriptName);
        return this;
    }

    /**
     * 获取指定数据库类型的初始化脚本
     * 
     * @param type 数据库类型
     * @return 初始化脚本文件名，如果未配置则返回null
     */
    public String getInitScript(DatabaseType type) {
        return initScripts.get(type);
    }

    /**
     * 检查是否配置了指定数据库类型的初始化脚本
     * 
     * @param type 数据库类型
     * @return 是否已配置
     */
    public boolean hasInitScript(DatabaseType type) {
        return initScripts.containsKey(type) && initScripts.get(type) != null;
    }

    /**
     * 获取所有已配置的数据库类型
     * 
     * @return 数据库类型数组
     */
    public DatabaseType[] getConfiguredTypes() {
        return initScripts.keySet().toArray(new DatabaseType[0]);
    }

    /**
     * 创建一个新的配置实例
     * 
     * @return 新的配置实例
     */
    public static DatabaseTestConfig create() {
        return new DatabaseTestConfig();
    }
}