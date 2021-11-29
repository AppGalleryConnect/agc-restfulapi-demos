/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import version.model.Site;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteQueryReq {
    // 站点信息
    private Site site;

    private PageInfo pageInfo;
}
