package com.vmock.base.core.response;

import com.vmock.base.utils.ContextUtils;
import com.vmock.base.utils.OutMsgUtils;
import com.vmock.base.utils.SpringUtils;
import com.vmock.biz.entity.Response;
import com.vmock.biz.entity.Url;
import com.vmock.biz.service.IResponseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vmock.base.utils.BizUtils.doOutResponse;


/**
 * 手动模式的响应处理
 *
 * @author vt
 * @since 2020-5-14
 */
public class ManualIResponseStrategy implements IResponseStrategy {

    /**
     * response service
     */
    private IResponseService responseService = SpringUtils.getBean(IResponseService.class);


    @Override
    public IMockResponse doResponse(Url mockUrl) {
        // 查询对应的返回entity
        Response mockResponse = responseService.selectMainResponse(mockUrl.getUrlId());
        // get resp and req from context
        HttpServletResponse response = ContextUtils.getResponse();
        HttpServletRequest request = ContextUtils.getRequest();
        // null check
        if (mockResponse == null) {
            OutMsgUtils.notFondResponse(response);
            return null;
        }
        //method check
        if (!request.getMethod().equalsIgnoreCase(mockResponse.getMethod())) {
            OutMsgUtils.methodNotValid(response);
            return null;
        }
        // 执行请求
        doOutResponse(mockResponse, response);
        return mockResponse;
    }
}
