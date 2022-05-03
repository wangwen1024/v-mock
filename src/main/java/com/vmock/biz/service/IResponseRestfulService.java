package com.vmock.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vmock.base.vo.ResponseRestfulVo;
import com.vmock.biz.entity.ResponseRestful;

import java.util.List;

/**
 * 返回体Service接口
 *
 * @author mock
 * @date 2019-11-20
 */
public interface IResponseRestfulService extends IService<ResponseRestful> {

    /**
     * 根据urlId和httpMethod转为的int code查询一条数据
     *
     * @param code  httpMethodCode
     * @param urlId url的Id
     * @return 返回体 实体
     */
    ResponseRestful getOneByCodeAndUrlId(Integer code, Long urlId);

    /**
     * 指定url的Restful接口列表
     *
     * @param responseRestful 条件
     * @return 列表
     */
    List<ResponseRestfulVo> selectResponseRestfulList(ResponseRestful responseRestful);

    /**
     * 所有字段 用于导出
     *
     * @param responseRestful 条件
     * @return 列表
     */
    List<ResponseRestful> list(ResponseRestful responseRestful);
}
