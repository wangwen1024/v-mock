package com.vmock.biz.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmock.biz.entity.Log;
import com.vmock.biz.mapper.LogMapper;
import com.vmock.biz.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author mock
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    /**
     * 页面来源 url
     */
    private static final String FROM_URL = "url";

    @Autowired
    private LogMapper mockLogMapper;

    /**
     * 查询系统操作日志集合
     *
     * @param mockLog  操作日志对象
     * @param isExport 是否为excel导出
     * @return 操作日志集合
     */
    @Override
    public List<Log> selectMockLogList(Log mockLog, boolean isExport) {
        return this.list(getFilterWrapper(mockLog, isExport).orderByDesc(Log::getCreateTime));
    }

    /**
     * 异步插入
     *
     * @param mockLog log实体
     */
    @Async
    @Override
    public void asyncInsert(Log mockLog) {
        mockLog.insert();
    }

    /**
     * 清除指定区间数据
     *
     * @param mockLog log实体
     */
    @Override
    public void clean(Log mockLog) {
        this.remove(getFilterWrapper(mockLog, false));
        mockLogMapper.deleteInvalidData();
    }

    /**
     * 获取过滤数据的wrapper
     *
     * @param mockLog  参数实体
     * @param isExport 是否excel导出的过滤，excel导出查询全部字段。
     */
    private LambdaQueryWrapper<Log> getFilterWrapper(Log mockLog, boolean isExport) {
        Long beginTime = Convert.toLong(mockLog.getParams().get("beginTime"));
        Long endTime = Convert.toLong(mockLog.getParams().get("endTime"));
        String from = Convert.toStr(mockLog.getParams().get("from"));
        LambdaQueryWrapper<Log> mockLogWrapper = Wrappers.<Log>lambdaQuery()
                // like ip
                .like(StrUtil.isNotBlank(mockLog.getRequestIp()), Log::getRequestIp, mockLog.getRequestIp())
                // like hit url
                .like(StrUtil.isNotBlank(mockLog.getHitUrl()) && !FROM_URL.equals(from), Log::getHitUrl, mockLog.getHitUrl())
                // equals hit url, in url log page
                .eq(StrUtil.isNotBlank(mockLog.getHitUrl()) && FROM_URL.equals(from), Log::getHitUrl, mockLog.getHitUrl())
                // equals method
                .eq(StrUtil.isNotBlank(mockLog.getRequestMethod()), Log::getRequestMethod, mockLog.getRequestMethod())
                // beginTime
                .ge(beginTime != null, Log::getCreateTime, beginTime)
                // endTime 加入当日的时间戳
                .le(endTime != null, Log::getCreateTime, endTime);
        // 非导出场景
        if (!isExport) {
            // 只查询需要字段，排除大json字段
            mockLogWrapper.select(Log::getLogId, Log::getHitUrl, Log::getRequestMethod, Log::getCreateTime, Log::getRequestIp);
        }
        return mockLogWrapper;
    }
}
