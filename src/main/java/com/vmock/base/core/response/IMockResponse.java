package com.vmock.base.core.response;

/**
 * 响应基本条件接口
 *
 * @author vt
 * @since 2020/5/15
 */
public interface IMockResponse {

    /**
     * 响应httpcode
     */
    Integer getStatusCode();

    /**
     * 获取自定义响应头json
     */
    String getCustomHeader();

    /**
     * 获取响应内容
     */
    String getContent();
}
