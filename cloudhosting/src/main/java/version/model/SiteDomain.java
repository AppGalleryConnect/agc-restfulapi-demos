/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SiteDomain {
    // 站点域名
    private String domain;

    // 站点Id，创建站点不必携带
    private String siteId;

    // 站点名称，创建站点不必携带
    private String siteName;

    // 站点归属的租户Id，创建站点不必携带
    private String tenantId;

    // 域名类型 0- allocation , 1- customize
    private Integer domainType;

    // 域名使用的带宽，创建站点不必携带
    private long bandwidth;

    // 域名创建时间，创建站点不必携带
    private long createDate;

    // 域名修改时间，创建站点不必携带
    private long modifyDate;

    // 证书ID
    private String certId;

    // 域名状态, 0-未配置, 1-配置中, 2-配置成功，3-配置失败
    private Integer domainStatus;

    // 域名TXT记录
    private String txtRecord;

    // 加速域名ID
    private String domainId;

    // 响应字段--完整错误信息入库
    private String errorInfo;

    // 响应字段--前端响应错误码
    private String errorCode;

    // 响应字段--证书过期时间
    private long certExpireTime;

    // 租户注册地
    private String regionCode;

    // 域名类型 0- 网站托管 , 1- 媒体托管, 2- 存储加速
    private Integer businessType;
}
