/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package hostingmanager.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateUtil {
    private RestTemplateUtil() {

    }

    public static RestTemplate getRestTemplate() {
        return RestTemplateHolderUtil.INSTANCE;
    }

    private static class RestTemplateHolderUtil {
        private final static RestTemplate INSTANCE = new RestTemplate(RestTemplateUtil.getClientHttpRequestFactory());
    }

    private static ClientHttpRequestFactory getClientHttpRequestFactory() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(3000);
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(30000).build());
        RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(30000)
            .setConnectTimeout(30000)
            .setSocketTimeout(30000)
            .setSocketTimeout(30000)
            .build();
        HttpClient httpClient = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(defaultRequestConfig)
            .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
            .setRetryHandler(new DefaultHttpRequestRetryHandler(2, true))
            .build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
            new HttpComponentsClientHttpRequestFactory(httpClient);
        return clientHttpRequestFactory;
    }
}
