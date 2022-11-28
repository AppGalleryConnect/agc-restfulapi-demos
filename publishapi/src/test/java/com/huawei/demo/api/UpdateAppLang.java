/*
 * Copyright 2019. Huawei Technologies Co., Ltd. All rights reserved.
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

package com.huawei.demo.api;

import com.huawei.demo.common.KeyConstants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 功能描述
 *
 * @since 2021-01-13
 */
public class UpdateAppLang {

    private static String LANG = "ar";  // Type of language you need to update

    private static String APP_NAME = "PublishAPI_Demo"; // App name update

    private static String APP_DESC = "This is a demo"; // Application-related description information

    public static void updateAppLang(String domain, String clientId, String token, String appId) throws IOException {
        HttpPut put = new HttpPut(domain + "/publish/v2/app-language-info?appId" + appId);
        put.setHeader("Authorization", "Bearer " + token);
        put.setHeader("client_id", clientId);

        JSONObject keyString = new JSONObject();
        keyString.put("lang", LANG);
        keyString.put("appName", APP_NAME);
        keyString.put("appDesc", APP_DESC);

        StringEntity entity = new StringEntity(keyString.toString(), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        put.setEntity(entity);

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(put);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                    new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();

                JSONObject object = JSON.parseObject(result);
                JSONObject ret = (JSONObject) object.get("ret");
                if (ret.get("code").equals(KeyConstants.SUCCESS)) {
                    // The application language information was updated successfully
                } else {
                    // Application language information update failed, please refer to the error code statement in the
                    // materials for specific reasons
                }
                br.close();
                httpClient.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
