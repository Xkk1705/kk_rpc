package com.xk.kkrpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;


/**
 * 全局配置工具类
 */
public class ConfigUtils {
    /**
     * @param tClass 映射的配置类
     * @param prefix 前缀
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T loadRpcConfig(Class<T> tClass, String prefix) throws Exception {
        return loadRpcConfig(tClass, prefix, "");
    }

    /**
     * @param tClass      映射的配置类
     * @param prefix      前缀
     * @param environment 配置文件环境
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T loadRpcConfig(Class<T> tClass, String prefix, String environment) throws Exception {
        try {
            StringBuilder configFileBuilder = new StringBuilder("application");
            if (StrUtil.isNotBlank(environment)) {
                configFileBuilder.append("-").append(environment);
            }
            configFileBuilder.append(".properties");
            Props props = new Props(configFileBuilder.toString());
            return props.toBean(tClass, prefix);
        } catch (Exception e) {

            throw new Exception("读取配置文件失败");
        }
    }
}
