package com.vmock.base.core.response;

import com.vmock.base.utils.ContextUtils;
import com.vmock.base.utils.OutMsgUtils;
import com.vmock.base.utils.SpringUtils;
import com.vmock.biz.entity.ResponseRestful;
import com.vmock.biz.entity.Url;
import com.vmock.biz.service.IResponseRestfulService;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vmock.base.utils.BizUtils.doOutResponse;

/**
 * Restful模式的响应处理
 *
 * @author vt
 * @since 2020-5-14
 */
public class RestfulResponseStrategy implements IResponseStrategy {

    /**
     * response restful service
     */
    private IResponseRestfulService responseService = SpringUtils.getBean(IResponseRestfulService.class);

    @Override
    public IMockResponse doResponse(Url mockUrl) {
        // get req and resp form context
        HttpServletRequest request = ContextUtils.getRequest();
        HttpServletResponse response = ContextUtils.getResponse();
        // get http method, and convert to int code by {@link org.springframework.http.HttpMethod}
        String method = request.getMethod().toUpperCase();
        int httpMethodCode = HttpMethod.resolve(method).ordinal();
        // 根据http方法和urlId锁定一条返回
        ResponseRestful mockResponse = responseService.getOneByCodeAndUrlId(httpMethodCode, mockUrl.getUrlId());
        // null check
        if (mockResponse == null) {
            OutMsgUtils.notFondResponseRestful(response);
            return null;
        }
        // 执行请求
        doOutResponse(mockResponse, response);
        return mockResponse;
    }
}
