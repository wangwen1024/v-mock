package com.vmock.base.core.url;

import com.vmock.base.utils.SpringUtils;
import com.vmock.biz.entity.Url;
import com.vmock.biz.service.IUrlService;

/**
 * 直接命中 - 最优情况
 *
 * @author vt
 * @since 2020-5-14
 */
public class DirectHitUrlHandler extends BaseMockUrlHandler {

    /**
     * url service
     */
    private IUrlService mockUrlService = SpringUtils.getBean(IUrlService.class);


    @Override
    protected Url find(String requestUrl) {
        return mockUrlService.selectMockUrlByUrl(requestUrl);
    }
}
