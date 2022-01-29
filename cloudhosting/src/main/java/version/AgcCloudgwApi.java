
package version;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import version.model.MaterialFileInfoV2;
import version.model.SiteVersionPublish;
import version.model.response.MaterialResp;
import version.model.MaterialUploadInfo;
import version.model.Site;
import version.model.SiteVersion;
import version.model.request.AssignVersionReq;
import version.model.request.SiteQueryReq;
import version.model.request.SiteVersionBase;
import version.model.request.SiteVersionReleaseReq;
import version.model.request.SiteVersionReq;
import version.model.request.SiteVersionLockReq;
import version.model.response.CodeResp;
import version.model.response.SiteQueryResp;
import version.model.response.SiteVersionLockResp;
import version.model.response.SiteVersionQueryResp;
import version.model.response.SiteVersionResp;

public class AgcCloudgwApi {
    /**
     * agc云测接口地址，
     * 中国存储地站点：connect-api.cloud.huawei.com
     * 亚非拉存储地站点：connect-api-dra.cloud.huawei.com
     * 欧洲存储地站点：connect-api-dre.cloud.huawei.com
     * 俄罗斯存储地站点：connect-api-drru.cloud.huawei.com
     */
    private static final String DOMAIN = "https://connect-api.cloud.huawei.com";

    /**
     * agc-API客户端Id
     */
    private static final String CLIENTID = "726190*******91808";

    /**
     * agc-API客户端秘钥
     */
    private static final String CLIENTSECRET = "91A484AD75B755866**************************A30B4A3BEF9325B53F";

    /**
     * agc-项目id
     */
    private static final String PRODUCTID = "258779******930596";

    /**
     * 云托管业务服务标识
     */
    private static final String SERVICE = "hosting";

    /**
     * 创建版本 uri
     */
    private static final String OAUTH2_TOKEN = "/api/oauth2/v1/token";

    /**
     * 创建版本 uri
     */
    private static final String CREATE_VERSION = "/api/cloudhosting/web/v1/version/create";

    /**
     * 修改版本文件列表 uri
     */
    private static final String ASSIGN_VERSION = "/api/cloudhosting/web/v1/version/modifyFilelist";

    /**
     * 获取上传地址 uri
     */
    private static final String POPULATE_VERSION = "/api/cloudhosting/web/v1/version/populateFiles";

    /**
     * 归档版本 uri
     */
    private static final String MERGE_VERSION = "/api/cloudhosting/web/v1/version/merge";

    /**
     * 发布版本 uri
     */
    private static final String RELEASE_PRODUCTION_VERSION = "/api/cloudhosting/web/v1/version/release";

    /**
     * 查询站点 uri
     */
    private static final String SITES_QUERY = "/api/cloudhosting/web/v1/sites/query";

    /**
     * 查询版本 uri
     */
    private static final String VERSION_QUERY = "/api/cloudhosting/web/v1/version/query";

    /**
     * 查询归档版本文件 uri
     */
    private static final String VERSION_FILES_QUERY = "/api/cloudhosting/web/v1/version/files/query";

    /**
     * 删除版本 uri
     */
    private static final String DELETE_VERSION = "/api/cloudhosting/web/v1/version/{versionId}";

    /**
     * api: 获取云托管云测网关token
     */
    public static String getCloudToken() throws IOException {
        String token = null;
        HttpPost post = null;
        CloseableHttpClient httpClient = null;
        try {
            post = new HttpPost(DOMAIN + OAUTH2_TOKEN);
            JSONObject keyString = new JSONObject();
            keyString.put("client_id", CLIENTID);
            keyString.put("client_secret", CLIENTSECRET);
            keyString.put("grant_type", "client_credentials");

            StringEntity entity = new StringEntity(keyString.toString(), StandardCharsets.UTF_8);
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            post.setEntity(entity);

            httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                    new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();
                JSONObject object = JSON.parseObject(result);
                token = object.getString("access_token");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            post.releaseConnection();
            httpClient.close();
        }
        return token;
    }

