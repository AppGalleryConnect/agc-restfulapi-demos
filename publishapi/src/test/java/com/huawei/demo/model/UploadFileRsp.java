/*
 * Copyright 2019. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


package com.huawei.demo.model;

import java.util.List;

/**
 * Function description.
 *
 *
 * @since 2019-10-24
 */
public class UploadFileRsp {
    public String getIfSuccess() {
        return ifSuccess;
    }

    public void setIfSuccess(String ifSuccess) {
        this.ifSuccess = ifSuccess;
    }

    String ifSuccess;

    public List<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }

    List<FileInfo> fileInfoList;
}
