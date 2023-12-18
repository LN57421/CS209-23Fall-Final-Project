package edu.sustech.cs209a.java2finalprojectdemo.utils;

import java.sql.*;

public class ExceptionMatcher {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/cs209a";
    private static final String USER = "root";
    private static final String PASS = "NIANljb123";

    public static void main(String[] args) {
        try {
            String SQL = "select * from questions";
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement(SQL);
                 ResultSet rs = stmt.executeQuery()) {

                int count1 = 0;
                int count2 = 0;
                int count3 = 0;
                while (rs.next()) {
                    //System.out.println(1);
                    int questionId = rs.getInt("question_id");
                    String body = rs.getString("body");


                    // 匹配语法错误
                    if (body.toLowerCase().contains("syntax error") || body.toLowerCase().contains("compile error") ||
                            body.toLowerCase().contains("syntaxerror") || body.toLowerCase().contains("compilation error") ||
                            body.toLowerCase().contains("compiling error") || body.toLowerCase().contains("parse error") ||
                            body.toLowerCase().contains("unexpected token") || body.toLowerCase().contains("invalid syntax") ||
                            body.toLowerCase().contains("syntax mistake") || body.toLowerCase().contains("syntax issue") ||
                            body.toLowerCase().contains("syntax problem") || body.toLowerCase().contains("parsing error") ||
                            body.toLowerCase().contains("coding error") || body.toLowerCase().contains("error in syntax") ||
                            body.toLowerCase().contains("syntax failure") || body.toLowerCase().contains("syntax flaw") ||
                            body.toLowerCase().contains("syntax defect") || body.toLowerCase().contains("code error") ||
                            body.toLowerCase().contains("programming error") || body.toLowerCase().contains("coding mistake") ||
                            body.toLowerCase().contains("code issue")) {
                        System.out.println("Question ID: " + questionId);
                        System.out.println("Matched Exception Type: Syntax Error");
                        System.out.println("Matched Body: " + body);
                        count1++;
                    }

                    // 匹配致命错误
                    if (body.toLowerCase().contains("outofmemory") || body.toLowerCase().contains("fatal error") ||
                            body.toLowerCase().contains("runtime error") || body.toLowerCase().contains("unrecoverable error") ||
                            body.toLowerCase().contains("unexpected error") || body.toLowerCase().contains("critical error") ||
                            body.toLowerCase().contains("severe error") || body.toLowerCase().contains("fatal exception") ||
                            body.toLowerCase().contains("system crash") || body.toLowerCase().contains("catastrophic error") ||
                            body.toLowerCase().contains("fatal mistake") || body.toLowerCase().contains("serious error") ||
                            body.toLowerCase().contains("critical mistake") || body.toLowerCase().contains("unrecoverable mistake") ||
                            body.toLowerCase().contains("system failure") || body.toLowerCase().contains("catastrophic failure") ||
                            body.toLowerCase().contains("unrecoverable failure") || body.toLowerCase().contains("fatal crash") ||
                            body.toLowerCase().contains("severe exception") || body.toLowerCase().contains("catastrophic exception")) {
                        System.out.println("Question ID: " + questionId);
                        System.out.println("Matched Exception Type: Fatal Error");
                        System.out.println("Matched Body: " + body);
                        count2++;
                    }

                    // 匹配异常
                    if (body.toLowerCase().contains("nullpointerexception") || body.toLowerCase().contains("arrayindexoutofboundsexception") ||
                            body.toLowerCase().contains("arithmeticexception") || body.toLowerCase().contains("illegalargumentexception") ||
                            body.toLowerCase().contains("illegalstateexception") || body.toLowerCase().contains("indexoutofboundsexception") ||
                            body.toLowerCase().contains("classcastexception") || body.toLowerCase().contains("numberformatexception") ||
                            body.toLowerCase().contains("unsupportedoperationexception") || body.toLowerCase().contains("ioexception") ||
                            body.toLowerCase().contains("sqlexception") || body.toLowerCase().contains("filenotfoundexception")) {
                        System.out.println("Question ID: " + questionId);
                        System.out.println("Matched Exception Type: General Exception");
                        System.out.println("Matched Body: " + body);
                        count3++;
                    }
                }
                System.out.println(count1);
                System.out.println(count2);
                System.out.println(count3);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

