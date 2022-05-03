package com.vmock.base.core.url;

import com.vmock.base.utils.SpringUtils;
import com.vmock.biz.entity.Url;
import com.vmock.biz.service.IUrlLogicService;
import com.vmock.biz.service.IUrlService;

/**
 * path传参场景
 *
 * @author vt
 * @since 2020-5-14
 */
public class PathUrlHandler extends BaseMockUrlHandler {

    /**
     * url service
     */
    private IUrlService mockUrlService = SpringUtils.getBean(IUrlService.class);

    /**
     * logic url service
     */
    private IUrlLogicService mockUrlLogicService = SpringUtils.getBean(IUrlLogicService.class);

    @Override
    protected Url find(String requestUrl) {
        // 获取url logic字符串
        String requestUrlLogic = mockUrlLogicService.selectLogicStrByUrl(requestUrl);
        return mockUrlService.selectMockUrlByLogic(requestUrlLogic);
    }
}
