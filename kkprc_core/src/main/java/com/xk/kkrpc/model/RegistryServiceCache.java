package com.xk.kkrpc.model;

import java.util.List;

/**
 * 注册服务节点缓存类
 */
public class RegistryServiceCache {
    private List<ServiceMateInfo> serviceMateInfoList;


    /**
     * 写注册服务节点缓存
     * @param serviceMateInfoList
     */
    public void writeServiceCache(List<ServiceMateInfo> serviceMateInfoList) {
        this.serviceMateInfoList = serviceMateInfoList;
    }

    /**
     * 读取注册服务节点
     * @return
     */
    public List<ServiceMateInfo> readServiceCache() {
        return this.serviceMateInfoList;
    }

    /**
     * 清空缓存
     */
    public void clearRegisterServiceCache() {
        this.serviceMateInfoList = null;
    }
}
