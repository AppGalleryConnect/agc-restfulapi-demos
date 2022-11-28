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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 功能描述
 *
 * @since 2021-01-13
 */
public class GetAppIdList {
    private static String PACKAGE_NAME = "com.huawei.app.TestAPI11"; // Give the packageName value of the appId you need
                                                                    // to query

    public static void getAppIdList(String domain, String clientId, String token) {
        HttpGet get = new HttpGet(domain + "/publish/v2/appid-list?packageName" + PACKAGE_NAME);
        get.setHeader("client_id", clientId);
        get.setHeader("Authorization", "Bearer " + token);

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(get);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                    new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();
                JSONObject object = JSON.parseObject(result);
                JSONObject ret = (JSONObject) object.get("ret");
                if (ret.get("code").equals(KeyConstants.SUCCESS)) {
                    // Get the application ID successfully
                } else {
                    // Failed to get the application ID, please refer to the meaning of the error code for the
                    // specific reason
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