    /**
     * api: 查询站点信息
     */
    public static Site querySiteByName(String siteName, String authorization) throws Exception {
        Site site = Site.builder().siteName(siteName).build();
        String responseBody = sendRequest(SiteQueryReq.builder().site(site).build(), SITES_QUERY, authorization, "POST");
        SiteQueryResp siteQueryResp = JSON.parseObject(responseBody, SiteQueryResp.class);
        if (siteQueryResp==null || CollectionUtils.isEmpty(siteQueryResp.getSites())) {
            throw new Exception("Failed to query the site. The result is empty.");
        }
        return CollectionUtils.extractSingleton(siteQueryResp.getSites());
    }

    /**
     * api: 查询站点版本信息
     */
    public static SiteVersionQueryResp querySiteVersion(SiteVersionReq siteVersionQueryReq, String authorization)
            throws Exception {
        String responseBody = sendRequest(siteVersionQueryReq, VERSION_QUERY, authorization, "POST");
        SiteVersionQueryResp siteVersionQueryResp = JSON.parseObject(responseBody, SiteVersionQueryResp.class);
        if (siteVersionQueryResp == null || CollectionUtils.isEmpty(siteVersionQueryResp.getSiteVersionList())) {
            throw new Exception("Failed to query the site version. The query result is empty.");
        }
        return siteVersionQueryResp;
    }


    /**
     * api: 创建云托管站点版本
     */
    public static SiteVersionResp createHostingSiteVersion(SiteVersionReq siteVersion, String authorization)
        throws Exception {
        String responseBody = sendRequest(siteVersion, CREATE_VERSION, authorization, "POST");
        SiteVersionResp siteVersionResp = JSON.parseObject(responseBody, SiteVersionResp.class);
        String errorMsg = "Create version failed. code is " + siteVersionResp.getCode() + " , msg is : "
            + siteVersionResp.getMessage();
        validateCode(siteVersionResp.getCode(), "0", errorMsg);
        // 查询版本状态是否为 已创建
        valiedVersionStatus(siteVersionResp.getSiteVersion(), "CREATED", authorization);
        return siteVersionResp;
    }


    /**
     * api: 云托管修改版本文件列表
     * 在基于基线版本创建的当前版本中，删除对应路径的文件
     * 说明：如果无基线版本则不需要该步骤
     */
    public static CodeResp modifySiteVersion(SiteVersion siteVersion, String authorization) throws Exception {
        // 需要删除的文件集合
        List<String> deleteFiles = Arrays.asList("/image/yanxishe.png", "/image/sitemanager.png");
        AssignVersionReq assignVersionReq = AssignVersionReq.builder()
            .version(siteVersion.getVersion())
            .siteId(siteVersion.getSiteId())
            .deleteFiles(deleteFiles)
            .build();
        String responseBody = sendRequest(assignVersionReq, ASSIGN_VERSION, authorization, "POST");
        CodeResp codeResp = JSON.parseObject(responseBody, CodeResp.class);
        String errorMsg = "Assign version failed. code is " + codeResp.getCode() + " , msg is : " + codeResp.getMessage();
        validateCode(codeResp.getCode(), "0", errorMsg);
        return codeResp;
    }

    /**
     * api: 云托管 获取文件上传地址
     * 修改版本完成后，获取文件上传地址后并上传文件
     * 说明：获取文件上传地址后，不可再次修改版本
     */
    public static void populateFilesSiteVersion(SiteVersion siteVersion, String versionFilePath, String authorization)
        throws Exception {
        // 获取文件上传地址
        SiteVersionLockReq siteVersionLockReq = convertLockBody(versionFilePath, siteVersion);
        String responseBody = sendRequest(siteVersionLockReq, POPULATE_VERSION, authorization, "POST");
        SiteVersionLockResp siteVersionLockResp = JSON.parseObject(responseBody, SiteVersionLockResp.class);
        if (siteVersionLockResp == null || CollectionUtils.isEmpty(siteVersionLockResp.getMaterialRsps())) {
            throw new Exception("Failed to obtain the upload address. The upload information is empty.");
        }
        // 文件上传至云托管
        int code = AgcCloudgwApi.uploadVersionFile(CollectionUtils.extractSingleton(siteVersionLockResp.getMaterialRsps()), versionFilePath);
        String errorMsg = "Fail to upload version packge. code is " + String.valueOf(code);
        validateCode(String.valueOf(code), "200", errorMsg);
    }

