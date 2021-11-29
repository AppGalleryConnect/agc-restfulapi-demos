
package version;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import version.model.Site;
import version.model.SiteVersion;
import version.model.request.SiteVersionBase;
import version.model.request.SiteVersionReleaseReq;
import version.model.request.SiteVersionReq;
import version.model.response.SiteVersionQueryResp;
import version.model.response.SiteVersionResp;

/**
 * AGC云托管-云测接口API
 * 【通过api客户端-创建一个全新版本步骤】
 * 查询站点
 * 1. 创建版本
 * 2. 锁定版本
 * 3. 上传压缩包
 * 4. 归档版本
 * 5.1 沙箱发布
 * 5.2 生产发布
 */
public class CreateNewVersion {
    /**
     * 站点名称
     */
    private static final String siteName = "sasaxsacom";

    /**
     * 云托管站点版本增量zip文件路径
     */
    private static final String filePath = "src/main/resources/static/hosting-demo.zip";

    public static void main(String[] args) {
        try {
            String errorMsg;
            // 云测网关头部token
            String cloudToken = AgcCloudgwApi.getCloudToken();
            if (StringUtils.isBlank(cloudToken)) {
                System.out.println("Failed to obtain the token.");
                return;
            }
            // 查询版本
            Site site = AgcCloudgwApi.querySiteByName(siteName, cloudToken);
            // 创建版本
            SiteVersionResp siteVersionResp = AgcCloudgwApi.createHostingSiteVersion(
                SiteVersionReq.builder().siteId(site.getSiteId()).build(), cloudToken);

            errorMsg = "Create version failed. code is " + siteVersionResp.getCode() + " , msg is : "
                + siteVersionResp.getMessage();
            validateCode(siteVersionResp.getCode(), "0", errorMsg);
            SiteVersion siteVersion = siteVersionResp.getSiteVersion();

            // 获取完整版本包上传地址并上传版本包zip文件
            int code = AgcCloudgwApi.populateFilesSiteVersion(siteVersionResp.getSiteVersion(), filePath, cloudToken);
            errorMsg = "Fail to upload version packge. code is " + code;
            validateCode(String.valueOf(code), "200", errorMsg);

            // 归档版本
            SiteVersionResp finalizeResult = AgcCloudgwApi.mergeSiteVersion(
                SiteVersionBase.builder().siteId(site.getSiteId()).version(siteVersion.getVersion()).build(),
                cloudToken);
            errorMsg = "Finalize version failed. code is " + finalizeResult.getCode() + " , msg is : "
                + finalizeResult.getMessage();
            validateCode(finalizeResult.getCode(), "0", errorMsg);

            // 版本发布 environment--发布环境类型：0 生产 1沙箱
            SiteVersionResp productionResult = AgcCloudgwApi.releaseSiteVersion(SiteVersionReleaseReq.builder()
                .siteId(site.getSiteId())
                .version(siteVersion.getVersion())
                .environment(1)
                .build(), cloudToken);
            errorMsg = "Finalize version failed. code is " + productionResult.getCode() + " , msg is "
                + productionResult.getMessage();
            validateCode(productionResult.getCode(), "0", errorMsg);

            // 两个环境首次发布，都是异步发布。需要等到发布成功
            SiteVersion version = queryVersionStatus(
                SiteVersionReq.builder().siteId(site.getSiteId()).version(siteVersion.getVersion()).build(),
                cloudToken);
            errorMsg = "The version sandbox is released failed.";
            validateCode(String.valueOf(version.getStatus()), "11", errorMsg);
            System.out
                .println("The version is released successfully. sandboxUrlPrefix is " + version.getSandboxUrlPrefix());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void validateCode(String code, String valiCode, String msg) throws Exception {
        if (!StringUtils.equals(code, valiCode)) {
            System.out.println(msg);
            throw new Exception(msg);
        }
    }

    private static SiteVersion queryVersionStatus(SiteVersionReq siteVersionReq, String cloudToken) throws Exception {
        Thread.sleep(60000);
        SiteVersionQueryResp siteVersionQueryResp = AgcCloudgwApi.querySiteVersion(siteVersionReq, cloudToken);
        SiteVersion version = CollectionUtils.extractSingleton(siteVersionQueryResp.getSiteVersionList());
        return version;
    }

}