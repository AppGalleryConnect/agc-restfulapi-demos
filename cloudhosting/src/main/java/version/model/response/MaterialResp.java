/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import version.model.MaterialUploadInfo;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialResp {

    // 素材ID
    private String materialId;

    // 文件ID
    private String fileId;


    // [响应属性]文件上传信息
    private MaterialUploadInfo uploadInfo;
}
