/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import version.model.SiteVersion;
import version.model.request.PageInfo;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteVersionQueryResp {
    private List<SiteVersion> siteVersionList;

    private PageInfo pageInfo;
}
