package version;

import org.apache.commons.lang3.StringUtils;

import version.model.response.SiteVersionResp;

/**
 *  【通过api客户端-删除版本】
 *  1. 删除版本
 */
public class DeleteHostingVersion {
    public static void main(String[] args) {
        try {
            // 只能删除非上线状态版本
            String cloudToken = AgcCloudgwApi.getCloudToken();
            SiteVersionResp siteVersionResp = AgcCloudgwApi.deleteHostingVersion("hRK8fbQoSeWZhbChpn7vtA", cloudToken);
            if (StringUtils.equals(siteVersionResp.getCode(),"0")) {
                System.out.println("Hosting version is deleted successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
