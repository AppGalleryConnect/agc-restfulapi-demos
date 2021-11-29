/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialUploadInfo {

    // OBS上传地址
    private String url;

    // OBS上传请求方式
    private String method;

    // OBS上传携带请求头
    private Map<String, String> headers;
}
