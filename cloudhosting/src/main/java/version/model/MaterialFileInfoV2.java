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
    // 文件所属的场景ID, size = 1-128
    private String sceneId;

    // 素材文件名, size = 1-256
    private String fileName;

    // 内容ID,素材上传时为可选字段, size = 1-64
    private String contentId;

    // 文件SHA256值,分段上传时为可选字段, size = 64-64
    private String fileSha256;

    @Default
    private Integer contentType = 900;

    // 素材文件源大小, size = 1-400M
    private Long fileSize;

    // 素材访问权限，0- 公开访问
    private Integer fileAccessRight = 0;
}
