package com.vmock.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vmock.biz.entity.Log;
import org.apache.ibatis.annotations.Delete;

/**
 * 操作日志 数据层
 *
 * @author mock
 */
public interface LogMapper extends BaseMapper<Log> {

    /**
     * 真实删除delete flag为1的
     *
     * @return 影响行数
     */
    @Delete("delete from mock_log where del_flag = 1")
    int deleteInvalidData();

}
