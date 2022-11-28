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

package hostingmanager.api.version;

import static hostingmanager.constants.Constants.CLIENT_ID;
import static hostingmanager.constants.Constants.CLIENT_SECRET;
import static hostingmanager.constants.Constants.DOMAIN;
import static hostingmanager.constants.Constants.PRODUCT_ID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import hostingmanager.api.token.GetToken;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class GetVersionUploadInfo {
    public static void main(String[] args) {
        try {
            // get token
            String token = GetToken.getToken(DOMAIN, CLIENT_ID, CLIENT_SECRET);
            if (StringUtils.isBlank(token)) {
                System.out.println("Failed to get the token.");
                return;
            }
            String uploadFilePath = "D:\\image_1.zip";
            getVersionPopulateFiles(DOMAIN, PRODUCT_ID, CLIENT_ID, token, uploadFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getVersionPopulateFiles(String domain, String productId, String clientId, String token,
        String uploadFilePath) throws IOException {
        HttpPost post = new HttpPost(domain + "/api/cloudhosting/web/v1/version/populateFiles");

        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("client_id", clientId);
        post.setHeader("productId", productId);
        post.setHeader("requestid", UUID.randomUUID().toString());
        post.setHeader("service", "hosting");

        // replace by your actual value
        JSONArray materialInfos = new JSONArray();
        JSONObject materialInfo = new JSONObject();
        File file = FileUtils.getFile(uploadFilePath);
        materialInfo.put("sceneId", "hosting");
        materialInfo.put("fileName", file.getName());
        materialInfo.put("fileSha256", Files.asByteSource(file).hash(Hashing.sha256()).toString());
        materialInfo.put("fileSize", FileUtils.sizeOf(file));
        materialInfo.put("contentType", 902);
        materialInfos.add(materialInfo);

        JSONObject requestObject = new JSONObject();
        requestObject.put("siteId", "CghfVMT******JnoA");
        requestObject.put("version", "j37ZrL4Y*******7coyg");
        requestObject.put("materialInfos", materialInfos);

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
                if (resultObject.containsKey("materialRsps")) {
                    System.out.println("get uploadInfo successfully");
                    return resultObject;
                }
            }
            System.out.println("get uploadInfo failed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}