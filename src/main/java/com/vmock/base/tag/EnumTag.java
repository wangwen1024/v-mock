package com.vmock.base.tag;

import com.vmock.biz.enums.ResponseTypeEnum;
import org.springframework.stereotype.Service;

import java.util.Map;

import static cn.hutool.core.util.StrUtil.EMPTY;

/**
 * 枚举tag
 *
 * @author mock
 */
@Service("enums")
public class EnumTag {


    /**
     * 根据字典类型查询字典数据信息
     *
     * @param code 字典类型
     * @return 名称
     */
    public String getResponseTypeLabel(Integer code) {
        ResponseTypeEnum typeEnum = ResponseTypeEnum.getByCode(code);
        if (typeEnum != null) {
            return typeEnum.getLabel();
        }
        return EMPTY;
    }

    /**
     * 获取响应类型枚举Map
     *
     * @return 枚举转为的Map
     */
    public Map<Integer, String> getResponseTypes() {
        return ResponseTypeEnum.getMapping();
    }

}
