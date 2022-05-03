package com.vmock.base.core.response;

import com.vmock.biz.entity.Url;

/**
 * 响应策略
 *
 * @author vt
 * @since
 */
public interface IResponseStrategy {

    /**
     * 处理响应，返回IResponse，方便记录日志等操作
     *
     * @return IResponse响应条件接口
     */
    IMockResponse doResponse(Url mockUrl);
}
