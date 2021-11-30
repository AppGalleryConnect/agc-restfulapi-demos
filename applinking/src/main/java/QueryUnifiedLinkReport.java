import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class QueryUnifiedLinkReport {

    // replace with your client id
    private static final String YOUR_CLIENT_ID = "62529*****31751552";

    // replace with your client secret
    private static final String YOUR_CLIENT_SECRET = "B17337A39E83AA*****A08768BDF8C6B727F6CE5987988E87FBE6180FA62B43";

    // replace with your Project ID
    private static final String YOUR_PRODUCT_ID = "736430*****4583900";

    // replace with your unified linking
    private static final String YOUR_UNIFIED_LINKING= "https://p*****a.drcn.agconnect.link/u*****x";

    private static final String GATEWAY_URL = "https://connect-api.cloud.huawei.com/api/oauth2/v1/token";

    private static final String UNIFIED_REPORT_LINK = "https://connect-api.cloud.huawei.com/api/applinking/v1/unified-links-report";


    public static void main(String[] args) {
        String token = getToken();
        queryUnifiedLinkReport(token);
    }

    public static void queryUnifiedLinkReport(String token) {
        HttpGet get = new HttpGet(UNIFIED_REPORT_LINK + "?shortUrl=" + YOUR_UNIFIED_LINKING);
        get.setHeader("Authorization", "Bearer " + token);
        get.setHeader("client_id", YOUR_CLIENT_ID);
        get.setHeader("productId", YOUR_PRODUCT_ID);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(get);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();
                JSONObject object = JSON.parseObject(result);
                System.out.println("queryResult: " + object);
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }

    public static String getToken() {
        String token = null;
        try {
            HttpPost post = new HttpPost(GATEWAY_URL);
            JSONObject keyString = new JSONObject();
            keyString.put("client_id", YOUR_CLIENT_ID);
            keyString.put("client_secret", YOUR_CLIENT_SECRET);
            keyString.put("grant_type", "client_credentials");

            StringEntity entity = new StringEntity(keyString.toString(), Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            post.setEntity(entity);

            HttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();
                JSONObject object = JSON.parseObject(result);
                token = object.getString("access_token");
            }
            post.releaseConnection();
            httpClient.close();
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        return token;
    }



    private static CloseableHttpClient httpClient;

    /**
     * 信任SSL证书
     */
    static {
        try {
            SSLContext sslContext = SSLContextBuilder.create().useProtocol(SSLConnectionSocketFactory.SSL).loadTrustMaterial((x, y) -> true).build();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setSSLContext(sslContext).setSSLHostnameVerifier((x, y) -> true).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
