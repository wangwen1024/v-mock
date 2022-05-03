package com.vmock.base.core.response;

import com.vmock.biz.enums.ResponseTypeEnum;

import java.util.EnumMap;

/**
 * 提供获取mockResponse的处理类
 *
 * @author vt
 * @since 2020-5-14
 */
public class StrategyExtractor {

    /**
     * 返回所有响应策略
     *
     * @return 所有策略的枚举map
     */
    public static EnumMap<ResponseTypeEnum, IResponseStrategy> getResponseStrategies() {
        // 包装为enumMap
        EnumMap<ResponseTypeEnum, IResponseStrategy> strategyEnumMap = new EnumMap<>(ResponseTypeEnum.class);
        strategyEnumMap.put(ResponseTypeEnum.MANUAL, new ManualIResponseStrategy());
        strategyEnumMap.put(ResponseTypeEnum.RESTFUL, new RestfulResponseStrategy());
        // 暂未开发auto模式
        return strategyEnumMap;
    }
}
