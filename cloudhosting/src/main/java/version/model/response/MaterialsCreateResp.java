/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialsCreateResp {
    // 素材创建响应
    private List<MaterialResp> materialRsps;

    /**
     * 状态码，参考HTTP错误码的定义
     */
    private String code;

    /**
     * 响应体
     */
    private String message;

}
