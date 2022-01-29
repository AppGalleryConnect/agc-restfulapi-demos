/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteVersion {
    // 版本号
    private String version;

    // 基线版本
    private String baseVersion;

    // 站点ID
    private String siteId;

    // 版本文件数量
    private Integer fileCount;

    // 版本压缩包大小
    private Integer zipFileSize;

    // 版本解压后大小
    private Integer sourceSize;

    // 租户ID
    private String tenantId;

    // 版本描述
    private String description;

    // 版本状态
    private String versionStatus;

    // 版本发布信息
    private List<SiteVersionPublish> siteVersionPublishList;

    // 沙箱发布访问前缀
    private String sandboxUrlPrefix;

    // 创建时间
    private long createDate;

    // 修改时间
    private long modifyDate;
}
