package com.haili.finance.http;

import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.helper.URLHelper;
import com.haili.finance.modle.DeviceInfoModel;
import com.haili.finance.stroage.DataCache;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Created by lfu on 2017/2/21.
 */

public class RetrofitClient {

    /*
     * 所有post请求form的KEY
     */
    public static final String POST_FIELD_NAME = "Json";

    private Retrofit retrofit;


    public RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(URLHelper.getInstance().URL)
                .client(getHttpClient())
                .build();
    }

    public InterfaceService getInterfaceService() {
        return retrofit.create(InterfaceService.class);
    }

    /*
     * 自定义RetrofitClient
     *
     * @param baseUrl             自定义baseUrl
     * @param useRawJsonConverter 是否使用JSON字符串转换器
     */
    public RetrofitClient(String baseUrl, boolean useRawJsonConverter) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addConverterFactory(useRawJsonConverter ? RawJsonConverterFactory.create() : GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        builder.baseUrl(baseUrl);
        builder.client(getHttpClient());
        retrofit = builder.build();
    }


    /*
     * 自定义OkHttpClient 设置超时和LOG
     *
     * @return
     */
    private OkHttpClient getHttpClient() {
        final DeviceInfoModel model ;
        if ( DataCache.instance.getCacheData("haili", "DeviceInfoModel") == null){
            model = new DeviceInfoModel();
            LoginResponse response = DataCache.instance.getCacheData("haili","LoginResponse");
            if (response != null){
                model.accessToken =  response.loginModel.accessToken;
            }
        } else {
            model =DataCache.instance.getCacheData("haili", "DeviceInfoModel");
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("version", model.version)
                                .addHeader("phoneId", model.phoneId)
//                                .addHeader("channnelId",model.channnelId)
                                .addHeader("phoneVersion",model.phoneVersion)
                                .addHeader("devicevendor",model.devicevendor)
                                .addHeader("devicemodel",model.devicemodel)
                                .addHeader("lang",model.lang)
                                .addHeader("os",model.os)
                                .addHeader("accessToken",model.accessToken)
                                .addHeader("sign","")
                                .addHeader("gps",model.gps)
                                .build();
                        return chain.proceed(request);
                    }
                })
                .retryOnConnectionFailure(true)
                .sslSocketFactory(getSslScoketFactory())
                .hostnameVerifier(getHostnameVerifier())
                .build();
    }

    public HostnameVerifier getHostnameVerifier() {
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        return DO_NOT_VERIFY;
    }


    public SSLSocketFactory getSslScoketFactory() {

        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }

    /*
     * 接口数据转换器，该转换器不做任何转换，直接输出JSON字符串
     */
    private static class RawJsonConverterFactory extends Converter.Factory {

        public static RawJsonConverterFactory create() {
            return new RawJsonConverterFactory();
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return new RawJsonResponseBodyConverter();
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
        }

        @Override
        public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return super.stringConverter(type, annotations, retrofit);
        }
    }


    private static class RawJsonResponseBodyConverter<String> implements Converter<ResponseBody, String> {

        public RawJsonResponseBodyConverter() {

        }

        @Override
        public String convert(ResponseBody value) throws IOException {

            return (String) value.string();
        }
    }
}
