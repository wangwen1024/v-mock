package com.vmock.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vmock.base.annotation.Excel;
import com.vmock.base.core.response.IMockResponse;
import lombok.Data;

/**
 * 返回体对象restful模式 mock_response_restful
 *
 * @author mock
 * @date 2020-05-15
 */
@Data
@TableName("mock_response_restful")
public class ResponseRestful extends BaseEntity<ResponseRestful> implements IMockResponse {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long responseId;

    /**
     * 所属url
     */
    private Long urlId;

    /**
     * 返回体
     */
    @Excel(name = "返回体")
    private String content;

    /**
     * 返回http状态码
     */
    @Excel(name = "返回http状态码")
    private Integer statusCode;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String description;

    /**
     * 自定义响应头
     */
    @Excel(name = "自定义响应头")
    private String customHeader;

    /**
     * 请求方法
     * 以spring的{@link org.springframework.http.HttpMethod}枚举ordinal为准即可
     */
    @Excel(name = "请求方法", readConverterExp = "0=GET,1=HEAD,2=POST,3=PUT,4=PATCH,5=DELETE,6=OPTIONS,7=TRACE")
    private Integer httpMethod;

}
