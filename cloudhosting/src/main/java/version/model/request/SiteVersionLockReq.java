package version.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import version.model.MaterialFileInfoV2;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteVersionLockReq {
    // 版本号
    private String version;

    // 站点ID
    private String siteId;

    // 版本包上传信息
    private List<MaterialFileInfoV2> materialInfos;
}
