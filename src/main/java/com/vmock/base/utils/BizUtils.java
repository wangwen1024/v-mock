package com.vmock.base.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.vmock.base.constant.CommonConst;
import com.vmock.base.core.response.IMockResponse;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Map;
import java.util.stream.Collectors;

import static com.vmock.base.utils.OutMsgUtils.outMsg;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 通用业务工具
 * 普通工具类请优先使用{@link cn.hutool},{@link org.apache.commons.lang3}等
 *
 * @author vt
 * @since 2020/5/14
 */
@UtilityClass
public class BizUtils {

    /**
     * 处理url
     *
     * @param request url
     * @return 处理过的URL
     */
    public String getProcessedUrl(HttpServletRequest request) {
        // get request url path
        String requestUrl = request.getRequestURI();
        //去除前缀(contextPatn)
        requestUrl = StrUtil.removePrefix(requestUrl, CommonConst.RESTFUL_PATH);
        // 空的话匹配 [/]
        requestUrl = StrUtil.isBlank(requestUrl) ? StrUtil.SLASH : requestUrl;
        return requestUrl;
    }

    /**
     * 包装请求参数为json
     *
     * @param request 请求
     * @return requestJson
     */
    @SneakyThrows
    public static String requestToJson(HttpServletRequest request) {
        JSONObject requestJsonObj = new JSONObject();
        // get all header
        Map<String, String> headerMap = ServletUtil.getHeaderMap(request);
        requestJsonObj.put("headers", headerMap);
        // get all param
        Map<String, String> paramMap = ServletUtil.getParamMap(request);
        requestJsonObj.put("params", paramMap);
        // body
        request = new ContentCachingRequestWrapper(request);
        @Cleanup BufferedReader reader = request.getReader();
        String body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        requestJsonObj.put("body", body);
        return requestJsonObj.toString();
    }


    /**
     * 根据配置的mockResponse返回mock请求
     *
     * @param mockResponse 系统中配置的mock响应
     */
    public static void doOutResponse(IMockResponse mockResponse, HttpServletResponse response) {
        // header逻辑处理
        String customHeader = mockResponse.getCustomHeader();
        if (StrUtil.isNotBlank(customHeader)) {
            // 将custom header存储的json 反序列化，并遍历存入header.
            JSONArray jsonArray = new JSONArray(customHeader);
            jsonArray.forEach(jsonItem -> {
                String key = ((JSONObject) jsonItem).getStr("key");
                String val = ((JSONObject) jsonItem).getStr("val");
                response.addHeader(key, val);
            });
        }
        // 默认返回contentType为json
        String contentType = response.getContentType();
        if (StrUtil.isBlank(contentType)) {
            response.setContentType(ContentType.JSON.toString(UTF_8));
        }
        // http code
        response.setStatus(mockResponse.getStatusCode());
        outMsg(mockResponse.getContent(), response);
    }

}
