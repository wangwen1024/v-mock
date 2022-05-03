package com.vmock.biz.controller;

import cn.hutool.json.JSONUtil;
import com.vmock.base.utils.ExcelUtil;
import com.vmock.base.vo.Result;
import com.vmock.base.vo.TableDataVo;
import com.vmock.biz.entity.Log;
import com.vmock.biz.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author mock
 */
@Controller
@RequestMapping("/system/log")
public class LogController extends BaseController {

    private static final String PREFIX = "system/log";

    @Autowired
    private ILogService mockLogService;


    @GetMapping
    public String mockLog(String url, String from, ModelMap modelMap) {
        modelMap.addAttribute("requestUrl", url);
        modelMap.addAttribute("from", from);
        return PREFIX + "/mocklog";
    }


    @PostMapping("/list")
    @ResponseBody
    public TableDataVo<Log> list(Log mockLog) {
        startPage();
        List<Log> list = mockLogService.selectMockLogList(mockLog, false);
        return getDataTable(list);
    }


    @PostMapping("/export")
    @ResponseBody
    public Result<Void> export(Log mockLog) {
        List<Log> list = mockLogService.selectMockLogList(mockLog, true);
        ExcelUtil<Log> util = new ExcelUtil<Log>(Log.class);
        return util.exportExcel(list, "操作日志");
    }


    @DeleteMapping
    @ResponseBody
    public Result<Void> clean(Log mockLog) {
        mockLogService.clean(mockLog);
        return success();
    }


    @GetMapping("/request/{logId}")
    public String requestDetail(@PathVariable("logId") Long logId, ModelMap mmap) {
        Log mockLog = mockLogService.getById(logId);
        String requestDetail = mockLog.getRequestDetail();
        mmap.put("mockLog", mockLog);
        mmap.put("requestDetail", JSONUtil.parseObj(requestDetail));
        return PREFIX + "/requestdetail";
    }

    @GetMapping("/response/{logId}")
    public String responseDetail(@PathVariable("logId") Long logId, ModelMap mmap) {
        Log mockLog = mockLogService.getById(logId);
        String responseDetail = mockLog.getResponseDetail();
        mmap.put("mockLog", mockLog);
        mmap.put("responseDetail", JSONUtil.parseObj(responseDetail));
        return PREFIX + "/responsedetail";
    }
}
