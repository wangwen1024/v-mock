package com.vmock.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmock.base.constant.CommonConst;
import com.vmock.base.utils.DataBaseUtils;
import com.vmock.biz.entity.Url;
import com.vmock.biz.entity.UrlLogic;
import com.vmock.biz.mapper.UrlMapper;
import com.vmock.biz.service.IUrlLogicService;
import com.vmock.biz.service.IUrlService;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static cn.hutool.core.util.StrUtil.COMMA;

/**
 * url路径Service业务层处理
 *
 * @author mock
 * @date 2019-11-20
 */
@Service
public class UrlServiceImpl extends ServiceImpl<UrlMapper, Url> implements IUrlService {

    private static final String SQL_REGEXP_FORMAT = "({}|\\{path\\})";
    private static final String SQL_REGEXP_PATH = "(\\{path\\})";
    private static final String SQL_REGEXP = "SELECT url_id, description, remark, update_time, del_flag, url, create_by, update_by," +
            " create_time, name, logic FROM mock_url WHERE logic REGEXP {} limit 1";
    @Value("${spring.datasource.druid.url}")
    private String dbPath;
    @Autowired
    private IUrlLogicService mockUrlLogicService;


    /**
     * 根据Url查询
     *
     * @param url url路径
     * @return MockUrl
     */
    @Override
    public Url selectMockUrlByUrl(String url) {
        return this.getOne(Wrappers.<Url>lambdaQuery().eq(Url::getUrl, url));
    }

    /**
     * 根据logic查询
     *
     * @param logic logic字符串
     * @return MockUrl
     */
    @Override
    public Url selectMockUrlByLogic(String logic) {
        return this.getOne(Wrappers.<Url>lambdaQuery().eq(Url::getLogic, logic));
    }

    /**
     * 查询url路径列表
     *
     * @param mockUrl url路径
     * @return url路径
     */
    @Override
    public List<Url> selectMockUrlList(Url mockUrl) {
        return this.list(Wrappers.<Url>lambdaQuery()
                .like(StrUtil.isNotBlank(mockUrl.getName()), Url::getName, mockUrl.getName())
                .like(StrUtil.isNotBlank(mockUrl.getDescription()), Url::getDescription, mockUrl.getDescription())
                .orderByDesc(Url::getCreateTime));
    }

    /**
     * 新增url路径
     *
     * @param mockUrl url路径
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertMockUrl(Url mockUrl) {
        // url format
        String url = this.formatUrlStr(mockUrl.getUrl());
        // logic insert
        String urlLogic = this.insertUrlLogic(url);
        // 保存转换后的logic
        mockUrl.setLogic(urlLogic);
        return this.save(mockUrl);
    }

    /**
     * 简单处理入参URL
     *
     * @param url
     * @return url
     */
    @Override
    public String insertUrlLogic(String url) {
        // 处理逻辑 logic 表 url插入
        List<UrlLogic> mockUrlLogics = mockUrlLogicService.insertByUrl(url);
        // 转为map <subUrl, logicId>
        Map<String, Long> urlAndId = mockUrlLogics.stream()
                .collect(Collectors.toMap(UrlLogic::getSubUrl, UrlLogic::getLogicId));
        // 处理logic字段
        StringJoiner logicJoiner = new StringJoiner(COMMA);
        // 根据url 转为对应的logic字符串
        Arrays.stream(url.split("\\/")).filter(StrUtil::isNotBlank).map(i ->
                // if {path} in url, return {path}
                CommonConst.PATH_PLACEHOLDER.equalsIgnoreCase(i) ? CommonConst.PATH_PLACEHOLDER : urlAndId.get(i).toString()
        ).collect(Collectors.toList()).forEach(logicJoiner::add);
        return logicJoiner.toString();
    }

    /**
     * 处理url格式
     *
     * @param url url格式
     */
    @Override
    public String formatUrlStr(String url) {
        // not start with /, concat it.
        if (!url.startsWith(StrUtil.SLASH)) {
            url = StrUtil.SLASH.concat(url);
        }
        // end with /, remove it
        while (url.endsWith(StrUtil.SLASH) && url.length() > 1) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * url unique check
     *
     * @param url    url path
     * @param urlId, for edit page
     */
    @Override
    public boolean isUniqueUrl(String url, Long urlId) {
        // format url
        url = this.formatUrlStr(url);
        int count = this.count(Wrappers.<Url>lambdaQuery()
                // url
                .eq(Url::getUrl, url)
                // id
                .notIn(urlId != null, Url::getUrlId, urlId));
        return count == 0;
    }

    /**
     * 根据逻辑创建正则查url
     *
     * @param requestUrlLogic logic string
     * @return mockUrl entity
     */
    @SneakyThrows
    @Override
    public Url getUrlByRegexp(String requestUrlLogic) {
        if (StrUtil.isNotBlank(requestUrlLogic)) {
            // 注册一个带正则的Connection
            @Cleanup Connection connection = DriverManager.getConnection(dbPath);
            DataBaseUtils.createRegexpFun(connection);
            // sql gen
            // "12,13,14" => ["12","13","14"]
            String[] afterSplit = requestUrlLogic.split(COMMA);
            StringJoiner regexpJoiner = new StringJoiner(StrUtil.COMMA);
            // 将数组遍历 拼接 为正则
            Arrays.stream(afterSplit).forEach(item ->
                    // path 直接拼接，数字logic 加上或逻辑拼接
                    regexpJoiner.add(CommonConst.PATH_PLACEHOLDER.equals(item) ? SQL_REGEXP_PATH : StrUtil.format(SQL_REGEXP_FORMAT, item)));
            String regexp = "'^".concat(regexpJoiner.toString()).concat("$'");
            // 查询
            Map<String, Object> mockUrlMap = DataBaseUtils.queryMap(connection, StrUtil.format(SQL_REGEXP, regexp));
            if (mockUrlMap != null) {
                // 转为实体
                return BeanUtil.mapToBean(mockUrlMap, Url.class, true);
            }
        }
        return null;
    }

    /**
     * 修改返回类型
     *
     * @param urlId url主键
     * @param type  目标返回类型
     */
    @Override
    public void changeResponseType(Long urlId, Integer type) {
        Url urlEntity = new Url();
        urlEntity.setUrlId(urlId);
        urlEntity.setResponseType(type);
        this.updateById(urlEntity);
    }

    /**
     * 修改url路径
     *
     * @param mockUrl url路径
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateMockUrl(Url mockUrl) {
        // url处理
        String url = this.formatUrlStr(mockUrl.getUrl());
        String urlLogic = this.insertUrlLogic(url);
        // 保存转换后的logic
        mockUrl.setLogic(urlLogic);
        return this.updateById(mockUrl);
    }

    /**
     * 删除url路径对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public boolean deleteMockUrlByIds(String ids) {
        return this.removeByIds(StrUtil.splitTrim(ids, COMMA));
    }
}
