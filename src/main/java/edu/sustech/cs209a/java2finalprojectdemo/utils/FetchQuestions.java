//package edu.sustech.cs209a.java2finalprojectdemo.utils;
//
//import com.google.gson.JsonObject;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
//public class FetchQuestions {
//
//    public static void main(String[] args) {
//        StackOverflowApi stackOverflowApi = new StackOverflowApi();
//
//        try {
//            // 爬取600个具有Java标签的数据 由于page限制分批保存
//            for (int i = 1; i <= 6; i++) {
//                JsonObject result = stackOverflowApi.fetchData("java_questions", createParams(i)).get();
//                // 将返回的JSON保存到文件
//                saveJsonToFile(result, "question\\java_question_100_" +  i + ".json");
//            }
//        } catch (InterruptedException | ExecutionException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void saveJsonToFile(JsonObject json, String filePath) throws IOException {
//        try (FileWriter fileWriter = new FileWriter(filePath)) {
//            fileWriter.write(json.toString());
//        }
//    }
//
//    private static Map<String, String> createParams(int i) {
//        Map<String, String> params = new HashMap<>();
//        params.put("page", String.valueOf(i));
//        params.put("tagged", "java");
//        params.put("pagesize", "100");  // 设置爬取量
//        return params;
//    }
//}
//
