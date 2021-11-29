/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PageInfo {
    // 当前offset
    private int queryIndex;

    // 每页数量

    @Builder.Default
    private int querySize = 1000;

    // 总记录数，请求时不需要携带
    private long totalSize;
}
