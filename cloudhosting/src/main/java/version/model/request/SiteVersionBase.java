package version.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SiteVersionBase {
    // 版本号，全局唯一，自动生成
    private String version;

    // 站点ID
    private String siteId;
}
