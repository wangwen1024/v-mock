package com.vmock.base.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.vmock.base.core.response.IMockResponse;
import com.vmock.base.core.response.IResponseStrategy;
import com.vmock.base.core.response.StrategyExtractor;
import com.vmock.base.core.url.BaseMockUrlHandler;
import com.vmock.base.core.url.HandlerExtractor;
import com.vmock.base.utils.BizUtils;
import com.vmock.biz.entity.Log;
import com.vmock.biz.entity.Url;
import com.vmock.biz.enums.ResponseTypeEnum;
import com.vmock.biz.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.EnumMap;

/**
 * 主要业务过滤器处理类
 *
 * @author vt
 * @since 2019年12月4日
 */
@DependsOn("springUtils")
@Component
public class MockFilterHandler {

    /**
     * system log service
     */
    @Autowired
    private ILogService logService;

    /**
     * 获取mockUrl实体的处理
     */
    private BaseMockUrlHandler mockUrlHandler = HandlerExtractor.getHandler();

    /**
     * 获取响应处理的策略类
     */
    private EnumMap<ResponseTypeEnum, IResponseStrategy> responseStrategies = StrategyExtractor
            .getResponseStrategies();


    /**
     * 执行处理
     *
     * @param request
     * @param response
     */
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        // set utf8
        response.setCharacterEncoding("utf-8");
        // 获取对应url实体
        String requestUrl = BizUtils.getProcessedUrl(request);
        Url mockUrlEntity = mockUrlHandler.getMockUrlEntity(requestUrl);
        //null check
        if (mockUrlEntity == null) {
            return;
        }
        // 获取响应类型
        ResponseTypeEnum responseType = ResponseTypeEnum.getByCode(mockUrlEntity.getResponseType());
        // 请求详细
        String requestDetail = BizUtils.requestToJson(request);
        // 获取对应策略 并执行
        IResponseStrategy responseStrategy = responseStrategies.get(responseType);
        IMockResponse mockResponse = responseStrategy.doResponse(mockUrlEntity);
        // 如果成功命中 则异步记录日志
        if (mockResponse != null) {
            logResponse(mockUrlEntity.getUrl(), requestDetail, mockResponse, request);
        }
    }

    /**
     * 记录response日志
     *
     * @param requestDetail 请求详细
     * @param hitUrl        命中url
     * @param mockResponse  mock的返回实体
     * @param request       httpServletRequest
     */
    private void logResponse(String hitUrl, String requestDetail, IMockResponse mockResponse, HttpServletRequest request) {
        Log mockLog = new Log();
        // 命中url，系统中配置的，可能是带path占位符的
        mockLog.setHitUrl(hitUrl);
        // 实际url
        mockLog.setRequestUrl(BizUtils.getProcessedUrl(request));
        // ip
        mockLog.setRequestIp(ServletUtil.getClientIP(request));
        // request method
        mockLog.setRequestMethod(request.getMethod());
        // request detail
        mockLog.setRequestDetail(requestDetail);
        // 日志处理，只有成功命中的场合才记录详细，防止产生过多垃圾数据。
        JSONObject responseDetailJson = new JSONObject();
        // 响应http码
        responseDetailJson.put("respStatus", mockResponse.getStatusCode());
        // header
        String customHeader = mockResponse.getCustomHeader();
        if (StrUtil.isNotBlank(customHeader)) {
            responseDetailJson.put("respHeader", new JSONArray(customHeader));
        }
        // 相应内容
        responseDetailJson.put("respContent", mockResponse.getContent());
        // response detail
        mockLog.setResponseDetail(responseDetailJson.toString());
        // 异步插入
        logService.asyncInsert(mockLog);
    }
}
