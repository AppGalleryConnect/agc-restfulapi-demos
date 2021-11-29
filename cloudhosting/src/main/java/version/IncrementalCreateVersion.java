
package version;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


import version.model.Site;
import version.model.SiteVersion;
import version.model.request.SiteVersionBase;
import version.model.request.SiteVersionReleaseReq;
import version.model.request.SiteVersionReq;
import version.model.response.CodeResp;
import version.model.response.SiteVersionQueryResp;
import version.model.response.SiteVersionResp;

/**
 * AGC云托管-云测接口API
 * 【通过api客户端-基于基线版本，增量创建新版本步骤】
 * 查询站点
 * 1. 创建版本
 * 2. 修改版本
 * 3. 锁定版本
 * 4. 上传压缩包
 * 5. 归档版本
 * 6.1 沙箱发布
 * 6.2 生产发布
 */
public class IncrementalCreateVersion {
    /**
     * 基线版本ID
     */
    private static final String baseVersionId = "SVFIUH********CeCViA";

    /**
     * 站点名称
     */
    private static final String siteName = "cnper-cn*********thinkcom";

    /**
     * 云托管站点版本增量zip文件路径
     */
    private static final String filePath = "src/main/resources/static/image.zip";

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
                SiteVersionReq.builder().siteId(site.getSiteId()).baseVersion(baseVersionId).build(), cloudToken);

            errorMsg = "Create version failed. code is " + siteVersionResp.getCode() + " , msg is : "
                + siteVersionResp.getMessage();
            validateCode(siteVersionResp.getCode(), "0", errorMsg);
            SiteVersion siteVersion = siteVersionResp.getSiteVersion();

            // 修改版本
            CodeResp codeResp = AgcCloudgwApi.modifySiteVersion(siteVersion, cloudToken);
            errorMsg = "Assign version failed. code is " + codeResp.getCode() + " , msg is : " + codeResp.getMessage();
            validateCode(codeResp.getCode(), "0", errorMsg);

            // 获取增量文件版本包上传地址并上传版本包zip文件
            int code = AgcCloudgwApi.populateFilesSiteVersion(siteVersionResp.getSiteVersion(), filePath, cloudToken);
            errorMsg = "Fail to upload version packge. code is " + code;
            validateCode(String.valueOf(code), "200", errorMsg);

            // 归档版本
            SiteVersionResp mergeResult = AgcCloudgwApi.mergeSiteVersion(
                SiteVersionBase.builder().siteId(site.getSiteId()).version(siteVersion.getVersion()).build(),
                cloudToken);
            errorMsg = "Finalize version failed. code is " + mergeResult.getCode() + " , msg is : "
                + mergeResult.getMessage();
            validateCode(mergeResult.getCode(), "0", errorMsg);

            // 查询版本状态，需要等待版本状态为： 3 下线状态 时，才可以发布版本，具体时间根据版本文件数量和文件大小而定
            SiteVersion version = queryVersionStatus(
                SiteVersionReq.builder().siteId(site.getSiteId()).version(siteVersion.getVersion()).build(),
                cloudToken);
            if (version.getStatus() != 3) {
                System.out.println("Finalize version failed. version status is " + version.getStatus());
                return;
            }

            // 版本发布 environment--发布环境类型：0 生产 1沙箱
            SiteVersionResp productionResult = AgcCloudgwApi.releaseSiteVersion(SiteVersionReleaseReq.builder()
                .siteId(site.getSiteId())
                .version(siteVersion.getVersion())
                .environment(1)
                .build(), cloudToken);
            errorMsg = "Finalize version failed. code is " + productionResult.getCode() + " , msg is "
                + productionResult.getMessage();
            validateCode(productionResult.getCode(), "0", errorMsg);

            // 异步发布,需要等到发布成功
            SiteVersion releaseVersion = queryVersionStatus(
                SiteVersionReq.builder().siteId(site.getSiteId()).version(siteVersion.getVersion()).build(),
                cloudToken);
            errorMsg = "The version sandbox is released failed.";
            validateCode(String.valueOf(releaseVersion.getStatus()), "11", errorMsg);
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
        // 根据版本包大小和文件数量决定需要多长时间
        Thread.sleep(600000);
        SiteVersionQueryResp siteVersionQueryResp = AgcCloudgwApi.querySiteVersion(siteVersionReq, cloudToken);
        SiteVersion version = CollectionUtils.extractSingleton(siteVersionQueryResp.getSiteVersionList());
        return version;
    }
}
