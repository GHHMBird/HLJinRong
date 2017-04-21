package com.haili.finance.helper;

import android.text.TextUtils;

import com.haili.finance.http.BusinessEnum;

/*
 * Created by lfu on 2017/2/21.
 */

public class URLHelper {

//    public String URL = "http://192.168.80.50:8080/gateway-web/app/";
    public String URL = "http://192.168.4.199:8080/hl-004-gateway-web/app/";
//    public String URL = "http://192.168.4.212:8080/hl-004-gateway-web/app/";//柯
//    public String URL = "http://192.168.4.210:8080/hl-004-gateway-web/app/";//金城

    public String getRequestUrl(BusinessEnum model, String methodName) {
        if (TextUtils.isEmpty(methodName)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        switch (model) {
            case BUSINESS_USER:
                sb.append("user");
                break;
        }
        sb.append(methodName);
        return sb.toString();
    }

    public static URLHelper getInstance() {
        return Singleton.instance;
    }

    private static final class Singleton {
        public static final URLHelper instance = new URLHelper();
    }

}
