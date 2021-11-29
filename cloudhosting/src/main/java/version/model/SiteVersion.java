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
public class SiteVersion {
    // 版本号，全局唯一，自动生成
    private String version;

    // 基线版本，默认为当前版本
    private String baseVersion;

    // 站点ID
    private String siteId;

    // 租户ID
    private String tenantId;

    // 素材ID
    private String materialId;

    // 版本包元数据素材ID
    private String metadataMaterialId;

    // 版本描述
    private String description;

    // cleanSuffix访问规则 0--关闭  1--打开
    private Integer cleanSuffix;

    // 响应字段，版本状态：0上线中 1已上线 2下线中 3已下线 4已删除 5错误 6已创建
    private Integer status;

    // 响应字段
    private String config;

    // 响应字段
    private long fileCount;

    // 响应字段
    private long zipFileSize;

    // 响应字段
    private long sourceSize;

    // 响应字段
    private Integer environment;

    // 响应字段
    private long createDate;

    // 响应字段
    private long modifyDate;

    // 响应字段
    private Long expireDate;

    // 响应字段
    private String errorInfo;

    // 响应字段
    private String errorCode;

    // 响应字段 沙箱版本访问前缀
    private String sandboxUrlPrefix;
}
