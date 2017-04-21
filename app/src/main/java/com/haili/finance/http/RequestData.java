package com.haili.finance.http;

import com.haili.finance.helper.URLHelper;

/*
 * Created by lfu on 2017/2/22.
 */

public abstract class RequestData {


    public String getUrl() {
        return URLHelper.getInstance().getRequestUrl(getBusinessType(),getInterfaceName());
    }

    /*
     * 对应的业务模块
     *
     * @return
     */
    public abstract BusinessEnum getBusinessType();

    /*
     * 对应的接口名称
     *
     * @return
     */
    public abstract String getInterfaceName();


    /*
     * 是否需要缓存该Request对应的响应数据
     *
     * @return
     */
    public abstract boolean isNeedCache();

    /*
     * 缓存该Request对应的响应数据的时间有效期
     *
     * @return 缓存周期（毫秒）
     */
    public abstract long getCachePeriod();

    /*
     * Request数据拼接的字符串
     *
     * @return
     */
    public abstract String getRequestKey();
}
