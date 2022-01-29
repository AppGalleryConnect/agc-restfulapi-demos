/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Site {
    // 站点Id
    private String siteId;

    // 站点名称
    private String siteName;

    // 站点归属的租户Id
    private String tenantId;

    // 站点大小
    private long totalSize;

    // 站点状态
    private Integer status;

    // 站点当前工作版本号
    private String curVersion;

    // 描述信息
    private String description;

    // 创建时间
    private long createDate;

    // 修改时间
    private long modifyDate;

    // 站点访问规则 临时
    private Integer cleanSuffix;
}
