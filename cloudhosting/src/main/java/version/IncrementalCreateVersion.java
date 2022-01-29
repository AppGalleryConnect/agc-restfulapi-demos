
package version;

import org.apache.commons.lang3.StringUtils;

import version.model.Site;
import version.model.SiteVersion;
import version.model.request.SiteVersionBase;
import version.model.request.SiteVersionReleaseReq;
import version.model.request.SiteVersionReq;
import version.model.response.SiteVersionResp;

/**
 * AGC云托管-云测接口API
 * 【通过api客户端-基于基线版本，增量创建新版本步骤】
 * 查询站点
 * 1. 创建版本
 * 2. 修改版本
 * 3. 合并版本
 * 4. 上传压缩包
 * 5. 归档版本
 * 6.1 沙箱发布
 * 6.2 生产发布
 */
public class IncrementalCreateVersion {
    /**
     * 基线版本ID
     */
    private static final String baseVersionId = "G7W7n********vHZyA";

    /**
     * 站点名称,云托管--站点管理--站点名称
     */
    private static final String siteName = "test";

    /**
     * 云托管站点版本增量zip文件路径
     */
    private static final String filePath = "src/main/resources/static/image.zip";

    public static void main(String[] args) {
        try {
            // 云测网关头部token
            String cloudToken = AgcCloudgwApi.getCloudToken();
            if (StringUtils.isBlank(cloudToken)) {
                System.out.println("Failed to obtain the token.");
                return;
            }
            // 查询版本
            Site site = AgcCloudgwApi.querySiteByName(siteName, cloudToken);
            // 基于基线版本创建新版本
            SiteVersionResp siteVersionResp = AgcCloudgwApi.createHostingSiteVersion(
                SiteVersionReq.builder().siteId(site.getSiteId()).baseVersion(baseVersionId).build(), cloudToken);
            SiteVersion siteVersion = siteVersionResp.getSiteVersion();

            // 修改版本
            AgcCloudgwApi.modifySiteVersion(siteVersion, cloudToken);

            // 获取增量文件版本包上传地址并上传版本包zip文件
            AgcCloudgwApi.populateFilesSiteVersion(siteVersionResp.getSiteVersion(), filePath, cloudToken);

            // 归档版本
            AgcCloudgwApi.mergeSiteVersion(
                SiteVersionBase.builder().siteId(site.getSiteId()).version(siteVersion.getVersion()).build(),
                cloudToken);

            // 版本发布 environment--发布环境类型：0 生产 1沙箱
            SiteVersionResp sanboxRelease = AgcCloudgwApi.releaseSiteVersion(SiteVersionReleaseReq.builder()
                .siteId(site.getSiteId())
                .version(siteVersion.getVersion())
                .environment(1)
                .build(), cloudToken);

            // 等待发布成功后，查询版本发布状态为 SANDBOX_RELEASED;
            String sandboxAccessUrl =
                AgcCloudgwApi.valiedPublishStatus(sanboxRelease.getSiteVersion(), "SANDBOX_RELEASED", cloudToken);
            System.out.println(
                "The sandbox is successfully released and the sandbox accesses the URL is : " + sandboxAccessUrl);
            // 沙箱测试没问题后，AgcCloudgwApi.releaseSiteVersion 进行生产上线

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
