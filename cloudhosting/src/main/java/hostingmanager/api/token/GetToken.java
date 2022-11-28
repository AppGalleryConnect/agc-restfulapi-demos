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

package hostingmanager.api.token;

import static hostingmanager.constants.Constants.CLIENT_ID;
import static hostingmanager.constants.Constants.CLIENT_SECRET;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import hostingmanager.constants.Constants;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;

public class GetToken {
    public static void main(String[] args) {
        String token = getToken(Constants.DOMAIN, CLIENT_ID, CLIENT_SECRET);
        System.out.println(token);
    }

    public static String getToken(String domain, String clientId, String clientSecret) {
        HttpPost post = new HttpPost(domain + "/api/oauth2/v1/token");

        JSONObject keyString = new JSONObject();
        keyString.put("client_id", clientId);
        keyString.put("client_secret", clientSecret);
        keyString.put("grant_type", "client_credentials");

        StringEntity entity = new StringEntity(keyString.toString(), StandardCharsets.UTF_8);
        entity.setContentEncoding(StandardCharsets.UTF_8.name());
        entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        post.setEntity(entity);

        try (CloseableHttpResponse response = HttpClients.createDefault().execute(post)) {
            String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            JSONObject resultObject = JSON.parseObject(result);
            System.out.println(resultObject);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("get token successfully");
                return resultObject.getString("access_token");
            }
            System.out.println("get token failed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}