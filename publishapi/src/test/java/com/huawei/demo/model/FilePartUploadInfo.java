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
 * 
 * @since 2021-01-14
 */
public class FilePartUploadInfo {
    private String url;
    private String method;
    private String partObjectId;
    private Map<String,String> headers;

    public FilePartUploadInfo() {
    }

    public FilePartUploadInfo(String url, String method, String partObjectId, Map<String, String> headers) {
        this.url = url;
        this.method = method;
        this.partObjectId = partObjectId;
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPartObjectId() {
        return partObjectId;
    }

    public void setPartObjectId(String partObjectId) {
        this.partObjectId = partObjectId;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}