    /**
     * 上传完整版本zip文件
     * 上传版本zip包至云托管
     */
    public static int uploadVersionFile(MaterialResp materialRsp, String filePath) throws IOException {
        MaterialUploadInfo uploadInfo = materialRsp.getUploadInfo();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/zip");
        RequestBody body = RequestBody.create(mediaType, new File(filePath));

        Request.Builder request = new Request.Builder();
        Iterator iterator = uploadInfo.getHeaders().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            request.addHeader((String) entry.getKey(), (String) entry.getValue());
        }

        request.url(uploadInfo.getUrl());
        request.method(uploadInfo.getMethod(), body);
        Request request1 = request.build();
        Response response = client.newCall(request1).execute();
        return response.code();
    }

    /**
     * 组装素材上传参数
     */
    public static SiteVersionLockReq convertLockBody(String filePath, SiteVersion siteVersion) throws IOException {
        File tmpFile = new File(filePath);
        MaterialFileInfoV2 materialFileInfoV2 = MaterialFileInfoV2.builder()
            .fileName(tmpFile.getName())
            .fileSha256(Files.asByteSource(tmpFile).hash(Hashing.sha256()).toString())
            .contentType(900)
            .sceneId(PRODUCTID)
            .fileSize(FileUtils.sizeOf(tmpFile))
            .fileAccessRight(1)
            .build();
        List<MaterialFileInfoV2> materialFileInfoV2s = new ArrayList<>();
        materialFileInfoV2s.add(materialFileInfoV2);
        return SiteVersionLockReq.builder()
            .version(siteVersion.getVersion())
            .siteId(siteVersion.getSiteId())
            .materialInfos(materialFileInfoV2s)
            .build();
    }

    /**
     * api: 归档版本
     * 处理基线版本和当前版本文件内容
     * 说明：一旦归档版本，则不可以再次锁定版本
     */
    public static SiteVersionResp mergeSiteVersion(SiteVersionBase version, String authorization) throws Exception {
        String responseBody = sendRequest(version, MERGE_VERSION, authorization, "POST");
        SiteVersionResp finalizeResult = JSON.parseObject(responseBody, SiteVersionResp.class);
        String errorMsg = "Finalize version failed. code is " + finalizeResult.getCode() + " , msg is : "
                + finalizeResult.getMessage();
        validateCode(finalizeResult.getCode(), "0", errorMsg);
        valiedVersionStatus(finalizeResult.getSiteVersion(), "FINALIZED", authorization);
        return finalizeResult;
    }


    /**
     * api: 发布版本
     * 发布环境（相互隔离，发布沙箱不影响生产环境的版本和业务流量）：
     * 1. 沙箱环境
     * 2. 生产环境
     */
    public static SiteVersionResp releaseSiteVersion(SiteVersionReleaseReq version, String cloudToken)
        throws Exception {
        String responseBody = sendRequest(version, RELEASE_PRODUCTION_VERSION, cloudToken, "POST");
        SiteVersionResp siteVersionResp = JSON.parseObject(responseBody, SiteVersionResp.class);
        String errorMsg = "Release version failed. code is " + siteVersionResp.getCode() + " , msg is "
            + siteVersionResp.getMessage();
        validateCode(siteVersionResp.getCode(), "0", errorMsg);
        return siteVersionResp;
    }

    /**
     * api: 云托管站点版本删除
     * **当前版本不能删除
     */
    public static SiteVersionResp deleteHostingVersion(String version, String authorization) throws Exception {
        String url = DELETE_VERSION.replace("{versionId}", version);
        String responseBody = sendRequest(new Object(), url, authorization, "DELETE");
        return JSON.parseObject(responseBody, SiteVersionResp.class);
    }

    /**
     * 发送请求
     */
    public static String sendRequest(Object object, String uri, String authorization, String method) throws Exception {
        String objectBody = JSON.toJSONString(object);
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, objectBody);
        Request request = new Request.Builder().url(DOMAIN + uri)
            .method(method, body)
            .addHeader("productId", PRODUCTID)
            .addHeader("Authorization", "Bearer " + authorization)
            .addHeader("requestid", String.valueOf(UUID.randomUUID()))
            .addHeader("client_id", CLIENTID)
            .addHeader("service", SERVICE)
            .addHeader("Content-Type", "application/json")
            .build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            String msg = response.body().string();
            throw new Exception(
                "Failed to send the request. code is " + String.valueOf(response.code()) + ", msg is " + msg);
        }
        String bodyString = response.body().string();
        if (StringUtils.isBlank(bodyString)) {
            throw new Exception("Failed to send the request, response body is empty.");
        }
        return bodyString;
    }


    /**
     * 检查版本状态是否符合进行下一步操作
     */
    public static SiteVersion valiedVersionStatus(SiteVersion siteVersion, String versionStatus, String cloudToken)
        throws Exception {
        if (StringUtils.equals(siteVersion.getVersionStatus(), versionStatus)) {
            return siteVersion;
        }
        // 根据版本包的大小和数据数量定义等待时间，一般2-5分钟即可。
        Thread.sleep(300000);
        SiteVersionReq build =
            SiteVersionReq.builder().siteId(siteVersion.getSiteId()).version(siteVersion.getVersion()).build();
        SiteVersionQueryResp siteVersionQueryResp = querySiteVersion(build, cloudToken);
        SiteVersion version = CollectionUtils.extractSingleton(siteVersionQueryResp.getSiteVersionList());
        if (!StringUtils.equals(version.getVersionStatus(), versionStatus)) {
            System.out.println("Failed to verify the version status. The verification value is " + versionStatus
                + ". The current versionStatus is " + version.getVersionStatus());
            throw new Exception("Failed to verify the version status. The verification value is " + versionStatus
                + ". The current versionStatus is " + version.getVersionStatus());
        }
        return version;
    }

    /**
     * 检查版本发布状态是否符合进行下一步操作
     */
    public static String valiedPublishStatus(SiteVersion siteVersion, String publishStatus, String cloudToken) throws Exception{
        // 根据版本包的大小和数据数量定义发布等待时间，一般2-5分钟即可。
        Thread.sleep(300000);
        SiteVersionReq build =
                SiteVersionReq.builder().siteId(siteVersion.getSiteId()).version(siteVersion.getVersion()).build();
        SiteVersionQueryResp siteVersionQueryResp = querySiteVersion(build, cloudToken);
        SiteVersion version = CollectionUtils.extractSingleton(siteVersionQueryResp.getSiteVersionList());
        List<SiteVersionPublish> siteVersionPublishList = version.getSiteVersionPublishList();
        List<SiteVersionPublish> versionCollect = siteVersionPublishList.stream()
                .filter(siteVersionPublish -> siteVersionPublish.getEnvironment() == 1)
                .collect(Collectors.toList());
        SiteVersionPublish siteVersionPublish = CollectionUtils.extractSingleton(versionCollect);
        if (siteVersionPublish != null && !StringUtils.equals(siteVersionPublish.getPublishStatus(), publishStatus)) {
            System.out.println("Failed to verify the version status. The verification value is " + publishStatus
                    + ". The current versionStatus is " + siteVersionPublish.getPublishStatus());
            throw new Exception("Failed to verify the version status. The verification value is " + publishStatus
                    + ". The current versionStatus is " + siteVersionPublish.getPublishStatus());
        }
        String sandboxAccessUrl = "https://" + version.getSandboxUrlPrefix();
        return sandboxAccessUrl;
    }

    /**
     * 判断是否处理成功
     * @param code code
     * @param valiCode valiCode
     * @param msg msg
     * @throws Exception
     */
    private static void validateCode(String code, String valiCode, String msg) throws Exception {
        if (!StringUtils.equals(code, valiCode)) {
            System.out.println(msg);
            throw new Exception(msg);
        }
    }
}
