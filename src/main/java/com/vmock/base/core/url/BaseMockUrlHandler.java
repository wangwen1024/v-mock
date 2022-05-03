package com.vmock.base.core.url;

import com.vmock.biz.entity.Url;

/**
 * 获取mock url实体类责任链
 *
 * @author vt
 * @since
 */
public abstract class BaseMockUrlHandler {


    /**
     * 下一个处理器
     */
    protected BaseMockUrlHandler nextHandler;

    /**
     * 获取MockUrl实体
     *
     * @param requestUrl 请求路径
     * @return 命中的实体类
     */
    public Url getMockUrlEntity(String requestUrl) {
        Url mockUrlEntity = this.find(requestUrl);
        if (mockUrlEntity != null) {
            return mockUrlEntity;
        }
        if (nextHandler != null) {
            return nextHandler.getMockUrlEntity(requestUrl);
        }
        return null;
    }

    /**
     * 填入下一个handle
     */
    protected void setNextLogger(BaseMockUrlHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 查找 or 处理 具体逻辑
     *
     * @param requestUrl 请求路径
     * @return
     */
    abstract protected Url find(String requestUrl);

}
