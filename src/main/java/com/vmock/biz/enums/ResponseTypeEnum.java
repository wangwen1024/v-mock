package com.vmock.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应类型
 *
 * @author vt
 * @since 2020年05月15日
 */
@AllArgsConstructor
public enum ResponseTypeEnum {


    /**
     * 手动模式
     */
    MANUAL(1, "手动模式"),

    /**
     * Restful模式
     */
    RESTFUL(2, "Restful模式");

    /**
     * 自动模式 (暂未开发)
     */
    // AUTO(3, "自动模式");


    private static final Map<Integer, String> MAPPINGS
            = new HashMap<>((int) (ResponseTypeEnum.values().length / 0.75F) + 1);

    static {
        for (ResponseTypeEnum typeEnum : values()) {
            MAPPINGS.put(typeEnum.getCode(), typeEnum.getLabel());
        }
    }

    /**
     * 类型值
     */
    @Getter
    private final Integer code;

    @Getter
    private final String label;

    /**
     * 根据code获取对应枚举
     *
     * @param code code值
     * @return 响应类型enum
     */
    public static ResponseTypeEnum getByCode(Integer code) {
        for (ResponseTypeEnum item : ResponseTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 获取kv形式
     *
     * @return 映射map
     */
    public static Map<Integer, String> getMapping() {
        return MAPPINGS;
    }
}
