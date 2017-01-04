/**
 * Copyright &copy; 2016 <a href="https://www.k2data.com.cn">K2DATA</a> All rights reserved.
 */
package com.k2data.platform.nvr;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.k2data.platform.nvr.domain.NvrMachineContrast;
import com.k2data.platform.utils.Global;
import com.k2data.platform.utils.HttpsUtils;
import com.k2data.platform.utils.OkhttpUtils;
import okhttp3.*;
import okhttp3.Request.Builder;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author K2DATA.wsguo
 * @date Aug 3, 2016 4:57:00 PM    
 * 
 */
public class NvrClient {
    
    private static final int CONNECT_TIMEOUT = 60; 
    private static final int READ_TIMEOUT = 100;
    private static final int WRITE_TIMEOUT = 60;
    
    private static final String NVR_GET_TOKEN_URL = Global.getConfig("nvr.token.url");
    private static final String NVR_SSN = Global.getConfig("nvr.ssn");
    private static final String NVR_PASSWORD = Global.getConfig("nvr.password");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient nvrClient = createNvrClient();
    private static final Logger logger = LogManager.getLogger(NvrClient.class);

    /**
     * 创建httpclient对象
     *
     * @author K2DATA.wsguo
     * @date Aug 3, 2016 5:01:43 PM   
     * @return
     */
    private static OkHttpClient createNvrClient() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(sslParams.unSafeHostnameVerifier) // 不验证主机名,如果不加 这一句 报错javax.net.ssl.SSLPeerUnverifiedException: Hostname iotserver2.nvr-china.com not verified:
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间  
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间  
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间  
                .build();
        return okHttpClient;
    }
    
    /**
     * 生成请求JSON参数
     *
     * @author K2DATA.wsguo
     * @date Aug 3, 2016 5:22:27 PM   
     * @param params
     * @return 
     */
    private static String genParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return StringUtils.EMPTY;
        }
        StringBuilder result = new StringBuilder();
        result.append("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                result.append(",");
            }
            result.append("\"").append(entry.getKey()).append("\"");
            result.append(":");
            result.append("\"").append(entry.getValue()).append("\"");
            first = false;
        }
        result.append("}");
        return result.toString();
    }
    
    /**
     * 生成 post 请求的 {@link Call} 对象,添加请求头
     *
     * @author K2DATA.wsguo
     * @date Aug 3, 2016 6:10:49 PM   
     * @param url
     * @param params
     * @return
     */
    private static Call createPostCall(String url, Map<String, String> params, Map<String, String> headers) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, genParams(params));
        Builder build = new Request.Builder();
        if(headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                build.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = build.post(body).url(url).build();
        return nvrClient.newCall(request);
    }
    
    public static Call createGetCall(String url, Map<String, String> params, Map<String, String> headers) {
        Builder build = new Request.Builder();
        if(headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                build.addHeader(entry.getKey(), entry.getValue());
            }
        }

        String urlParams = OkhttpUtils.buildUrlParams(params);
        String urlGet = (urlParams == null)?url:(url + '?' + urlParams);

        Request request = build.get().url(urlGet).build();
        return nvrClient.newCall(request);
    }

    public static NvrMachineContrast getResponseBody(String url, Map<String, String> params) {
        String token = NvrClient.getToken();
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("token", token);

        try {
            return JSON.parseObject(createGetCall(url, params, headers).execute().body().string(),
                    NvrMachineContrast.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public  static Response get(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return createGetCall(url, params, headers).execute();
    }

    public static String getJson(String url, Map<String, String> params, Map<String, String> headers) {
        try {
            return get(url, params, headers).body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public  static JSONObject getJSON(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        Response response = get(url, params, headers);
        if(!response.isSuccessful()) {
            logger.error("request error " + response);
            throw new IOException("Unexpected code " + response);
        }
        return JSON.parseObject(response.body().string());
    }
    
    /**
     * post同步请求
     *
     * @author K2DATA.wsguo
     * @date Aug 3, 2016 6:10:42 PM   
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    private static Response post(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return createPostCall(url, params, headers).execute();
    }
    
    /**
     * 获取post json数据
     *
     * @author K2DATA.wsguo
     * @date Aug 4, 2016 3:45:54 PM   
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws IOException
     */
    private static JSONObject postJSON(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        Response response = post(url, params, headers);
        if(!response.isSuccessful()) {
            logger.error("request error " + response);
            throw new IOException("Unexpected code " + response);
        }
        return JSON.parseObject(response.body().string());
    }

    /**
     * 获取token
     *
     * @author K2DATA.wsguo
     * @date Aug 4, 2016 4:24:58 PM   
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String getToken(String url, Map<String, String> params, Map<String, String> headers) {
        String token = "";
        
        try {
            JSONObject responseJson = postJSON(url, params, null);
            if(responseJson.getInteger("code") == null) {
                token = responseJson.getString("token");
            } else {
                logger.error("get token error! " + responseJson.getInteger("code") + "-"  + responseJson.getString("message"));
            }
            return token;
        } catch (IOException e) {
            logger.error("get token error! " + e);
        }
        return token;
    }
    /**
     * 获取Token
     *
     * @author K2DATA.wsguo
     * @date Aug 4, 2016 4:24:12 PM   
     * @return
     */
    public static String getToken() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ssn", NVR_SSN);
        params.put("password", NVR_PASSWORD);
        return getToken(NVR_GET_TOKEN_URL, params, null);
    }
    
    
    public static void main(String[] args) {
        String token = getToken();
        System.out.println(token);
    }
}
