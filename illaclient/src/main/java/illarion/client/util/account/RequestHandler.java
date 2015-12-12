/*
 * This file is part of the Illarion project.
 *
 * Copyright © 2015 - Illarion e.V.
 *
 * Illarion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Illarion is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package illarion.client.util.account;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import illarion.client.util.Lang;
import illarion.common.data.IllarionSSLSocketFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Period;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

/**
 * This class is sending out requests and handling the responses.
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
class RequestHandler {
    @Nonnull
    private final String rootUrl;

    @Nonnull
    private final ExecutorService executorService;

    @Nonnull
    private final Gson gson;

    @Nonnull
    private final Charset utf8;

    RequestHandler(@Nonnull String rootUrl) {
        this.rootUrl = rootUrl;
        executorService = Executors.newCachedThreadPool(new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("AccountSystem-REST-Thread %d")
                .build());

        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(Period.class, new PeriodTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();

        utf8 = Charset.forName("UTF-8");
    }

    @Nonnull
    private static String buildRequestUrl(@Nonnull String root, @Nonnull String route) {
        String fixedRoot;
        if (root.endsWith("/")) {
            fixedRoot = root.substring(0, root.length() - 1);
        } else {
            fixedRoot = root;
        }

        String fullRoute;
        if (route.startsWith("/")) {
            fullRoute = fixedRoot + route;
        } else {
            fullRoute = fixedRoot + '/' + route;
        }
        fullRoute += ".json";
        return fullRoute;
    }

    @Nonnull
    public <T> Future<T> sendRequestAsync(@Nonnull Request<T> request) {
        return executorService.submit(() -> sendRequest(request));
    }

    @Nullable
    public <T> T sendRequest(@Nonnull Request<T> request) throws IOException {
        if (request instanceof AuthenticatedRequest) {
            java.net.Authenticator.setDefault(((AuthenticatedRequest) request).getAuthenticator());
        }
        URL requestUrl = new URL(buildRequestUrl(rootUrl, request.getRoute()));
        URLConnection urlConnection = requestUrl.openConnection();
        if (!(urlConnection instanceof HttpURLConnection)) {
            throw new IllegalStateException("Request did not create the expected http connection.");
        }
        HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

        if (httpConnection instanceof HttpsURLConnection) {
            SSLSocketFactory factory = IllarionSSLSocketFactory.getFactory();
            if (factory != null) {
                ((HttpsURLConnection) urlConnection).setSSLSocketFactory(factory);
            }
        }
        httpConnection.setRequestMethod(request.getMethod());
        httpConnection.setRequestProperty("Accept", "application/json");
        httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        httpConnection.setRequestProperty("Accept-Language", Lang.getInstance().isGerman() ? "de-DE,de" : "en-US,en-GB,en");

        Object payload = request.getData();
        if (payload != null) {
            httpConnection.setDoOutput(true);
            try (JsonWriter wr = new JsonWriter(new OutputStreamWriter(httpConnection.getOutputStream(), utf8))) {
                gson.toJson(payload, payload.getClass(), wr);
                wr.flush();
            }
        }

        int response = httpConnection.getResponseCode();
        Class<T> responseClass = request.getResponseMap().get(response);
        if (responseClass == null) {
            return null;
        }

        try (InputStream in = httpConnection.getInputStream()) {
            InputStream usedIn = in;
            String contentEncoding = httpConnection.getHeaderField("Content-Encoding");
            if (contentEncoding != null) {
                switch (contentEncoding) {
                    case "gzip":
                        //noinspection IOResourceOpenedButNotSafelyClosed,resource
                        usedIn = new GZIPInputStream(usedIn);
                        break;
                    case "deflate":
                        //noinspection IOResourceOpenedButNotSafelyClosed,resource
                        usedIn = new DeflaterInputStream(usedIn);
                        break;
                    default:
                        break;
                }
            }

            try (JsonReader rd = new JsonReader(new InputStreamReader(usedIn, utf8))) {
                return gson.fromJson(rd, responseClass);
            }
        }
    }
}