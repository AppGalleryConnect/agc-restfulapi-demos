/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package hostingmanager.constants;

public interface Constants {
    /**
     * Storage Site in CN：connect-api.cloud.huawei.com
     * Storage site in SG：connect-api-dra.cloud.huawei.com
     * Storage site in DE：connect-api-dre.cloud.huawei.com
     * Storage site in RU：connect-api-drru.cloud.huawei.com
     */
    String DOMAIN = "https://connect-api.cloud.huawei.com";

    /**
     * clientId,replace by your actual value
     */
    String CLIENT_ID = "726190*******91808";

    /**
     * clientSecret,replace by your actual value
     */
    String CLIENT_SECRET = "91A484AD75B755866**************************A30B4A3BEF9325B53F";

    /**
     * projectId
     */
    String PRODUCT_ID = "258779******930596";

    /**
     * Cloud Hosting Service ID
     */
    String SERVICE = "hosting";

    /**
     * Success code
     */
    String SUCCESS = "0";

    Integer UPLOAD_PACKAGE_CONTENT_TYPE = 902;

    String QUERY_SITE = "/api/cloudhosting/web/v1/site/query";

    String CREATE_VERSION = "/api/cloudhosting/web/v1/version/create";

    String MODIFY_FILE_LIST = "/api/cloudhosting/web/v1/version/modifyFilelist";

    String VERSION_UPLOAD = "/api/cloudhosting/web/v1/version/populateFiles";

    String MERGE_VERSION = "/api/cloudhosting/web/v1/version/merge";

    String RELEASE_PRODUCTION_VERSION = "/api/cloudhosting/web/v1/version/release";

    String VERSION_QUERY = "/api/cloudhosting/web/v1/version/query";
}
