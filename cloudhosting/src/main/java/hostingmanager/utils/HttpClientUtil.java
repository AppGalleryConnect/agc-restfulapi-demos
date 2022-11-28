/*
 * Copyright 2022. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package hostingmanager.utils;

import static com.google.auth.http.AuthHttpConstants.AUTHORIZATION;
import static com.google.auth.http.AuthHttpConstants.BEARER;

import static hostingmanager.constants.Constants.CLIENT_ID;
import static hostingmanager.constants.Constants.PRODUCT_ID;
import static hostingmanager.constants.Constants.SERVICE;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpClientUtil {

    private CloseableHttpClient httpclient;

    private static final Map<String, String> HEADERS_MAP = new HashMap<>();
    static {
        HEADERS_MAP.put("productId", PRODUCT_ID);
        HEADERS_MAP.put("requestid", UUID.randomUUID().toString());
        HEADERS_MAP.put("client_id", CLIENT_ID);
        HEADERS_MAP.put("service", SERVICE);
        HEADERS_MAP.put("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
    }

    private HttpClientUtil() {
        httpclient = getHttpClient();
    }

    public static HttpClientUtil getInstance() {
        return HttpClientHolderUtil.INSTANCE;
    }

    private static class HttpClientHolderUtil {
        private final static HttpClientUtil INSTANCE = new HttpClientUtil();
    }

    private CloseableHttpClient getHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(3000);
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(30000).build());
        RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(30000)
            .setConnectTimeout(30000)
            .setSocketTimeout(30000)
            .build();
        return HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(defaultRequestConfig)
            .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
            .setRetryHandler(new DefaultHttpRequestRetryHandler(2, true))
            .build();
    }

    public String sendPost(String url, String body, String authorization) {
        return sendPost(url, HEADERS_MAP, body, authorization);
    }

    public String sendPost(String url, Map<String, String> headers, String body, String authorization) {
        HttpPost httpPost = new HttpPost(url);
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(httpPost::setHeader);
        }
        if (StringUtils.isNotBlank(authorization)) {
            httpPost.addHeader(AUTHORIZATION, StringUtils.join(BEARER, " ", authorization));
        }
        StringEntity entity = new StringEntity(body, StandardCharsets.UTF_8);
        entity.setContentEncoding(StandardCharsets.UTF_8.name());
        entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setEntity(entity);
        String result = StringUtils.EMPTY;
        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new Exception("Fail to send request");
            }
            result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String sendDelete(String url, String authorization) {
        return sendDelete(url, HEADERS_MAP, authorization);
    }

    public String sendDelete(String url, Map<String, String> headers, String authorization) {
        HttpDelete httpDelete = new HttpDelete(url);
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(httpDelete::setHeader);
        }
        if (StringUtils.isNotBlank(authorization)) {
            httpDelete.setHeader(AUTHORIZATION, StringUtils.join(BEARER, " ", authorization));
        }

        String result = StringUtils.EMPTY;
        try (CloseableHttpResponse response = httpclient.execute(httpDelete)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new Exception("Fail to send request");
            }
            result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpDelete.releaseConnection();
        }
        return result;
    }
}
