package com.xk.kkrpc.model;

import cn.hutool.core.util.StrUtil;
import com.xk.kkrpc.constant.RegisterConstant;
import lombok.Data;

/**
 * 服务信息类
 */
@Data
public class ServiceMateInfo {
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;

    /**
     * 服务版本
     */
    private String serviceVersion = RegisterConstant.SERVICE_VERSION;
    /**
     * 服务组
     */
    private String serviceGroup;

    /**
     * 获取服务键名
     *
     * @return
     */
    public String getServiceKey() {
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * 获取服务注册节点键名
     *
     * @return
     */
    public String getServiceNodeKey() {
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, servicePort);
    }


    /**
     * 获取完整服务地址
     *
     * @return
     */
    public String getServiceAddress() {
        if (!StrUtil.contains(serviceHost, "http")) {
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }
}
