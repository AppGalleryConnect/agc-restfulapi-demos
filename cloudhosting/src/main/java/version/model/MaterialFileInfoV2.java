/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialFileInfoV2 {
    // 文件所属的场景ID
    private String sceneId;

    // 素材文件名
    private String fileName;

    // 文件SHA256值
    private String fileSha256;

    @Default
    private Integer contentType = 900;

    // 素材文件源大小
    private Long fileSize;

    // 素材访问权限
    private Integer fileAccessRight = 0;
}
