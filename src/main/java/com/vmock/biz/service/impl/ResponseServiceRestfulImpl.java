package com.vmock.biz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmock.base.constant.CommonConst;
import com.vmock.base.vo.ResponseRestfulVo;
import com.vmock.biz.entity.ResponseRestful;
import com.vmock.biz.mapper.ResponseRestfulMapper;
import com.vmock.biz.service.IResponseRestfulService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 返回体 restful 模式 Service业务层处理
 *
 * @author mock
 * @date 2019-11-20
 */
@Service
public class ResponseServiceRestfulImpl extends ServiceImpl<ResponseRestfulMapper, ResponseRestful> implements IResponseRestfulService {

    /**
     * 根据urlId和httpMethod转为的int code查询一条数据
     *
     * @param code  httpMethodCode
     * @param urlId url的Id
     * @return 返回体 实体
     */
    @Override
    public ResponseRestful getOneByCodeAndUrlId(Integer code, Long urlId) {
        return this.getOne(Wrappers.<ResponseRestful>lambdaQuery()
                // where urlId = urlId
                .eq(ResponseRestful::getUrlId, urlId)
                // and httpMethod = code
                .eq(ResponseRestful::getHttpMethod, code)
                // limit 1
                .last(CommonConst.LIMIT_ONE)
        );
    }

    /**
     * 指定url的Restful接口列表
     *
     * @param responseRestful 条件
     * @return 列表
     */
    @Override
    public List<ResponseRestfulVo> selectResponseRestfulList(ResponseRestful responseRestful) {
        return baseMapper.getRestfulList(responseRestful.getUrlId());
    }

    /**
     * 所有字段 用于导出
     *
     * @param responseRestful 条件
     * @return 列表
     */
    @Override
    public List<ResponseRestful> list(ResponseRestful responseRestful) {
        return baseMapper.selectList(Wrappers.<ResponseRestful>lambdaQuery()
                // where urlId = urlId
                .eq(ResponseRestful::getUrlId, responseRestful.getUrlId()));
    }
}
