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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Function description.
 *
 * 
 * @since 2019-10-24
 */
public class GetToken {
    public static String getToken(String domain, String clientId, String clientSecret) {
        String token = null;
        try {
            HttpPost post = new HttpPost(domain + "/oauth2/v1/token");

            JSONObject keyString = new JSONObject();
            keyString.put("client_id", clientId);
            keyString.put("client_secret", clientSecret);
            keyString.put("grant_type", "client_credentials");

            StringEntity entity = new StringEntity(keyString.toString(), Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            post.setEntity(entity);

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            CloseableHttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {

                BufferedReader br =
                    new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();
                JSONObject object = JSON.parseObject(result);
                token = object.getString("access_token");
                br.close();
            }
            httpClient.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }
}
