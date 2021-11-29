/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import version.model.SiteVersion;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SiteVersionResp extends CodeResp{
    /**
     * 版本信息
     */
    private SiteVersion siteVersion;
}
