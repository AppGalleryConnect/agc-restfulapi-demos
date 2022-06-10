/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package hostingmanager.api.version;

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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class DeleteVersion {
    public static void main(String[] args) {
        try {
            // get token
            String token = GetToken.getToken(DOMAIN, CLIENT_ID, CLIENT_SECRET);
            if (StringUtils.isBlank(token)) {
                System.out.println("Failed to get the token.");
                return;
            }
            deleteVersion(DOMAIN, PRODUCT_ID, CLIENT_ID, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteVersion(String domain, String productId, String clientId, String token) {
        // replace by your actual value
        String versionId = "B2jApgm********yUlw";

        HttpDelete delete = new HttpDelete(domain + "/api/cloudhosting/web/v1/version/" + versionId);
        delete.setHeader("Authorization", "Bearer " + token);
        delete.setHeader("client_id", clientId);
        delete.setHeader("productId", productId);
        delete.setHeader("requestid", UUID.randomUUID().toString());
        delete.setHeader("service", "hosting");
        delete.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = HttpClients.createDefault().execute(delete)) {
            String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            JSONObject resultObject = JSON.parseObject(result);
            System.out.println(resultObject);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                if (StringUtils.equals(resultObject.getString("code"), "0")) {
                    System.out.println("delete version successfully");
                    return;
                }
            }
            System.out.println("delete version fail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
