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

package hostingmanager.api.site;

import static hostingmanager.constants.Constants.CLIENT_ID;
import static hostingmanager.constants.Constants.CLIENT_SECRET;
import static hostingmanager.constants.Constants.DOMAIN;
import static hostingmanager.constants.Constants.PRODUCT_ID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import hostingmanager.api.token.GetToken;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class GetSiteList {
    public static void main(String[] args) {
        try {
            // get token
            String token = GetToken.getToken(DOMAIN, CLIENT_ID, CLIENT_SECRET);
            if (StringUtils.isBlank(token)) {
                System.out.println("Failed to get the token.");
                return;
            }
            getSiteList(DOMAIN, PRODUCT_ID, CLIENT_ID, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getSiteList(String domain, String productId, String clientId, String token) {
        HttpPost post = new HttpPost(domain + "/api/cloudhosting/web/v1/site/query");

        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("client_id", clientId);
        post.setHeader("productId", productId);
        post.setHeader("requestid", UUID.randomUUID().toString());
        post.setHeader("service", "hosting");

        // replace by your actual value
        JSONObject requestObject = new JSONObject();
        JSONObject siteObject = new JSONObject();
        siteObject.put("siteName", "hosting****-test");
        requestObject.put("site", siteObject);

        StringEntity entity = new StringEntity(requestObject.toString(), StandardCharsets.UTF_8);
        entity.setContentEncoding(StandardCharsets.UTF_8.name());
        entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        post.setEntity(entity);

        try (CloseableHttpResponse response = HttpClients.createDefault().execute(post)) {
            String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            JSONObject resultObject = JSON.parseObject(result);
            System.out.println(resultObject);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("get siteList successfully");
                return resultObject;
            }
            System.out.println("get siteList failed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}