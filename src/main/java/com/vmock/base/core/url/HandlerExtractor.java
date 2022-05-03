package com.vmock.base.core.url;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

/**
 * 提供获取mockUrl的handler
 *
 * @author vt
 * @since 2020-5-14
 */
@UtilityClass
public class HandlerExtractor {

    /**
     * 初始化需要的handler
     *
     * @return BaseMockUrlHandler实现类集合
     */
    private static List<BaseMockUrlHandler> initMockUrlHandlers() {
        // 实例各个handler 按照顺序
        List<BaseMockUrlHandler> mockUrlHandlers = Arrays
                .asList(new DirectHitUrlHandler(),
                        new PathUrlHandler(),
                        new PathConflictUrlHandler(),
                        new UrlNotFoundHandler());
        return mockUrlHandlers;
    }

    /**
     * 赋值链式关系 获取首个元素
     *
     * @return 对外使用的handler
     */
    public static BaseMockUrlHandler getHandler() {
        List<BaseMockUrlHandler> mockUrlHandlers = initMockUrlHandlers();
        BaseMockUrlHandler pre = null;
        for (BaseMockUrlHandler mockUrlHandler : mockUrlHandlers) {
            if (pre != null) {
                pre.setNextLogger(mockUrlHandler);
            }
            pre = mockUrlHandler;
        }
        return mockUrlHandlers.get(0);
    }

}
