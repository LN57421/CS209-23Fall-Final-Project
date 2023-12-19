package edu.sustech.cs209a.java2finalprojectdemo.mapper;


import edu.sustech.cs209a.java2finalprojectdemo.dao.QuestionsWithError;
import edu.sustech.cs209a.java2finalprojectdemo.model.Questions;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionsMapper {
    @Select("select * from questions")
    List<Questions> findAllQuestions();

    @Select("Select * from questions where score > 0")
    List<Questions> findQuestionsValuable();

    @Select("SELECT * FROM questions WHERE question_id = #{id}")
    Questions findQuestionById(Integer id);

    @Select("SELECT q.* FROM tags a " +
            "JOIN questions q ON a.question_id = q.question_id " + "WHERE a.name = #{keyword}")
    List<Questions> findQuestionsByTags(String keyword);

    @Select("SELECT * FROM questions WHERE body LIKE CONCAT('%', #{keyword}, '%')")
    List<Questions> findQuestionsByBody(String keyword);

    // 类与类之间对比
    @Select("SELECT q.*, se.name" +
            "FROM Questions q " +
            "JOIN SyntaxErrors se ON q.question_id = se.question_id")
    List<Questions> findQuestionsWithSyntaxError();

    @Select("SELECT q.*, fe.name" +
            "FROM Questions q " +
            "JOIN FatalErrors fe ON q.question_id = fe.question_id ")
    List<Questions> findQuestionsWithFatalError();

    @Select("SELECT q.*, ex.name" +
            "FROM Questions q " +
            "JOIN Exceptions ex ON q.question_id = ex.question_id ")
    List<Questions> findQuestionsWithException();


    // 类内部对比
    @Select("SELECT q.*, se.name " +
            "FROM Questions q " +
            "JOIN SyntaxErrors se ON q.question_id = se.question_id " +
            "WHERE se.name = #{name}")
    List<QuestionsWithError> findQuestionsWithSyntaxErrorByName(String name);

    @Select("SELECT q.*, fe.name " +
            "FROM Questions q " +
            "JOIN FatalErrors fe ON q.question_id = fe.question_id " +
            "WHERE fe.name = #{name}")
    List<QuestionsWithError> findQuestionsWithFatalErrorByName(String name);


    @Select("SELECT q.*, ex.name " +
            "FROM Questions q " +
            "JOIN Exceptions ex ON q.question_id = ex.question_id " +
            "WHERE ex.name = #{name}")
    List<QuestionsWithError> findQuestionsWithExceptionByName(String name);
}
