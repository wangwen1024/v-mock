package com.vmock.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vmock.base.vo.ResponseRestfulVo;
import com.vmock.biz.entity.ResponseRestful;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 返回体Mapper接口
 *
 * @author mock
 * @date 2019-11-20
 */
public interface ResponseRestfulMapper extends BaseMapper<ResponseRestful> {

    /**
     * restful模式列表
     *
     * @param urlId 所属urlId
     * @return restful模式列表
     */
    List<ResponseRestfulVo> getRestfulList(@Param("urlId") Long urlId);
}
