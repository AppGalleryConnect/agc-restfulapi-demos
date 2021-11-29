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

    // 站点大小：\n" + "1、包含站点内所有版本解压缩后文件的大小之和；\n" + "2、创建版本、删除版本涉及totalSize的修改，禁用站点、恢复站点、回滚版本不涉及totalSize修改
    private long totalSize;

    // 总操作次数
    private int optCount;

    // 站点状态：1：online，上线；2：offline，禁用；3：configuring，配置中；4：onlining，上线中；5：error，错误；6：deregister，注销
    private Integer status;

    // 站点当前工作版本号
    private String curVersion;

    // 描述信息
    private String description;

    // 创建时间
    private long createDate;

    // 修改时间
    private long modifyDate;

    // 站点下域名信息
    private List<SiteDomain> siteDomains;

    // 版本总数量
    private int versionCount = 0;

    // 请求参数：站点域名
    private String siteDomain;

    // 加速域名ID
    private String domainId;

    // 站点访问规则 临时
    private Integer cleanSuffix;

    // 域名类型 0- 网站托管 , 1- 媒体托管, 2- 存储加速
    private Integer businessType;

    // storage桶名
    private String bucket;
}
