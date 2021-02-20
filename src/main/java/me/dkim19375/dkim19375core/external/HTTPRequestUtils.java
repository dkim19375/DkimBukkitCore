package me.dkim19375.dkim19375core.external;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class HTTPRequestUtils {
    private HTTPRequestUtils() {}

    private static final OkHttpClient client = new OkHttpClient();

    public static String sendGET(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        return sendGET(new URL(url), headers, params);
    }

    public static String sendGET(URL url, Map<String, String> headers, Map<String, String> params) throws IOException {
        final HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url.toString())).newBuilder();
        for (Map.Entry<String, String> key : params.entrySet()) {
            urlBuilder.addQueryParameter(key.getKey(), key.getValue());
        }
        final String newUrl = urlBuilder.build().toString();
        final Request.Builder builder = new Request.Builder().url(newUrl).get();

        for (Map.Entry<String, String> key : headers.entrySet()) {
            builder.addHeader(key.getKey(), key.getValue());
        }
        final Response response = client.newCall(builder.build()).execute();
        return response.body() == null ? null : response.body().string();
    }
}
