package edu.sustech.cs209a.java2finalprojectdemo;

import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) {
        StackOverflowApi stackOverflowApi = new StackOverflowApi();

        try {
            // 爬取500个具有Java标签的数据
            //JsonObject result = fetchData(stackOverflowApi, "questions", createParams());
            // 为新的方法
            JsonObject result = stackOverflowApi.fetchData("java_questions", createParams(),1).get();
            // 将返回的JSON保存到文件
            saveJsonToFile(result, "output.json");
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveJsonToFile(JsonObject json, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(json.toString());
        }
    }

    private static Map<String, String> createParams() {
        Map<String, String> params = new HashMap<>();
        params.put("tagged", "java");
        params.put("pagesize", "1"); // 请求500个数据
        return params;
    }
}

