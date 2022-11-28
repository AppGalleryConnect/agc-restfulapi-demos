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

import java.util.Map;

/**
 * 
 * @since 2021-01-14
 */
public class GetMultipartPartsRsp {
    private String ret;

    private Map<String, FilePartUploadInfo> uploadInfoMap;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public Map<String, FilePartUploadInfo> getUploadInfoMap() {
        return uploadInfoMap;
    }

    public void setUploadInfoMap(Map<String, FilePartUploadInfo> uploadInfoMap) {
        this.uploadInfoMap = uploadInfoMap;
    }

    public GetMultipartPartsRsp() {
    }

    public GetMultipartPartsRsp(String ret, Map<String, FilePartUploadInfo> uploadInfoMap) {
        this.ret = ret;
        this.uploadInfoMap = uploadInfoMap;
    }
}