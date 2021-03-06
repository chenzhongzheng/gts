package com.wf.gts.common.utils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import com.google.gson.Gson;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpTools {
  
  

  private static final OkHttpTools OK_HTTP_TOOLS = new OkHttpTools();

  public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

  private OkHttpTools() {

  }

  public static OkHttpTools getInstance() {
      return OK_HTTP_TOOLS;
  }


  private Gson gson = new Gson();

  public Request buildPost(String url, Map<String, String> params) {

      MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

      FormBody.Builder formBuilder = new FormBody.Builder();
      if (params != null) {
          for (String key : params.keySet()) {
              formBuilder.add(key, params.get(key));
          }
      }

      return new Request.Builder()
              .url(url)
              .addHeader("Content-Type", "application/json; charset=utf-8")
              .post(formBuilder.build())
              .build();
  }


  public String post(String url,String json) throws IOException {
      OkHttpClient client = new OkHttpClient();
      RequestBody body = RequestBody.create(JSON, json);
      Request request = new Request.Builder()
              .url(url)
              .post(body)
              .build();
      Response response = client.newCall(request).execute();
      return response.body().string();

  }

  public <T> T get(String url, Map<String, String> params, Class<T> classOfT) throws IOException {
      return execute(buildPost(url, params), classOfT);
  }


  public <T> T get(String url, Type type) throws IOException {
      return execute(buildPost(url, null), type);
  }

  private <T> T execute(Request request, Class<T> classOfT) throws IOException {
      OkHttpClient client = new OkHttpClient();
      Response response = client.newCall(request).execute();
      return gson.fromJson(response.body().string(), classOfT);
  }

  private String execute(Request request) throws IOException {
      OkHttpClient client = new OkHttpClient();
      Response response = client.newCall(request).execute();
      return response.body().string();
  }

  public <T> T execute(Request request, Type type) throws IOException {
      OkHttpClient client = new OkHttpClient();
      Response response = client.newCall(request).execute();
      return gson.fromJson(response.body().string(), type);
  }

}
