package com.hz.platform.master.core.common.huichaopay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * https client
 *
 * @author sunxy 2015年10月30日 上午11:03:58
 * @since 1.0
 */
public class HttpsClient {

    private final String url;
    private final Map<String, String> params = new HashMap<>();
    private HttpResponse response;
    private int statusCode;
    private String result;

    public HttpsClient(String url) {
        this.url = url;
    }

    public HttpsClient(String url, Map<String, String> params) {
        this.url = url;
        this.params.putAll(params);
    }

    public void addParam(String name, String value) {
        params.put(name, value);
    }

    public static void main(String[] args) {
        HttpsClient client = new HttpsClient("http://localhost:8088/user/create2");
        client.addParam("userName", "cs");
        client.addParam("password", "123456");
        client.addParam("age", "21");
        try {
            String res = client.sendHttps(10000);
            System.out.println(res);
            String ress = res.subSequence(8, res.length() - 1).toString();
            JSONArray json = JSONArray.parseArray(ress);
            JSONObject ob = json.getJSONObject(0);
            System.out.println(ob.get("backname").toString().split("\n")[0]);
            System.out.println(ob.get("cardname"));
            System.out.println(ob.get("type"));

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public String sendHttps(int timeout_time) {
        org.apache.http.client.HttpClient httpClient = null;
        try {
            httpClient = mkHttpsClient(timeout_time);
            HttpResponse aResponse = httpClient.execute(getHttpPost());
            setResponse(aResponse);
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            httpClient.getConnectionManager().shutdown();
            throw new RuntimeException("访问【" + this + "】时发生异常：" + e.getMessage(), e);
        }
        if (!isRedirect()) {
            return result;
        } else {
            throw new RuntimeException("需要转发!");
        }
    }
    public InputStream sendHttpsFile(int timeout_time) {
        org.apache.http.client.HttpClient httpClient = null;
        try {
            httpClient = mkHttpsClient(timeout_time);
            HttpResponse aResponse = httpClient.execute(getHttpPost());
            if(aResponse.getEntity()==null){
                return null;
            }
            InputStream inputStream=aResponse.getEntity().getContent();
            httpClient.getConnectionManager().shutdown();
            return inputStream;
        } catch (Exception e) {
            httpClient.getConnectionManager().shutdown();
            throw new RuntimeException("访问【" + this + "】时发生异常：" + e.getMessage(), e);

        }

    }

    private org.apache.http.client.HttpClient mkHttpsClient(int timeout_time) throws KeyManagementException, NoSuchAlgorithmException {
        org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();
        if (url.toLowerCase().startsWith("https")) {
            httpclient = getInstance(httpclient);
        }
        if (timeout_time > 0) {
            httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout_time);
            httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout_time);
        }
        return httpclient;
    }

    private org.apache.http.client.HttpClient getInstance(org.apache.http.client.HttpClient client) throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("https", 443, ssf));
        ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
        return new DefaultHttpClient(mgr, client.getParams());
    }

    private HttpPost getHttpPost() throws UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(urlEncodedFormEntity);
        return httpPost;
    }

    private boolean isRedirect() {
        return statusCode == 301 || statusCode == 302;
    }

    private void setResponse(HttpResponse aResponse) throws IOException {
        this.response = aResponse;
        this.statusCode = response.getStatusLine().getStatusCode();
        this.result = getResult(aResponse);

    }

    private String getResult(HttpResponse aResponse) throws IOException {
        HttpEntity entity = aResponse.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity);
        } else {
            return "error:entity is null!";
        }
    }


}
