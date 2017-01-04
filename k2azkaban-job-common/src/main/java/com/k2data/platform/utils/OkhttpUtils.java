/**
 * Copyright &copy; 2016 <a href="https://www.k2data.com.cn">K2DATA</a> All rights reserved.
 */
package com.k2data.platform.utils;

import okhttp3.*;
import okhttp3.FormBody.Builder;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp 工具类
 * 
 * @author K2DATA.wsguo
 * @date Jul 20, 2016 10:16:08 AM
 */
public class OkhttpUtils {
    private static final int CONNECT_TIMEOUT = 60;  
    private static final int READ_TIMEOUT = 100;  
    private static final int WRITE_TIMEOUT = 60;  

    private static OkHttpClient client =  
            new OkHttpClient.Builder()  
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间  
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间  
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                    .build();

    /**
     * get请求
     *
     * @param url 请求地址
     * @return 返回结果
     * @throws IOException
     */
    public static Response get(String url) throws IOException {
        return get(client, url, null);
    }

    /**
     * get请求
     * 
     * @param url 请求地址
     * @param params 请求参数
     * @return 返回结果
     * @throws IOException
     */
    public static Response get(String url, Map<String, String> params) throws IOException {
        return get(client, url, params);
    }

    /**
     * get请求
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param client 自定义客户端
     * @return 返回结果
     * @throws IOException
     */
    public static Response get(OkHttpClient client, String url, Map<String, String> params) throws IOException {
        return createGetCall(client, url, params).execute();
    }

    /**
     * get异步请求
     * 
     * @param url 请求地址
     * @param params 请求参数
     * @param callback 回调
     */
    public static void get(String url, Map<String, String> params, Callback callback) {
        get(client, url, params, callback);
    }

    /**
     * get异步请求
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param client 自定义客户端
     * @param callback 回调
     */
    public static void get(OkHttpClient client, String url, Map<String, String> params, Callback callback) {
        createGetCall(client, url, params).enqueue(callback);
    }

    /**
     * post同步请求
     * 
     * @param url 请求地址
     * @param params 请求参数
     * @return 返回结果
     * @throws IOException
     */
    public static Response post(String url, Map<String, String> params) throws IOException {
        return createPostCall(url, params).execute();
    }

    /**
     * post异步请求
     * 
     * @param url 请求地址
     * @param params 请求参数
     * @param callback 回调
     */
    public static void post(String url, Map<String, String> params, Callback callback) {
        createPostCall(url, params).enqueue(callback);
    }

    /**
     * post同步请求
     * 
     * @param url 请求地址
     * @param params 提交参数
     * @param files 提交的文件
     * @return 返回结果
     * @throws IOException
     */
    public static Response post(String url, Map<String, String> params, Map<String, File> files) throws IOException {
        return createPostCall(url, params, files).execute();
    }

    /**
     * post异步请求
     * 
     * @param url 请求地址
     * @param params 提交参数
     * @param files 提交的文件
     * @param callback 回调
     */
    public static void post(String url, Map<String, String> params, Map<String, File> files, Callback callback) {
        createPostCall(url, params, files).enqueue(callback);
    }

    /**
     * 生成 get 请求的 {@link Call} 对象
     * 
     * @param url 地址
     * @param params 参数
     * @return
     */
    private static Call createGetCall(String url, Map<String, String> params) {
        return createGetCall(client, url, params);
    }

    /**
     * 生成 get 请求的 {@link Call} 对象
     *
     * @param client 客户端
     * @param url 地址
     * @param params 参数
     */
    private static Call createGetCall(OkHttpClient client, String url, Map<String, String> params) {
        String urlParams = buildUrlParams(params);
        String urlGet = (urlParams == null)?url:(url + '?' + urlParams);
        Request request = new Request.Builder().get().url(urlGet).build();
        return client.newCall(request);
    }

    /**
     * 生成 get 请求的 url 地址
     * 
     * @param params 请求参数
     * @return
     */
    public static String buildUrlParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (Entry<String, String> entry : params.entrySet()) {
            if (result.length() > 0)
                result.append("&");
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }
        return result.toString();
    }

    /**
     * 生成 post 请求的 {@link Call} 对象
     * 
     * @param url
     * @param params
     * @param files
     * @return
     */
    private static Call createPostCall(String url, Map<String, String> params, Map<String, File> files) {
        okhttp3.MultipartBody.Builder builder = new MultipartBody.Builder();
        // 上传的参数
        if (params != null && !params.isEmpty()) {
            for (Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        // 设置上传的文件
        if (files != null && !files.isEmpty()) {

            for (Entry<String, File> entry : files.entrySet()) {
                File file = entry.getValue();
                String contentType = null;

                boolean isPng = file.getName().endsWith(".png") || file.getName().endsWith(".PNG");

                if (isPng) {
                    contentType = "image/png; charset=UTF-8";
                }

                boolean isJpg = file.getName().endsWith(".jpg") || file.getName().endsWith(".JPG")
                        || file.getName().endsWith(".jpeg") || file.getName().endsWith(".JPEG");
                if (isJpg) {
                    contentType = "image/jpeg; charset=UTF-8";
                }
                if (file.exists()) {
                    RequestBody body = RequestBody.create(MediaType.parse(contentType), file);
                    builder.addFormDataPart(entry.getKey(), file.getName(), body);
                }
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return client.newCall(request);
    }

    /**
     * 生成 post 请求的 {@link Call} 对象
     * 
     * @param url
     * @param params
     * @return
     */
    private static Call createPostCall(String url, Map<String, String> params) {
        Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return client.newCall(request);
    }

}
