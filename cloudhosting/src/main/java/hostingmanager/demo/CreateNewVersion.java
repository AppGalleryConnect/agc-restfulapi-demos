/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package hostingmanager.demo;

import static hostingmanager.constants.Constants.CLIENT_ID;
import static hostingmanager.constants.Constants.CLIENT_SECRET;
import static hostingmanager.constants.Constants.CREATE_VERSION;
import static hostingmanager.constants.Constants.DOMAIN;
import static hostingmanager.constants.Constants.MERGE_VERSION;
import static hostingmanager.constants.Constants.QUERY_SITE;
import static hostingmanager.constants.Constants.RELEASE_PRODUCTION_VERSION;
import static hostingmanager.constants.Constants.VERSION_QUERY;
import static hostingmanager.constants.Constants.VERSION_UPLOAD;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import hostingmanager.api.token.GetToken;
import hostingmanager.constants.Constants;
import hostingmanager.utils.HttpClientUtil;
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

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CreateNewVersion {
    private static final String FILE_PATH = "src/main/resources/static/hosting-demo.zip";

    private static final String SITE_NAME = "hosting***-test";

    public static void main(String[] args) {
        try {
            // 1.get token
            System.out.println("======================step 1 get token======================");
            String token = GetToken.getToken(DOMAIN, CLIENT_ID, CLIENT_SECRET);
            if (StringUtils.isBlank(token)) {
                System.out.println("Failed to get the token.");
                return;
            }

            // 2.get the siteId by querying the site
            System.out.println("======================step 2 get siteId======================");
            JSONObject getSiteListReq = new JSONObject();
            JSONObject siteObject = new JSONObject();
            siteObject.put("siteName", SITE_NAME);
            getSiteListReq.put("site", siteObject);

            String getSiteListResp = HttpClientUtil.getInstance()
                .sendPost(StringUtils.join(DOMAIN, QUERY_SITE), JSON.toJSONString(getSiteListReq), token);
            JSONObject siteListRespObject = JSONObject.parseObject(getSiteListResp);
            JSONArray sitesArray = siteListRespObject.getJSONArray("sites");
            if (sitesArray == null || sitesArray.size() == 0) {
                throw new RuntimeException("get siteInfo failed.");
            }
            JSONObject siteInfoObject = sitesArray.getJSONObject(0);
            String siteId = siteInfoObject.getString("siteId");
            System.out.println("Success to get siteId " + siteId);

            // 3.create version
            System.out.println("======================step 3 create version======================");
            JSONObject createVersionReq = new JSONObject();
            createVersionReq.put("siteId", siteId);

            String createVersionResp = HttpClientUtil.getInstance()
                .sendPost(StringUtils.join(DOMAIN, CREATE_VERSION), JSON.toJSONString(createVersionReq), token);

            JSONObject createVersionRespObject = JSONObject.parseObject(createVersionResp);
            if (!StringUtils.equals(createVersionRespObject.getString("code"), Constants.SUCCESS)) {
                throw new RuntimeException("create version failed.");
            }
            JSONObject siteVersionObject = createVersionRespObject.getJSONObject("siteVersion");
            String versionId = siteVersionObject.getString("version");
            System.out.println("Success to create version, versionId is " + versionId);

            // 4.get version uploadInfo
            System.out.println("======================step 4 get uploadInfo======================");
            JSONObject materialInfoObject = new JSONObject();
            File file = FileUtils.getFile(FILE_PATH);
            materialInfoObject.put("sceneId", Constants.SERVICE);
            materialInfoObject.put("fileName", file.getName());
            materialInfoObject.put("fileSha256", Files.asByteSource(file).hash(Hashing.sha256()).toString());
            materialInfoObject.put("fileSize", FileUtils.sizeOf(file));
            materialInfoObject.put("contentType", Constants.UPLOAD_PACKAGE_CONTENT_TYPE);

            JSONArray materialInfoArray = new JSONArray();
            materialInfoArray.add(materialInfoObject);

            JSONObject getUploadInfoReq = new JSONObject();
            getUploadInfoReq.put("siteId", siteId);
            getUploadInfoReq.put("version", versionId);
            getUploadInfoReq.put("materialInfos", materialInfoArray);
            String versionUploadInfoResp = HttpClientUtil.getInstance()
                .sendPost(StringUtils.join(DOMAIN, VERSION_UPLOAD), JSON.toJSONString(getUploadInfoReq), token);
            JSONObject versionUploadInfoRespObject = JSONObject.parseObject(versionUploadInfoResp);
            if (!versionUploadInfoRespObject.containsKey("materialRsps")) {
                throw new RuntimeException("get uploadInfo failed.");
            }
            System.out.println("Success to get uploadInfo.");

            // 5. upload version file
            System.out.println("======================step 5 upload file======================");
            JSONObject uploadInfoObject =
                versionUploadInfoRespObject.getJSONArray("materialRsps").getJSONObject(0).getJSONObject("uploadInfo");
            String url = uploadInfoObject.getString("url");
            String method = uploadInfoObject.getString("method");
            Map<String, String> headers = JSONObject.parseObject(uploadInfoObject.getString("headers"),
                new TypeReference<Map<String, String>>() {});
            HttpHeaders httpHeaders = new HttpHeaders();
            if (MapUtils.isNotEmpty(headers)) {
                headers.forEach(httpHeaders::add);
            }
            RequestEntity requestEntity = new RequestEntity(new FileSystemResource(FileUtils.getFile(FILE_PATH)),
                httpHeaders, HttpMethod.resolve(StringUtils.upperCase(method)), new URI(url));
            RestTemplate restTemplate = RestTemplateUtil.getRestTemplate();
            ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
            if (responseEntity.getStatusCodeValue() != HttpStatus.SC_OK) {
                throw new RuntimeException("upload version file failed");
            }
            System.out.println("Success to upload version file.");

            // 6.merge version
            System.out.println("======================step 6 merge version======================");
            JSONObject mergeVersionReq = new JSONObject();
            mergeVersionReq.put("siteId", siteId);
            mergeVersionReq.put("version", versionId);
            String mergeVersionResp = HttpClientUtil.getInstance()
                .sendPost(StringUtils.join(DOMAIN, MERGE_VERSION), JSON.toJSONString(mergeVersionReq), token);
            JSONObject mergeVersionRespObject = JSONObject.parseObject(mergeVersionResp);
            if (!StringUtils.equals(mergeVersionRespObject.getString("code"), Constants.SUCCESS)) {
                throw new RuntimeException("merge version failed.");
            }
            System.out.println("Success to merge version.");

            // 7.get versionStatus
            System.out.println("======================step 7 get versionStatus======================");
            String versionStatus = "-";
            do {
                JSONObject versionReq = new JSONObject();
                versionReq.put("siteId", siteId);
                versionReq.put("version", versionId);
                String versionResp = HttpClientUtil.getInstance()
                    .sendPost(StringUtils.join(DOMAIN, VERSION_QUERY), JSON.toJSONString(versionReq), token);

                JSONObject versionRespObject = JSONObject.parseObject(versionResp);
                if (!versionRespObject.containsKey("siteVersionList")) {
                    throw new RuntimeException("get version failed.");
                }
                JSONObject versionInfoObject = versionRespObject.getJSONArray("siteVersionList").getJSONObject(0);
                versionStatus = versionInfoObject.getString("versionStatus");
                TimeUnit.SECONDS.sleep(5);
            } while (StringUtils.equals(versionStatus, "MERGING"));
            if (StringUtils.equals(versionStatus, "FINALIZED")) {
                System.out.println("Success to get versionStatus " + versionStatus);
            } else {
                System.out.println("Merge version failed, get versionStatus " + versionStatus);
            }

            // 8.release sandbox
            System.out.println("======================step 8 release sandbox ======================");
            // environment: 0--production 1--sandbox
            JSONObject versionSandboxReleaseReq = new JSONObject();
            versionSandboxReleaseReq.put("siteId", siteId);
            versionSandboxReleaseReq.put("version", versionId);
            versionSandboxReleaseReq.put("environment", 1);
            String sandboxReleaseResp = HttpClientUtil.getInstance()
                .sendPost(StringUtils.join(DOMAIN, RELEASE_PRODUCTION_VERSION),
                    JSON.toJSONString(versionSandboxReleaseReq), token);
            JSONObject sandboxReleaseRespObject = JSONObject.parseObject(sandboxReleaseResp);
            if (!StringUtils.equals(sandboxReleaseRespObject.getString("code"), Constants.SUCCESS)) {
                throw new RuntimeException("release version sandbox failed.");
            }
            System.out.println("Success to release sandbox.");

            // 9.query publishStatus
            System.out.println("======================step 9 get publishStatus======================");
            String publishStatus = "-";
            String sandboxUrl = "";
            do {
                JSONObject versionReq = new JSONObject();
                versionReq.put("siteId", siteId);
                versionReq.put("version", versionId);
                String versionResp = HttpClientUtil.getInstance()
                    .sendPost(StringUtils.join(DOMAIN, VERSION_QUERY), JSON.toJSONString(versionReq), token);

                JSONObject versionRespObject = JSONObject.parseObject(versionResp);
                if (!versionRespObject.containsKey("siteVersionList")) {
                    throw new RuntimeException("get version failed.");
                }
                JSONObject siteVersionListObject = versionRespObject.getJSONArray("siteVersionList").getJSONObject(0);
                if (siteVersionListObject.containsKey("siteVersionPublishList")) {
                    JSONObject siteVersionPublishList =
                        siteVersionListObject.getJSONArray("siteVersionPublishList").getJSONObject(0);
                    publishStatus = siteVersionPublishList.getString("publishStatus");
                    sandboxUrl = StringUtils.appendIfMissing(
                        StringUtils.prependIfMissing(siteVersionPublishList.getString("mappingKey"), "https://"),
                        "/index.html");

                }
                TimeUnit.SECONDS.sleep(5);
            } while (StringUtils.equals(publishStatus, "SANDBOX_RELEASING"));
            if (StringUtils.equals(publishStatus, "SANDBOX_RELEASED")) {
                System.out.println("Success to get versionStatus " + publishStatus);
                System.out.println("sandboxUrl: " + sandboxUrl);
            } else {
                System.out.println("Release sandbox failed, get versionStatus " + publishStatus);
            }

            // test sandbox release
            TimeUnit.SECONDS.sleep(300);

            // 10.release version production
            System.out.println("======================step 10 release production ======================");
            // environment: 0--production 1--sandbox
            JSONObject versionProductionReleaseReq = new JSONObject();
            versionProductionReleaseReq.put("siteId", siteId);
            versionProductionReleaseReq.put("version", versionId);
            versionProductionReleaseReq.put("environment", 0);
            String productionReleaseResp = HttpClientUtil.getInstance()
                .sendPost(StringUtils.join(DOMAIN, RELEASE_PRODUCTION_VERSION),
                    JSON.toJSONString(versionProductionReleaseReq), token);
            JSONObject productionReleaseRespObject = JSONObject.parseObject(productionReleaseResp);
            if (!StringUtils.equals(productionReleaseRespObject.getString("code"), Constants.SUCCESS)) {
                throw new RuntimeException("release version production failed.");
            }
            System.out.println("Success to release production.");

            // 11.query version publishStatus
            System.out.println("======================step 11 get publishStatus======================");
            String productionPublishStatus = "";
            String productionUrl = "";
            do {
                JSONObject versionReq = new JSONObject();
                versionReq.put("siteId", siteId);
                versionReq.put("version", versionId);
                String productionVersionResp = HttpClientUtil.getInstance()
                    .sendPost(StringUtils.join(DOMAIN, VERSION_QUERY), JSON.toJSONString(versionReq), token);

                JSONObject productionVersionRespObject = JSONObject.parseObject(productionVersionResp);
                if (!productionVersionRespObject.containsKey("siteVersionList")) {
                    throw new RuntimeException("get version failed.");
                }
                JSONObject siteVersionListObject =
                    productionVersionRespObject.getJSONArray("siteVersionList").getJSONObject(0);
                if (siteVersionListObject.containsKey("siteVersionPublishList")) {
                    JSONObject siteVersionPublishList =
                        siteVersionListObject.getJSONArray("siteVersionPublishList").getJSONObject(0);
                    productionPublishStatus = siteVersionPublishList.getString("publishStatus");
                    productionUrl =
                        StringUtils.prependIfMissing(siteVersionPublishList.getString("mappingKey"), "https://");
                }
                TimeUnit.SECONDS.sleep(5);
            } while (StringUtils.equals(productionPublishStatus, "PRODUCTION_RELEASING"));
            if (StringUtils.equals(productionPublishStatus, "PRODUCTION_RELEASED")) {
                System.out.println("Success to get versionStatus " + productionPublishStatus);
                System.out.println("productionUrl: " + productionUrl);
            } else {
                System.out.println("Release production failed, get versionStatus " + productionPublishStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
