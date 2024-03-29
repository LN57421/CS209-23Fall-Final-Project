package edu.sustech.cs209a.java2finalprojectdemo.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import okhttp3.*;
import com.google.gson.*;

/**
 * @author LN This class is used to fetch data from Stack Overflow API.
 */
public class StackOverflowApi {

    private static final String API_BASE_URL = "https://api.stackexchange.com/2.3";
    private static final String API_KEY = "W4sBb4xkACVJtdhHVniS3Q((";
    private static final String CLIENT_ID = "27915";



    public CompletableFuture<JsonObject> fetchData(String op, Map<String, String> params) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        CompletableFuture<JsonObject> future = new CompletableFuture<>();

        HttpUrl url = null;
        String urlPath = null;

        // 构建请求 URL
        switch (op) {
            case "questions":
                if (params.containsKey("ids")) {
                    int questionId = Integer.parseInt(params.get("ids"));
                    params.remove("ids"); // 删除params中的ids参数
                    urlPath = String.format("/questions/%d", questionId); // 将id插入到URL路径中
                } else {
                    urlPath = "/questions";
                }
                url = HttpUrl.parse(API_BASE_URL + urlPath)
                        .newBuilder()
                        .addQueryParameter("site", "stackoverflow")
                        .addQueryParameter("key", API_KEY)
                        .addQueryParameter("filter", "withbody")
                        .build();
                break;

//            case "info":
//                url = HttpUrl.parse(API_BASE_URL + "/info")
//                        .newBuilder()
//                        .addQueryParameter("site", "stackoverflow")
//                        .addQueryParameter("key", API_KEY)
//                        .build();
//                break;

            case "answer_question":
                url = HttpUrl.parse(API_BASE_URL + "/questions/" + params.get("ids") + "/answers")
                        .newBuilder()
                        .addQueryParameter("site", "stackoverflow")
                        .addQueryParameter("key", API_KEY)
                        .addQueryParameter("filter", "withbody")
                        .build();
                break;

            case "comment_question":
                url = HttpUrl.parse(API_BASE_URL + "/questions/" + params.get("ids") + "/comments")
                        .newBuilder()
                        .addQueryParameter("site", "stackoverflow")
                        .addQueryParameter("key", API_KEY)
                        .addQueryParameter("filter", "withbody")
                        .build();
                break;

//            case "java_questions":
//                urlPath = "/questions";
//                url = HttpUrl.parse(API_BASE_URL + urlPath)
//                        .newBuilder()
//                        .addQueryParameter("site", "stackoverflow")
//                        .addQueryParameter("key", API_KEY)
//                        .build();
//                break;

            default:
                throw new IllegalArgumentException("Invalid operation: " + op);
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            url = url.newBuilder().addQueryParameter(entry.getKey(), entry.getValue()).build();
        }

        // 创建请求
        Request request = new Request.Builder().url(url).build();
        // 发送请求并处理响应
        HttpUrl finalUrl = url;

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String jsonData = response.body().string();
                    // 使用 Gson 解析 JSON 数据
                    JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
                    System.out.println(finalUrl);
                    System.out.println(jsonObject);
                    future.complete(jsonObject);
                }
            }
        });

        return future;
    }


}

