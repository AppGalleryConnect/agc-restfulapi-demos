/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteVersionReq {
    // 版本号，全局唯一，自动生成
    private String version;

    // 基线版本号
    private String baseVersion;

    // 站点ID
    private String siteId;

    // 租户ID，以请求头中为准
    private String tenantId;

    // 站点状态：0上线中 1已上线 2下线中 3已下线
    private Integer status;

    private PageInfo pageInfo;
}
