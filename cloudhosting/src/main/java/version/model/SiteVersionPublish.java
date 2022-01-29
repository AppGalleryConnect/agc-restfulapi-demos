/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteVersionPublish {
    // 租户ID
    private String tenantId;

    // 站点ID
    private String siteId;

    // 版本号，全局唯一，自动生成
    private String version;

    // 版本发布环境 0-生产，1-沙箱
    private Integer environment;

    // 版本发布状态
    private String publishStatus;

    // 站点映射Key,生产环境为CDN加速域名,沙箱环境CDN加速域名+沙箱目录
    private String mappingKey;

    // 创建时间
    private long createDate;

    // 修改时间
    private long modifyDate;
}
