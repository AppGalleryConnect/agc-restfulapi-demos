import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
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

public class CreateUnifiedLinking {

    // replace with your client id
    private static final String YOUR_CLIENT_ID = "625298*****31751552";

    // replace with your client secret
    private static final String YOUR_CLIENT_SECRET = "B17337A39E8*****2FA08768BDF8C6B727F6CE5987988E87FBE6180FA62B43";

    // replace with your Project ID
    private static final String YOUR_PRODUCT_ID = "73643*****44583900";

    // replace with your uri prefix
    private static final String URI_PREFIX = "https://p*****a.drcn.agconnect.link";

    // replace with your default Redirection Url
    private static final String DEFAULT_REDIRECT = "https://developer.huawei.com/consumer/cn/";

    private static final String GATEWAY_URL = "https://connect-api.cloud.huawei.com/api/oauth2/v1/token";

    private static final String CREATE_UNIFIED_URL ="https://connect-api.cloud.huawei.com/api/applinking/v1/unified-links";


    public static void main(String[] args) {
        String token = getToken();
        createUnifiedLinking(token);
    }

    public static void createUnifiedLinking(String token) {
        HttpPost post = new HttpPost(CREATE_UNIFIED_URL);
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("client_id", YOUR_CLIENT_ID);
        post.setHeader("productId", YOUR_PRODUCT_ID);
        //Request Body
        JSONObject keyString = new JSONObject();
        keyString.put("uriPrefix",URI_PREFIX );
        keyString.put("name", "UnifiedLinkingX");
        keyString.put("defaultRedirectionUrl", DEFAULT_REDIRECT);
        //...
        StringEntity entity = new StringEntity(keyString.toString(), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        post.setEntity(entity);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Consts.UTF_8));
                String result = br.readLine();
                JSONObject object = JSON.parseObject(result);
                String shortUrl = object.getString("shortUrl");
                System.out.println("Result：shortUrl is " + shortUrl);
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
