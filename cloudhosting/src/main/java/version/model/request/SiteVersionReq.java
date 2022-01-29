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
    // 版本号
    private String version;

    // 基线版本号
    private String baseVersion;

    // 站点ID
    private String siteId;

    // 租户ID
    private String tenantId;

    // 站点状态
    private Integer status;

    private PageInfo pageInfo;
}
