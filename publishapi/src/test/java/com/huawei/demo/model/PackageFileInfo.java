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

/**
 * 
 * @since 2021-01-13
 */
public class PackageFileInfo {
    private String downloadUrl;

    private String downloadFileName;

    private String sensitivePermissionDesc;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getSensitivePermissionDesc() {
        return sensitivePermissionDesc;
    }

    public void setSensitivePermissionDesc(String sensitivePermissionDesc) {
        this.sensitivePermissionDesc = sensitivePermissionDesc;
    }

    public PackageFileInfo(String downloadUrl, String downloadFileName, String sensitivePermissionDesc) {
        this.downloadUrl = downloadUrl;
        this.downloadFileName = downloadFileName;
        this.sensitivePermissionDesc = sensitivePermissionDesc;
    }

    public PackageFileInfo() {
    }
}