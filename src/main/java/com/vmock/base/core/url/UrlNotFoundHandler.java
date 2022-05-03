package com.vmock.base.core.url;

import com.vmock.base.utils.ContextUtils;
import com.vmock.base.utils.OutMsgUtils;
import com.vmock.biz.entity.Url;

/**
 * 没有找到该MockURL-兜底处理
 *
 * @author vt
 * @since 2020-5-14
 */
public class UrlNotFoundHandler extends BaseMockUrlHandler {

    @Override
    protected Url find(String requestUrl) {
        OutMsgUtils.mockUrlNotFond(ContextUtils.getResponse());
        return null;
    }
}
