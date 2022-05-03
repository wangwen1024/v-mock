package com.vmock.base.vo;

import lombok.Data;

/**
 * restful模式的查询vo
 *
 * @author vt
 * @since 2020-5-14
 */
@Data
public class ResponseRestfulVo {

    /**
     * 主键
     */
    private Long responseId;

    /**
     * 所属url
     */
    private Long urlId;

    /**
     * 描述
     */
    private String description;

    /**
     * 返回http状态码
     */
    private Integer statusCode;


    /**
     * http方法名
     */
    private String dictValue;


}
