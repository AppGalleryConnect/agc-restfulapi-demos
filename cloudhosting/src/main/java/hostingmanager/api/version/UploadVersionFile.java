/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package hostingmanager.api.version;

import static hostingmanager.api.version.GetVersionUploadInfo.getVersionPopulateFiles;
import static hostingmanager.constants.Constants.CLIENT_ID;
import static hostingmanager.constants.Constants.CLIENT_SECRET;
import static hostingmanager.constants.Constants.DOMAIN;
import static hostingmanager.constants.Constants.PRODUCT_ID;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import hostingmanager.api.token.GetToken;
import hostingmanager.utils.RestTemplateUtil;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

public class UploadVersionFile {
    public static void main(String[] args) {
        try {
            // get token
            String token = GetToken.getToken(DOMAIN, CLIENT_ID, CLIENT_SECRET);
            if (StringUtils.isBlank(token)) {
                System.out.println("Failed to get the token.");
                return;
            }
            uploadFile(DOMAIN, PRODUCT_ID, CLIENT_ID, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void uploadFile(String domain, String productId, String clientId, String token) {
        try {
            String uploadFilePath = "D:\\hosting-demo.zip";
            JSONObject uploadInfoResp = getVersionPopulateFiles(domain, productId, clientId, token, uploadFilePath);
            System.out.println(uploadInfoResp);

            JSONObject uploadInfoObject =
                uploadInfoResp.getJSONArray("materialRsps").getJSONObject(0).getJSONObject("uploadInfo");
            String url = uploadInfoObject.getString("url");
            String method = uploadInfoObject.getString("method");
            Map<String, String> headers = JSONObject.parseObject(uploadInfoObject.getString("headers"),
                new TypeReference<Map<String, String>>() {});

            HttpHeaders httpHeaders = new HttpHeaders();
            if (MapUtils.isNotEmpty(headers)) {
                headers.forEach(httpHeaders::add);
            }
            RequestEntity requestEntity = new RequestEntity(new FileSystemResource(FileUtils.getFile(uploadFilePath)),
                httpHeaders, HttpMethod.resolve(StringUtils.upperCase(method)), new URI(url));

            RestTemplate restTemplate = RestTemplateUtil.getRestTemplate();
            ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
            System.out.println(responseEntity.toString());
            if (responseEntity.getStatusCodeValue() == HttpStatus.SC_OK) {
                System.out.println("upload successfully");
                // upload file successfully
            } else {
                System.out.println("upload failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
