package com.k2data.platform.kmx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.k2data.platform.kmx.cond.KmxCond;
import com.k2data.platform.kmx.cond.KmxDevicesListV2Cond;
import com.k2data.platform.utils.Global;
import com.k2data.platform.utils.OkhttpUtils;
import com.k2data.platform.utils.StringUtils;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 连接大数据平台通用客户端
 * 
 * @author lidong
 * @since 2016-7-26
 */
public class KmxClient {

    private static final Logger logger = LogManager.getLogger(KmxClient.class);

    public static final String ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SZZ";
    public static final int PAGE_SIZE = 10000; //页大小

    private static OkHttpClient client;
    static {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(Integer.valueOf(Global.getConfig("kmx.max.requests")));
        dispatcher.setMaxRequestsPerHost(Integer.valueOf(Global.getConfig("kmx.max.requests")));

        client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)      // 读取超时时间
            .writeTimeout(60, TimeUnit.SECONDS)     // 写超时时间
            .connectTimeout(60, TimeUnit.SECONDS)   // 连接超时时间
            .dispatcher(dispatcher)
            .build();
    }
    
    /**
     * 同步 get 请求
     * 
     * @param cond 查询条件
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSync(final KmxCond cond) {
        Map<String, String> params = cond.getParams();
        String url = cond.getUrl();

        try {
            logger.info("Kmx sync get. url: {}", url);
            logger.debug("params: {}", params);

            Response response = OkhttpUtils.get(client, url, params);

            return (T) handleResponse(cond, url, params, response, null);
        } catch (IOException e) {
            throw new KmxException("Kmx get error! url: " + url + ", params: " + params.toString(), e);
        }
    }

    /**
     * 异步 get 请求
     * 
     * @param cond 查询条件
     * @param handler 处理结果接口
     */
    @SuppressWarnings("unchecked")
    public static void getAsync(final KmxCond cond, final KmxResponseHandler handler) {
        final Map<String, String> params = cond.getParams();
        String url = cond.getUrl();

        OkhttpUtils.get(client, url, params, new Callback() {
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                logger.info("Kmx async get callback. url: {}", url);
                logger.debug("params: {}", params);

                handleResponse(cond, url, params, response, handler);
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("Kmx get error! url: " + call.request().url().toString(), e);
            }
        });
    }

    /**
     * 处理 {@link Response}, 把json序列化成对象
     *
     * @param response 返回的{@link Response}
     * @param handler 异步调用的处理接口
     * @return 序列化后的对象
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private static Object handleResponse(KmxCond cond, String url, Map<String, String> params, Response response, KmxResponseHandler handler) throws IOException {
        if(!(cond instanceof KmxDevicesListV2Cond) && !response.isSuccessful())
            throw new KmxException("Kmx get error! code: " + response.code() + ", url: " + url + ", params: " + params);

        String responseStr = response.body().string();

        if (StringUtils.isBlank(responseStr)) {
            logger.warn("Kmx get error, Response body is blank!" + " url: " + url + ", params: " + params);
            return null;
        }

        JSONObject rowsJson = JSON.parseObject(responseStr);

        if (rowsJson.getInteger("code") != 0) {
            logger.warn("get Kmx data error, code: {}, message: {}, url: {}, params: {}", rowsJson.getInteger("code"), rowsJson.getString("message"), url, params);
        }

        Object object = JSON.toJavaObject(rowsJson, cond.getClazz());

        if (handler != null)
            handler.handleResponse(object);

        return object;
    }

}
