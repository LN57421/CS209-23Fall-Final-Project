package edu.sustech.cs209a.java2finalprojectdemo.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author ln
 */

// https://api.stackexchange.com/2.3/questions/3757396?site=stackoverflow&filter=withbody

// https://api.stackexchange.com/2.3/questions?page=1&pagesize=1&fromdate=1672531200&todate=1701388800&order=desc&sort=activity&tagged=java&site=stackoverflow&filter=!9MyMg2q4M.IhkHGnmFKa3xqNMdX)5ZKbrlzn8GdMwwBDKb6BWXZRlcH
public class FetchAllData {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/cs209a";
    private static final String USER = "root";
    private static final String PASS = "NIANljb123";

    public static void main(String[] args) throws SQLException {
//        // 控制爬取数量 Question表插入Question
//        for (int i = 1; i <= 5; i++) {
//            addQuestion(i);
//        }

         // 更新questions表 comments表
         addBodies();
    }
    /**
     *   重写filter查询
     *
     *
     */
    static void add(){
        // 通过filter，向API发送请求


    }


    /**
     *   查询Question
     */
    static void addQuestion(int page) {
        // 使用StackOverflowApi类的fetchData方法，向StackExchange API发送请求
        StackOverflowApi api = new StackOverflowApi();
        Map<String, String> params = new HashMap<>();
        // 数量上限100
        params.put("pagesize", "100");
        params.put("page", String.valueOf(page));
        params.put("tagged", "java");

        // 获得返回的JsonObject
        CompletableFuture<JsonObject> future = api.fetchData("questions", params);

        future.thenAcceptAsync(jsonObject -> {
            JsonArray items = jsonObject.getAsJsonArray("items");
            List<CompletableFuture<Void>> tasks = new ArrayList<>();

            for (int i = 0; i < items.size(); i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                tasks.add(CompletableFuture.runAsync(() -> insertQuestionIntoDatabase(item))
                );
            }
            CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[0])).join();
        });
    }

    /**
     *   将Question和tag插入表中
     */

    private static void insertQuestionIntoDatabase(JsonObject jsonObject) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

            String sql_insertQuestion = "INSERT INTO questions (question_id, is_answered, creation_date, score, view_count, title, body) VALUES (?, ?, ?, ?, ?, ?, ?)";
            String sql_insertTags = "INSERT INTO tags (name, score, view_count, question_id) VALUES (?, ?, ?, ?)";

            // 将信息填入sql语句
            PreparedStatement pstmt_in_Ques = conn.prepareStatement(sql_insertQuestion);
            pstmt_in_Ques.setInt(1, jsonObject.get("question_id").getAsInt());
            boolean is_answered = jsonObject.get("is_answered").getAsBoolean();
            pstmt_in_Ques.setBoolean(2, is_answered);
            Date date = new Date(jsonObject.get("creation_date").getAsLong() * 1000);
            Timestamp timestamp = new Timestamp(date.getTime());
            pstmt_in_Ques.setTimestamp(3, timestamp);

            pstmt_in_Ques.setInt(4, jsonObject.get("score").getAsInt());
            pstmt_in_Ques.setInt(5, jsonObject.get("view_count").getAsInt());

            // 获取问题的标签
            JsonArray tags = jsonObject.get("tags").getAsJsonArray();


            // 获取问题的标题
            String title = jsonObject.get("title").getAsString();
            pstmt_in_Ques.setString(6, title);

            // 获取问题的body
            String body = jsonObject.get("body").getAsString();
            pstmt_in_Ques.setString(7, body);

            pstmt_in_Ques.executeUpdate();
            System.out.println("Question record inserted successfully");

            // 插tags

            // Insert tags
            PreparedStatement pstmt_in_Tags = conn.prepareStatement(sql_insertTags);
            System.out.println(tags.asList().size());
            for (JsonElement tagElement : tags) {
                String tagName = tagElement.getAsString();
                //System.out.println(tagName);

                pstmt_in_Tags.setString(1, tagName);
                pstmt_in_Tags.setInt(2, jsonObject.get("score").getAsInt());
                pstmt_in_Tags.setInt(3, jsonObject.get("view_count").getAsInt());
                pstmt_in_Tags.setInt(4, jsonObject.get("question_id").getAsInt());

                pstmt_in_Tags.executeUpdate();
            }
            System.out.println("Tags inserted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     *  查询并插入answer和comment
     */

    private static void addBodies() {
        List<Integer> questionIds = getAllQuestionIds();

        // https://api.stackexchange.com/2.3/answers/77642161?site=stackoverflow&filter=withbody

        String sql1 = "INSERT INTO answers (answer_id, question_id, body, score) VALUES (?, ?, ?, ?) ";
        String sql2 = "INSERT INTO comments (comment_id, question_id, body, score) VALUES (?, ?, ?, ?) ";

        try (
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                PreparedStatement pstmt2 = conn.prepareStatement(sql2);
        ) {
            StackOverflowApi api = new StackOverflowApi();
            for (Integer questionId : questionIds) {

                Map<String, String> params = new HashMap<>();
                params.put("ids", questionId.toString());
                params.put("pagesize", "1");
                CompletableFuture<JsonObject> future = api.fetchData("answer_question", params);

                JsonObject response = future.get(); // Blocking call to get the result

                if (response.has("items")) {
                    JsonArray items = response.getAsJsonArray("items");
                    for (JsonElement item : items) {
                        JsonObject answer = item.getAsJsonObject();

                        int answerId = answer.get("answer_id").getAsInt();
                        String body = answer.get("body").getAsString();
                        int score = answer.get("score").getAsInt();

                        // Insert into answers table
                        pstmt1.setInt(1, answerId);
                        pstmt1.setInt(2, questionId);
                        pstmt1.setString(3, body);
                        pstmt1.setInt(4, score);
                        pstmt1.executeUpdate();

                        System.out.println("Answer with ID " + answerId + " inserted successfully.");
                    }
                }
            }

            for (Integer questionId : questionIds) {

                Map<String, String> params = new HashMap<>();
                params.put("ids", questionId.toString());
                params.put("pagesize", "1");
                CompletableFuture<JsonObject> future = api.fetchData("comment_question", params);

                JsonObject response = future.get(); // Blocking call to get the result

                if (response.has("items")) {
                    JsonArray items = response.getAsJsonArray("items");
                    for (JsonElement item : items) {
                        JsonObject answer = item.getAsJsonObject();

                        int commentId = answer.get("comment_id").getAsInt();
                        String body = answer.get("body").getAsString();
                        int score = answer.get("score").getAsInt();

                        // Insert into answers table
                        pstmt2.setInt(1, commentId);
                        pstmt2.setInt(2, questionId);
                        pstmt2.setString(3, body);
                        pstmt2.setInt(4, score);
                        pstmt2.executeUpdate();

                        System.out.println("Comment with ID " + commentId + " inserted successfully.");
                    }
                }
            }


        } catch (SQLException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    static List<JsonObject> getAllDataFromDatabaseForAcptInfo() {
        List<JsonObject> dataList = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE is_answered = 1";
        System.out.println(sql);
        System.out.println("update");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                JsonObject jsonObject = new JsonObject();
                // 获取每一行的数据
                jsonObject.addProperty("question_id", rs.getString("question_id"));
                jsonObject.addProperty("is_answered", rs.getString("is_answered"));
                jsonObject.addProperty("accepted_answer_id", rs.getString("accepted_answer_id"));
                dataList.add(jsonObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private static List<Integer> getAllQuestionIds() {
        List<Integer> questionIds = new ArrayList<>();
        String sql = "SELECT question_id FROM questions where question_id >= 0 ORDER BY question_id ASC";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                questionIds.add(rs.getInt("question_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questionIds;
    }




//    // ---------------------------------------------------------------
//    // ----------------------------------------------------------------
//    // ---------------- 下方代码不一定对
//
//
//    private static List<JsonObject> getAllTags() {
//        List<JsonObject> dataList = new ArrayList<>();
//        String sql = "SELECT tags, score, view_count FROM questions";
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
//             PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                JsonObject jsonObject = new JsonObject();
//                // 获取每一行的数据
//                jsonObject.addProperty("tags", rs.getString("tags"));
//                jsonObject.addProperty("score", rs.getString("score"));
//                jsonObject.addProperty("view_count", rs.getString("view_count"));
//                dataList.add(jsonObject);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return dataList;
//    }
//
//    private static List<JsonObject> getTagsForJavaRelated() {
//        List<JsonObject> tags = new ArrayList<>();
//
//        String sql = "SELECT name, count FROM tags;";
//        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            ResultSet rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("name", rs.getString("name"));
//                jsonObject.addProperty("count", rs.getString("count"));
//                tags.add(jsonObject);
//            }
//
//            return tags;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    static void updateDataInDatabase(JsonObject data) {
//        String sql = "UPDATE questions SET accepted_answer_id = ?, accepted_date = ?, not_public_will = ? WHERE question_id = ?";
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            // 根据你的表结构，设置预处理语句中的参数值
//            stmt.setInt(1, data.get("accepted_answer_id").getAsInt());
//            stmt.setTimestamp(2, new Timestamp(data.get("accepted_date").getAsLong()));
//            stmt.setBoolean(3, data.get("not_public_will").getAsBoolean());
//            stmt.setInt(4, data.get("question_id").getAsInt());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//    static JsonObject queryQuestionByAnswer(int answer_id, StackOverflowApi api) {
//        Map<String, String> params = new HashMap<>(1);
//        params.put("ids", String.valueOf(answer_id));
//        CompletableFuture<JsonObject> future = api.fetchData("answers", params);
//
//        try {
//            JsonObject jsonObject = future.get();
//            JsonArray items = jsonObject.getAsJsonArray("items");
//            return items.get(0).getAsJsonObject();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    static JsonObject queryQuestion(int question_id) throws SQLException {
//        String sql = "SELECT * FROM questions WHERE question_id = ?";
//
//        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setInt(1, question_id);
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    // 构建JsonObject
//                    JsonObject jsonObject = new JsonObject();
//                    jsonObject.addProperty("score", resultSet.getInt("score"));
//                    jsonObject.addProperty("view_count", resultSet.getInt("view_count"));
//                    jsonObject.addProperty("tags", resultSet.getString("tags"));
//
//                    return jsonObject;
//                } else {
//                    System.out.println("Question not found: " + question_id);
//                    return null;
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    static JsonObject queryTopAnswer(int question_id, StackOverflowApi api) {
//        try {
//            Map<String, String> params = new HashMap<>(1);
//            params.put("ids", String.valueOf(question_id));
//            params.put("sort", "votes");
//            params.put("order", "desc");
//            params.put("pagesize", "1");
//            CompletableFuture<JsonObject> future = api.fetchData("answer_question", params);
//
//            JsonArray items = future.get().getAsJsonArray("items");
//            return items.get(0).getAsJsonObject();
//
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static JsonArray queryQuestionAnswers(int questionId, StackOverflowApi api) {
//        Map<String, String> params = new HashMap<>(1);
//        params.put("ids", String.valueOf(questionId));
//        params.put("pagesize", "100");
//        CompletableFuture<JsonObject> future = api.fetchData("answer_question", params);
//        try {
//            JsonObject jsonObject = future.get();
//            return jsonObject.getAsJsonArray("items");
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private static JsonArray queryQuestionComments(int questionId, StackOverflowApi api) {
//        Map<String, String> params = new HashMap<>(1);
//        params.put("ids", String.valueOf(questionId));
//        params.put("pagesize", "100");
//        CompletableFuture<JsonObject> future = api.fetchData("comment_question", params);
//        try {
//            JsonObject jsonObject = future.get();
//            return jsonObject.getAsJsonArray("items");
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}

