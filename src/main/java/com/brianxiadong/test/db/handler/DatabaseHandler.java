package com.brianxiadong.test.db.handler;

import com.brianxiadong.test.db.DatabaseInfo;

/**
 * 数据库处理器接口
 * 定义数据库容器启动、初始化、清理等操作的统一接口
 */
public interface DatabaseHandler {

    /**
     * 启动数据库容器并获取连接信息
     * 
     * @return 数据库连接信息
     * @throws Exception 启动失败时抛出异常
     */
    DatabaseInfo startContainer() throws Exception;

    /**
     * 执行初始化脚本
     * 
     * @param dbInfo     数据库连接信息
     * @param scriptName 初始化脚本名称
     * @throws Exception 执行失败时抛出异常
     */
    void executeInitScript(DatabaseInfo dbInfo, String scriptName) throws Exception;

    /**
     * 停止并清理数据库容器
     */
    void stopContainer();

    /**
     * 检查容器是否正在运行
     * 
     * @return 容器运行状态
     */
    boolean isRunning();

    /**
     * 等待数据库就绪
     * 某些数据库（如OceanBase）需要额外的就绪等待时间
     * 
     * @throws Exception 等待失败时抛出异常
     */
    default void waitForReady() throws Exception {
        // 默认实现为空，子类可以根据需要重写
    }
}