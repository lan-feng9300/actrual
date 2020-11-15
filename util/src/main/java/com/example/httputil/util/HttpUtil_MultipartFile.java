package com.example.httputil.util;

import com.example.httputil.result.R;
import com.example.httputil.result.ResponseData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;

/**
 *
 *  文件请求工具类 --> 类似于表单提交
 *
 *  RequestConfig builder时间模式详解: https://blog.csdn.net/a1165117473/article/details/84105301
 *  1.从连接池中获取可用连接超时 setConnectionRequestTimeout(3000)
 *  HttpClient中的要用连接时尝试从连接池中获取，若是在等待了一定的时间后还没有获取到可用连接（比如连接池中没有空闲连接了）则会抛出获取连接超时异常。
 *  2.连接目标超时 connectionTimeout
 *  指的是连接目标url的连接超时时间，即客服端发送请求到与目标url建立起连接的最大时间。如果在该时间范围内还没有建立起连接，则就抛出connectionTimeOut异常。
 *  如测试的时候，将url改为一个不存在的url：“http://test.com” ，超时时间3000ms过后，系统报出异常：    org.apache.commons.httpclient.ConnectTimeoutException:The host did not accept the connection within timeout of 3000 ms
 *  3.等待响应超时（读取数据超时）socketTimeout
 * 连接上一个url后，获取response的返回等待时间 ，即在与目标url建立连接后，等待放回response的最大时间，在规定时间内没有返回响应的话就抛出SocketTimeout。
 *
 */

public class HttpUtil_MultipartFile {

    private static CloseableHttpClient httpClient;

    /**
     *  解决 SSL证书信任问题
     */
    static {
        try {
            SSLContext sslContext = SSLContextBuilder.create().useProtocol(SSLConnectionSocketFactory.SSL).loadTrustMaterial((x, y) -> true).build();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setSSLContext(sslContext).setSSLHostnameVerifier((x,y)->true).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public R sendRequest(String url, String userId, String userName, MultipartFile file) {

        String result = "";
        try {
            String fileName = file.getOriginalFilename();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("utf-8"));
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 加上此行解决中文乱码问题
            // 次提交方式类似于表单提交
            builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);
            builder.addTextBody("userId", userId);
            builder.addTextBody("userName", userName);

            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String response = EntityUtils.toString(httpEntity, Charset.forName("utf-8"));

            //return ResponseData.result(200, "请求成功", response);
            return R.ok().put("result",response);
        } catch (Exception e) {
            e.printStackTrace();
            //return ResponseData.result(500, "网络异常, 请求失败", null);
            return R.error(500,"网络异常,请求失败");
        }
    }
}