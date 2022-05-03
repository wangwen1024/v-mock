package com.vmock.biz.controller;

import com.vmock.base.utils.ExcelUtil;
import com.vmock.base.vo.ResponseRestfulVo;
import com.vmock.base.vo.Result;
import com.vmock.base.vo.TableDataVo;
import com.vmock.biz.entity.Response;
import com.vmock.biz.entity.ResponseRestful;
import com.vmock.biz.enums.ResponseTypeEnum;
import com.vmock.biz.service.IResponseRestfulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 返回体Controller
 *
 * @author mock
 * @date 2019-11-20
 */
@Controller
@RequestMapping("/system/response/restful")
public class ResponseRestfulController extends BaseController {

    private static final String PREFIX = "system/response";

    @Autowired
    private IResponseRestfulService responseRestfulService;


    /**
     * 查询返回体列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataVo<ResponseRestfulVo> list(ResponseRestful mockResponse) {
        startPage();
        List<ResponseRestfulVo> list = responseRestfulService.selectResponseRestfulList(mockResponse);
        return getDataTable(list);
    }

    /**
     * 详情页
     */
    @GetMapping("/{responseId}")
    public String detail(@PathVariable("responseId") Long responseId, ModelMap mmap) {
        ResponseRestful mockResponse = responseRestfulService.getById(responseId);
        mmap.put("mockResponse", mockResponse);
        mmap.put("responseType", ResponseTypeEnum.RESTFUL.name());
        return PREFIX + "/detail";
    }

    /**
     * 修改返回体
     */
    @GetMapping("/edit/{responseId}")
    public String edit(@PathVariable("responseId") Long responseId, ModelMap mmap) {
        ResponseRestful mockResponse = responseRestfulService.getById(responseId);
        mmap.put("responseType", ResponseTypeEnum.RESTFUL.name());
        mmap.put("mockResponse", mockResponse);
        return PREFIX + "/edit";
    }

    /**
     * 修改保存返回体
     */
    @PostMapping("/edit")
    @ResponseBody
    public Result editSave(ResponseRestful mockResponse) {
        return create(responseRestfulService.updateById(mockResponse));
    }

    /**
     * 新增返回体
     */
    @GetMapping("/add")
    public String add(String urlId, ModelMap mmap) {
        mmap.put("responseType", ResponseTypeEnum.RESTFUL.name());
        mmap.put("urlId", urlId);
        return PREFIX + "/add";
    }


    /**
     * 新增保存返回体
     */
    @PostMapping("/add")
    @ResponseBody
    public Result addSave(ResponseRestful mockResponse, String httpMethodCode) {
        //  以{@link org.springframework.http.HttpMethod}的ordinal作为字典顺
        mockResponse.setHttpMethod(HttpMethod.resolve(httpMethodCode).ordinal());
        return create(responseRestfulService.save(mockResponse));
    }

    /**
     * 导出返回体列表
     */
    @PostMapping("/export")
    @ResponseBody
    public Result<Void> export(ResponseRestful mockResponse) {
        List<ResponseRestful> list = responseRestfulService.list(mockResponse);
        ExcelUtil<ResponseRestful> util = new ExcelUtil<>(ResponseRestful.class);
        return util.exportExcel(list, "responseRestful");
    }

}
