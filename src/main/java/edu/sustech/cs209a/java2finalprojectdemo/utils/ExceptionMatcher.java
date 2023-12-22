package edu.sustech.cs209a.java2finalprojectdemo.utils;

import java.sql.*;

public class ExceptionMatcher {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/cs209a";
    private static final String USER = "root";
    private static final String PASS = "223844";

    public static void main(String[] args) {
        try {
            String SQL = "select * from questions";
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement(SQL);
                 ResultSet rs = stmt.executeQuery()) {

                int count1 = 0;
                int count2 = 0;
                int count3 = 0;


                // SQL for inserting errors into respective tables
                String insertSyntaxErrorSQL = "INSERT INTO SyntaxErrors (body, question_id, name) VALUES (?, ?, ?)";
                String insertFatalErrorSQL = "INSERT INTO FatalErrors (body, question_id, name) VALUES (?, ?, ?)";
                String insertExceptionSQL = "INSERT INTO Exceptions (body, question_id, name) VALUES (?, ?, ?)";

                try (PreparedStatement insertSyntaxErrorStmt = conn.prepareStatement(insertSyntaxErrorSQL);
                     PreparedStatement insertFatalErrorStmt = conn.prepareStatement(insertFatalErrorSQL);
                     PreparedStatement insertExceptionStmt = conn.prepareStatement(insertExceptionSQL)) {

                    while (rs.next()) {
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
                            // 内部再加一层分类  匹配 为name赋值插入表中
                            String name = extractSyntaxErrorCategory(body);
                            insertSyntaxErrorStmt.setString(1, body);
                            insertSyntaxErrorStmt.setInt(2, questionId);
                            insertSyntaxErrorStmt.setString(3, name);
                            insertSyntaxErrorStmt.addBatch();
                            count1++;
                        }

                        // 匹配致命错误
                        if (body.toLowerCase().contains("outofmemory") || body.toLowerCase().contains("fatal error") ||
                                body.toLowerCase().contains("runtime error") || body.toLowerCase().contains("unrecoverable error") ||
                                body.toLowerCase().contains("unexpected error") || body.toLowerCase().contains("critical error") ||
                                body.toLowerCase().contains("severe error") ||
                                body.toLowerCase().contains("system crash") || body.toLowerCase().contains("catastrophic error") ||
                                body.toLowerCase().contains("fatal mistake") || body.toLowerCase().contains("serious error") ||
                                body.toLowerCase().contains("critical mistake") || body.toLowerCase().contains("unrecoverable mistake") ||
                                body.toLowerCase().contains("system failure") || body.toLowerCase().contains("catastrophic failure") ||
                                body.toLowerCase().contains("unrecoverable failure") || body.toLowerCase().contains("fatal crash") ||
                                body.toLowerCase().contains("severe exception") || body.toLowerCase().contains("catastrophic exception")) {
                            String name = extractFatalErrorCategory(body);
                            insertFatalErrorStmt.setString(1, body);
                            insertFatalErrorStmt.setInt(2, questionId);
                            insertFatalErrorStmt.setString(3, name);
                            insertFatalErrorStmt.addBatch();
                            count2++;
                        }

                        // 匹配异常
                        if (body.toLowerCase().contains("nullpointerexception") || body.toLowerCase().contains("arrayindexoutofboundsexception") ||
                                body.toLowerCase().contains("arithmeticexception") || body.toLowerCase().contains("illegalargumentexception") ||
                                body.toLowerCase().contains("illegalstateexception") || body.toLowerCase().contains("indexoutofboundsexception") || body.toLowerCase().contains("fatal exception") ||
                                body.toLowerCase().contains("classcastexception") || body.toLowerCase().contains("numberformatexception") ||
                                body.toLowerCase().contains("unsupportedoperationexception") || body.toLowerCase().contains("ioexception") ||
                                body.toLowerCase().contains("sqlexception") || body.toLowerCase().contains("filenotfoundexception")) {
                            String name = extractExceptionCategory(body);
                            insertExceptionStmt.setString(1, body);
                            insertExceptionStmt.setInt(2, questionId);
                            insertExceptionStmt.setString(3, name);
                            insertExceptionStmt.addBatch();
                            count3++;
                        }
                    }

                    // 执行批处理插入
                    insertSyntaxErrorStmt.executeBatch();
                    insertFatalErrorStmt.executeBatch();
                    insertExceptionStmt.executeBatch();

                    System.out.println("Inserted Syntax Errors: " + count1);
                    System.out.println("Inserted Fatal Errors: " + count2);
                    System.out.println("Inserted General Exceptions: " + count3);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String extractSyntaxErrorCategory(String body) {
        // 根据具体的匹配逻辑，分类语法错误的名称
        if (body.toLowerCase().contains("syntax error") || body.toLowerCase().contains("syntaxerror")|| body.toLowerCase().contains("syntax mistake") || body.toLowerCase().contains("syntax issue") ||
                body.toLowerCase().contains("syntax problem") || body.toLowerCase().contains("parsing error") ||body.toLowerCase().contains("parse error")||
                body.toLowerCase().contains("coding error") || body.toLowerCase().contains("error in syntax") ||
                body.toLowerCase().contains("syntax failure") || body.toLowerCase().contains("syntax flaw") ||
                body.toLowerCase().contains("syntax defect") || body.toLowerCase().contains("invalid syntax")) {
            return "syntax error";
        } else if (body.toLowerCase().contains("compile error") || body.toLowerCase().contains("compilation error")|| body.toLowerCase().contains("code error") || body.toLowerCase().contains("compiling error")
                || body.toLowerCase().contains("programming error") || body.toLowerCase().contains("coding mistake") ||
                body.toLowerCase().contains("code issue")) {
            return "compile error";
        } else if (body.toLowerCase().contains("unexpected token")) {
            return "unexpected token";
        } {
            return "uncategorized";
        }
    }




    private static String extractFatalErrorCategory(String body) {
        // 根据具体的匹配逻辑，分类致命错误的名称
        if (body.toLowerCase().contains("outofmemory")) {
            return "out of memory";
        } else if (body.toLowerCase().contains("fatal error")) {
            return "fatal error";
        } else if (body.toLowerCase().contains("runtime error")) {
            return "runtime error";
        } else if (body.toLowerCase().contains("unrecoverable error")) {
            return "unrecoverable error";
        } else if (body.toLowerCase().contains("unexpected error")) {
            return "unexpected error";
        } else if (body.toLowerCase().contains("critical error")) {
            return "critical error";
        } else if (body.toLowerCase().contains("severe error")) {
            return "severe error";
        }else if (body.toLowerCase().contains("system crash")) {
            return "system crash";
        } else if (body.toLowerCase().contains("catastrophic error")) {
            return "catastrophic error";
        } else if (body.toLowerCase().contains("fatal mistake")) {
            return "fatal mistake";
        } else if (body.toLowerCase().contains("serious error")) {
            return "serious error";
        } else if (body.toLowerCase().contains("critical mistake")) {
            return "critical mistake";
        } else if (body.toLowerCase().contains("unrecoverable mistake")) {
            return "unrecoverable mistake";
        } else if (body.toLowerCase().contains("system failure")) {
            return "system failure";
        } else if (body.toLowerCase().contains("catastrophic failure")) {
            return "catastrophic failure";
        } else if (body.toLowerCase().contains("unrecoverable failure")) {
            return "unrecoverable failure";
        } else if (body.toLowerCase().contains("fatal crash")) {
            return "fatal crash";
        } else if (body.toLowerCase().contains("severe exception")) {
            return "severe exception";
        } else if (body.toLowerCase().contains("catastrophic exception")) {
            return "catastrophic exception";
        } else {
            return "uncategorized";
        }
    }


    private static String extractExceptionCategory(String body) {
        // 根据具体的匹配逻辑，分类异常的名称
        if (body.toLowerCase().contains("nullpointerexception")) {
            return "nullpointerexception";
        } else if (body.toLowerCase().contains("arrayindexoutofboundsexception")) {
            return "arrayindexoutofboundsexception";
        } else if (body.toLowerCase().contains("arithmeticexception")) {
            return "arithmeticexception";
        } else if (body.toLowerCase().contains("illegalargumentexception")) {
            return "illegalargumentexception";
        } else if (body.toLowerCase().contains("illegalstateexception")) {
            return "illegalstateexception";
        } else if (body.toLowerCase().contains("indexoutofboundsexception")) {
            return "indexoutofboundsexception";
        } else if (body.toLowerCase().contains("classcastexception")) {
            return "classcastexception";
        } else if (body.toLowerCase().contains("numberformatexception")) {
            return "numberformatexception";
        } else if (body.toLowerCase().contains("unsupportedoperationexception")) {
            return "unsupportedoperationexception";
        } else if (body.toLowerCase().contains("ioexception")) {
            return "ioexception";
        } else if (body.toLowerCase().contains("sqlexception")) {
            return "sqlexception";
        } else if (body.toLowerCase().contains("filenotfoundexception")) {
            return "filenotfoundexception";
        }  else if (body.toLowerCase().contains("fatal exception")) {
            return "fatal exception";
        } else {
            return "uncategorized";
        }
    }

}
