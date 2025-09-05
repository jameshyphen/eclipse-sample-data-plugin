package com.hyphen.sampledata.net;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.hyphen.sampledata.core.Issue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public final class IssueClient {
    private static final Type ISSUE_LIST = new TypeToken<List<Issue>>() {}.getType();

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    private final URI baseUri;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
                @Override public void write(JsonWriter out, Instant v) throws IOException {
                    if (v == null) out.nullValue(); else out.value(v.toString());
                }
                @Override public Instant read(JsonReader in) throws IOException {
                    if (in.peek() == JsonToken.NULL) { in.nextNull(); return null; }
                    return Instant.parse(in.nextString());
                }
            })
            .create();

    public IssueClient(URI baseUri) {
        this.baseUri = baseUri;
    }

    public List<Issue> fetch() throws IOException, InterruptedException {
        // baseUri.resolve("/api/issues") => http://localhost:8080/api/issues
        HttpRequest req = HttpRequest.newBuilder(baseUri.resolve("/api/issues"))
                .timeout(Duration.ofSeconds(5))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) {
            String body = res.body() == null ? "" : res.body();
            String snippet = body.substring(0, Math.min(200, body.length()));
            throw new IOException("HTTP " + res.statusCode() + " fetching /api/issues – " + snippet);
        }
		return gson.fromJson(res.body(), ISSUE_LIST);
	}
}
