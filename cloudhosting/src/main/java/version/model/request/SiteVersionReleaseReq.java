package version.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SiteVersionReleaseReq extends SiteVersionBase{

    // 发布环境类型：0生产 1沙箱
    private Integer environment;
}
