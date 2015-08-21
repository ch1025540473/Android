package com.wezebra.zebraking.http;

import android.text.TextUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpClientImp
{

    // private static final String TAG = "HttpClient";

    private static final int MAX_CONNECTIONS_PER_ROUTE = 8;
    private static final int MAX_CONNECTION = 16;

    public static final String REFERER = "Referer";
    public static final String TOKEN = "www.wezebra.com";
    public static final int TIMEOUT = 20 * 1000;

    private static final String DEFAULT_CLIENT_VERSION = "com.alex.haiqing";
    private static final String CLIENT_VERSION_HEADER = "User-Agent";
    private final HttpClient mHttpClient;
    private final String mClientVersion;

    /**
     * singleton
     */
    public static HttpClientImp INSTANCE = new HttpClientImp();

    private HttpClientImp()
    {
        mHttpClient = createHttpClient();
        mClientVersion = DEFAULT_CLIENT_VERSION;
    }

    public String postJsonForString(String url, String token, String json)
            throws Exception
    {
        HttpPost httpPost = createJsonHttpPost(url, token, json);

        HttpResponse response = executeHttpRequest(httpPost);

        switch (response.getStatusLine().getStatusCode())
        {
            case 200:
                try
                {
                    return EntityUtils.toString(response.getEntity());
                } catch (ParseException e)
                {
                    throw new Exception(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());
        }
    }


    public String postFileForString(String url, byte[] data) throws Exception
    {
        HttpPost httpPost = createFileHttpPost(url, data);

        HttpResponse response = executeHttpRequest(httpPost);

        switch (response.getStatusLine().getStatusCode())
        {
            case 200:
                try
                {
                    return EntityUtils.toString(response.getEntity());
                } catch (ParseException e)
                {
                    throw new Exception(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());
        }
    }

    public HttpPost createFileHttpPost(String url, byte[] data)
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(REFERER, TOKEN);

        if (data != null)
        {
            MultipartEntity multipartEntity = new MultipartEntity();
            ContentBody fileBody = new ByteArrayBody(data, "android.jpg");
            multipartEntity.addPart("file", fileBody);

            httpPost.setEntity(multipartEntity);
        }
        return httpPost;
    }

    public HttpPost createJsonHttpPost(String url, String token, String json)
    {
        HttpPost httpPost = new HttpPost(url);

        if (!TextUtils.isEmpty(token))
        {
            httpPost.addHeader(REFERER, token);
        }

        try
        {
            if (json != null)
            {
                httpPost.setEntity(new ByteArrayEntity(json.getBytes("UTF8")));
            }
        } catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException(
                    "Unable to encode http parameters.");
        }
        return httpPost;
    }

    /**
     * execute a HttpPost
     *
     * @param url   not null
     * @param token allow null
     * @param list
     * @return String
     */
    public String postForString(String url, String token,
                                List<NameValuePair> list) throws Exception
    {
        HttpPost httpPost = createHttpPost(url, token, list);

        HttpResponse response = executeHttpRequest(httpPost);

        switch (response.getStatusLine().getStatusCode())
        {
            case 200:
                try
                {
                    return EntityUtils.toString(response.getEntity());
                } catch (ParseException e)
                {
                    throw new Exception(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());
        }
    }

    public String putForString(String url, String token,
                               List<NameValuePair> list) throws Exception
    {
        HttpPut httpPut = createHttpPut(url, token, list);

        HttpResponse response = executeHttpRequest(httpPut);

        switch (response.getStatusLine().getStatusCode())
        {
            case 200:
                try
                {
                    return EntityUtils.toString(response.getEntity());
                } catch (ParseException e)
                {
                    throw new Exception(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());
        }
    }

    /**
     * execute a HttpPost
     *
     * @param url   not null
     * @param token allow null
     * @param
     * @return String
     */
    public String postForString(String url, String token,
                                NameValuePair nameValuePair) throws Exception
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(nameValuePair);
        return postForString(url, token, list);
    }

    /**
     * execute a HttpPost
     *
     * @param url   not null
     * @param token allow null
     * @param
     * @return InputStream
     */
    public InputStream postForStream(String url, String token,
                                     List<NameValuePair> list) throws Exception
    {
        HttpPost httpPost = createHttpPost(url, token, list);
        HttpResponse response = executeHttpRequest(httpPost);
        switch (response.getStatusLine().getStatusCode())
        {
            case 200:
                try
                {
                    return response.getEntity().getContent();
                } catch (ParseException e)
                {
                    throw new Exception(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());
        }
    }

    /**
     * execute a HttpPost
     *
     * @param url   not null
     * @param token allow null
     * @param
     * @return InputStream
     */
    public InputStream postForStream(String url, String token,
                                     NameValuePair nameValuePair) throws Exception
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(nameValuePair);
        return postForStream(url, token, list);
    }

    /**
     * execute a HttpGet
     *
     * @param url   not null
     * @param token allow null
     * @return String
     * @throws Exception
     */
    public String getForString(String url, String token) throws Exception
    {
        HttpGet httpGet = createHttpGet(url, token);

        HttpResponse response = executeHttpRequest(httpGet);

        switch (response.getStatusLine().getStatusCode())
        {
            case 200:
                try
                {
                    return EntityUtils.toString(response.getEntity());
                } catch (ParseException e)
                {
                    throw new Exception(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());
        }
    }

    public String getForString(String url, String token,
                               NameValuePair nameValuePair) throws Exception
    {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(nameValuePair);
        return getForString(url, token, nameValuePairs);
    }

    public String getForString(String url, String token,
                               List<NameValuePair> nameValuePairs) throws Exception
    {
        HttpGet httpGet = createHttpGet(url, token, nameValuePairs);

        HttpResponse response = executeHttpRequest(httpGet);

        switch (response.getStatusLine().getStatusCode())
        {
            case 200:
                try
                {
                    return EntityUtils.toString(response.getEntity());
                } catch (ParseException e)
                {
                    throw new Exception(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());
        }
    }

    /**
     * execute a HttpGet
     *
     * @param url   not null
     * @param token allow null
     * @return String
     * @throws Exception
     */
    public InputStream getForStream(String url, String token) throws Exception
    {
        HttpGet httpGet = createHttpGet(url, token);

        HttpResponse response = executeHttpRequest(httpGet);

        switch (response.getStatusLine().getStatusCode())
        {
            case 200:
                try
                {
                    return response.getEntity().getContent();
                } catch (ParseException e)
                {
                    throw new Exception(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());
        }
    }

    /**
     * execute a HttpGet
     *
     * @param url
     * @param nameValuePairs
     * @return
     * @throws Exception
     */
    public InputStream getForStream(String url, String token,
                                    List<NameValuePair> nameValuePairs) throws Exception
    {
        HttpGet httpGet = createHttpGet(url, token, nameValuePairs);

        HttpResponse response = executeHttpRequest(httpGet);

        switch (response.getStatusLine().getStatusCode())
        {
            case 200:
                try
                {
                    return response.getEntity().getContent();
                } catch (ParseException e)
                {
                    throw new Exception(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new Exception(response.getStatusLine().toString());
        }
    }

    /**
     * execute() an httpRequest catching exceptions and returning null instead.
     *
     * @param httpRequest
     * @return
     * @throws IOException
     */
    public HttpResponse executeHttpRequest(HttpRequestBase httpRequest)
            throws IOException
    {
        try
        {
            mHttpClient.getConnectionManager().closeExpiredConnections();
            return mHttpClient.execute(httpRequest);
        } catch (IOException e)
        {
            httpRequest.abort();
            throw e;
        }
    }

    /**
     * create HttpGet
     */
    public HttpGet createHttpGet(String url, String token)
    {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        if (!TextUtils.isEmpty(token))
        {
            httpGet.addHeader(REFERER, token);
        }
        return httpGet;
    }

    /**
     * create HttpGet
     */
    public HttpGet createHttpGet(String url, String token,
                                 List<NameValuePair> nameValuePairs)
    {
        if(nameValuePairs!=null)
        {
            String query = URLEncodedUtils.format(nameValuePairs, HTTP.UTF_8);
            url = url + "?" + query;
        }

        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        if (!TextUtils.isEmpty(token))
        {
            httpGet.addHeader(REFERER, token);
        }
        return httpGet;
    }

    /**
     * create HttpPost
     */
    public HttpPost createHttpPost(String url, String token,
                                   List<NameValuePair> list)
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        if (!TextUtils.isEmpty(token))
        {
            httpPost.addHeader(REFERER, token);
        }
        try
        {
            httpPost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e1)
        {
            throw new IllegalArgumentException(
                    "Unable to encode http parameters.");
        }
        return httpPost;
    }

    public HttpPut createHttpPut(String url, String token,
                                 List<NameValuePair> list)
    {
        HttpPut httpPost = new HttpPut(url);
        httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        if (!TextUtils.isEmpty(token))
        {
            httpPost.addHeader(REFERER, token);
        }
        try
        {
            httpPost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e1)
        {
            throw new IllegalArgumentException(
                    "Unable to encode http parameters.");
        }
        return httpPost;
    }

    /**
     * Create a thread-safe client. This client does not do redirecting, to
     * allow us to capture correct "error" codes.
     *
     * @return HttpClient
     */
    public static final DefaultHttpClient createHttpClient()
    {

        SSLSocketFactory sf = null;
        try
        {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);
            sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e)
        {
            // LogUnit.Log(TAG, "keystore error");
            e.printStackTrace();
        }

        // Sets up the http part of the service.
        final SchemeRegistry supportedSchemes = new SchemeRegistry();

        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        // final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        // supportedSchemes.register(new Scheme("http", sf, 80));
        supportedSchemes.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        supportedSchemes.register(new Scheme("https", sf, 443));

        // Set some client http client parameter defaults.
        final HttpParams httpParams = createHttpParams();
        HttpClientParams.setRedirecting(httpParams, false);

        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                httpParams, supportedSchemes);
        return new DefaultHttpClient(ccm, httpParams);
    }

    /**
     * Create the default HTTP protocol parameters.
     */
    private static final HttpParams createHttpParams()
    {
        final HttpParams params = new BasicHttpParams();

        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, 10 * 1024);

        ConnManagerParams.setTimeout(params, TIMEOUT);
        ConnManagerParams.setMaxTotalConnections(params, MAX_CONNECTION);
        ConnManagerParams.setMaxConnectionsPerRoute(params, CONN_PER_ROUTE);

        return params;
    }

    /**
     * The default maximum number of connections allowed per host
     */
    private static final ConnPerRoute CONN_PER_ROUTE = new ConnPerRoute()
    {

        public int getMaxForRoute(HttpRoute route)
        {
            return MAX_CONNECTIONS_PER_ROUTE;
        }

    };

    /**
     * @param url
     * @param token
     * @param avalabilityData
     * @return
     * @throws Exception
     */
    public String putForString(String url, String token,
                               BasicNameValuePair avalabilityData) throws Exception
    {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(avalabilityData);
        return putForString(url, token, nameValuePairs);
    }

    public static class MySSLSocketFactory extends SSLSocketFactory
    {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException
        {
            super(truststore);

            TrustManager tm = new X509TrustManager()
            {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException
                {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException
                {
                }

                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException
        {
            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException
        {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    public static class GzipDecompressingEntity extends HttpEntityWrapper
    {
        public GzipDecompressingEntity(HttpEntity paramHttpEntity)
        {
            super(paramHttpEntity);
        }

        public InputStream getContent() throws IOException,
                IllegalStateException
        {
            return new GZIPInputStream(this.wrappedEntity.getContent());
        }

        public long getContentLength()
        {
            return -1L;
        }
    }

}