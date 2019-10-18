package com.raccoons.lib.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public class RaccoonsHttpClient {

    public static final Map<String, String> EMPTY = new HashMap<>();

    private HttpClient httpClient;
    private PoolingHttpClientConnectionManager connectionManager;
    private Executor executor;

    public RaccoonsHttpClient() {
        this.connectionManager = new PoolingHttpClientConnectionManager();
        this.httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    public RaccoonsHttpClient(Executor executor) {
        this.connectionManager = new PoolingHttpClientConnectionManager();
        this.httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        this.executor = executor;
    }

    /**
     * GET requests
     */
    public CompletableFuture<RaccoonsHttpResponse> get(String url, Map<String, String> parameters) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final URI uri = new URIBuilder(url) {{
                    if (parameters != null) {
                        for (Map.Entry<String, String> entry : parameters.entrySet()) {
                            addParameter(entry.getKey(), entry.getValue());
                        }
                    }
                }}.build();
                final HttpGet httpGet = new HttpGet(uri);

                return new RaccoonsHttpResponse() {{
                    setHttpResponse(httpClient.execute(httpGet));
                }};
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }, executor != null ? executor : ForkJoinPool.commonPool());
    }

    public CompletableFuture<RaccoonsHttpResponse> get(String url, Map<String, String> headers, Map<String, String> parameters) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final URI uri = new URIBuilder(url) {{
                    if (parameters != null) {
                        for (Map.Entry<String, String> entry : parameters.entrySet()) {
                            setParameter(entry.getKey(), entry.getValue());
                        }
                    }
                }}.build();

                final HttpGet httpGet = new HttpGet(uri);
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        httpGet.addHeader(entry.getKey(), entry.getValue());
                    }
                }
                return new RaccoonsHttpResponse() {{
                    setHttpResponse(httpClient.execute(httpGet));
                }};
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }, executor != null ? executor : ForkJoinPool.commonPool());
    }

    /**
     * POST requests
     */
    public CompletableFuture<RaccoonsHttpResponse> post(String url, Map<String, String> headers, Map<String, String> parameters) {
        return CompletableFuture.supplyAsync(() -> {
            final HttpPost httpPost = new HttpPost(url);
            try {
                if (parameters != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(new ArrayList<NameValuePair>() {{
                        for (Map.Entry<String, String> entry : parameters.entrySet()) {
                            add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                        }
                    }}));
                }
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        httpPost.addHeader(entry.getKey(), entry.getValue());
                    }
                }
                return new RaccoonsHttpResponse() {{
                    setHttpResponse(httpClient.execute(httpPost));
                }};
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executor != null ? executor : ForkJoinPool.commonPool());
    }

    public CompletableFuture<RaccoonsHttpResponse> postJson(String url, Map<String, String> headers, String jsonString) {
        return CompletableFuture.supplyAsync(() -> {
            final HttpPost httpPost = new HttpPost(url);
            try {
                httpPost.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        httpPost.addHeader(entry.getKey(), entry.getValue());
                    }
                }
                return new RaccoonsHttpResponse() {{
                    setHttpResponse(httpClient.execute(httpPost));
                }};
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executor != null ? executor : ForkJoinPool.commonPool());
    }

    public CompletableFuture<RaccoonsHttpResponse> putJson(String url, Map<String, String> headers, String jsonString) {
        return CompletableFuture.supplyAsync(() -> {
            final HttpPut httpPut = new HttpPut(url);
            try {
                httpPut.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        httpPut.addHeader(entry.getKey(), entry.getValue());
                    }
                }
                return new RaccoonsHttpResponse() {{
                    setHttpResponse(httpClient.execute(httpPut));
                }};
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executor != null ? executor : ForkJoinPool.commonPool());
    }

    public CompletableFuture<RaccoonsHttpResponse> delete(String url, Map<String, String> headers) {
        return CompletableFuture.supplyAsync(() -> {
            final HttpDelete httpDelete = new HttpDelete(url);
            try {
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        httpDelete.addHeader(entry.getKey(), entry.getValue());
                    }
                }
                return new RaccoonsHttpResponse() {{
                    setHttpResponse(httpClient.execute(httpDelete));
                }};
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executor != null ? executor : ForkJoinPool.commonPool());
    }
}

