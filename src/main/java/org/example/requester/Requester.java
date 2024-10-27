package org.example.requester;

import okhttp3.*;

import java.util.Map;

public class Requester {
    private static final OkHttpClient client = new OkHttpClient();

    private HttpUrl getUrl(String baseUrl, Map<String, String> queryParams) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        queryParams.forEach(urlBuilder::addQueryParameter);
        return urlBuilder.build();
    }

    public String send(String baseUrl, Map<String, String> queryParams, Map<String, String> headers) {

        Request request = new Request.Builder()
                .url(getUrl(baseUrl, queryParams))
                .headers(getHeaders(headers))
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            System.out.printf("[ERR] не смогли получить ответ от %s . Причина: %s \n", baseUrl, e.getMessage());
            return null;
        }
    }

    private Headers getHeaders(Map<String, String> map) {
        Headers.Builder headers = new Headers.Builder();
        map.forEach(headers::add);
        return headers.build();
    }

}
