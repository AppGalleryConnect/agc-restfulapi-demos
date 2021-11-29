/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import version.model.Site;
import version.model.request.PageInfo;



@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SiteQueryResp {
    // 站点集合
    private List<Site> sites;

    // 分页信息
    private PageInfo pageInfo;

    // 站点最大版本数
    private int siteVersionMaxCount;
}
