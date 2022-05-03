package com.vmock.base.core.url;

import com.vmock.base.utils.SpringUtils;
import com.vmock.biz.entity.Url;
import com.vmock.biz.service.IUrlLogicService;
import com.vmock.biz.service.IUrlService;

/**
 * path传参-参数与SubURL冲突场景-最坏情况
 *
 * @author vt
 * @since 2020-5-14
 */
public class PathConflictUrlHandler extends BaseMockUrlHandler {

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
        String requestUrlLogic = mockUrlLogicService.selectLogicStrByUrl(requestUrl);
        return mockUrlService.getUrlByRegexp(requestUrlLogic);
    }
}
