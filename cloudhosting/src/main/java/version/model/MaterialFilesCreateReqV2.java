/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package version.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialFilesCreateReqV2 {

    // 上传文件的User-Agent,不填为服务默认值,一般用于通过页面上传文件, size = 1-1024
    private String userAgent;

    // 是否为分段上传.默认为false,表示不是分段上传
    @Default
    private Boolean multipartFlag = false;

    // 是否需要加速上传.默认为false,表示不需要
    @Default
    private Boolean useAccelerate = false;

    // 是否使用业务传的文件名 true(默认) - 使用业务传的文件名,不支持中文; false - 随机生成文件名,业务不能根据CM返回的素材ID,FileID和URL中的文件名做业务逻辑
    @Default
    private Boolean useAppFileName = true;

    // 素材创建信息,最大支持500个素材创建
    @Singular
    private List<MaterialFileInfoV2> materialInfos;

}
