package proxy;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.*;
import java.util.Collection;

/**
 * Created by xuaihua on 2017/2/4.
 */
public class GetTest {

    static class MyConnectionSocketFactory extends SSLConnectionSocketFactory {

        public MyConnectionSocketFactory(final SSLContext sslContext) {
            super(sslContext);
        }

        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            System.err.println("============================================================");
            return new Socket(proxy);
        }

    }

    static class HttpConnectionSocketFactory extends PlainConnectionSocketFactory {

        public HttpConnectionSocketFactory() {
            super();
        }

        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            System.err.println("============================================================");
            return new Socket(proxy);
        }

    }

    public static void main(String [] args) throws Exception{
        CloseableHttpClient httpclient = null;
        try {
            //设置http代理
//            CloseableHttpClient httpclient = HttpClients.createDefault();
//            HttpHost proxy = new HttpHost("115.28.147.89", 9081, "http");
//
//            RequestConfig config = RequestConfig.custom()
//                    .setProxy(proxy)
//                    .build();

            //设置socks代理
            //设置socks账号
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("admin", "admin".toCharArray());
                }
            });

            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("admin", "admin"));

            Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
//                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new MyConnectionSocketFactory(SSLContexts.createSystemDefault()))
                    .register("http", new HttpConnectionSocketFactory())
                    .build();

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg);
            httpclient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setDefaultCredentialsProvider(credsProvider)
                    .setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy(){
                        Collection<String> getPreferredAuthSchemes(RequestConfig config) {
                            System.err.println("--------------------------------------------------------------");
                            return config.getProxyPreferredAuthSchemes();
                        }
                    })
                    .build();

            InetSocketAddress socksaddr = new InetSocketAddress("115.28.147.89", 9081);
            HttpClientContext context = HttpClientContext.create();
            context.setAttribute("socks.address", socksaddr);

            HttpGet httpget = new HttpGet("http://www.baidu.com");
            //设置http代理
//            httpget.setConfig(config);

            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget,context);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            httpclient.close();
        }
    }

}
