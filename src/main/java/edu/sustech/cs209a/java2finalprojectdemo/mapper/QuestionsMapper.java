package edu.sustech.cs209a.java2finalprojectdemo.mapper;


import edu.sustech.cs209a.java2finalprojectdemo.model.Questions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionsMapper {
    @Select("select * from questions")
    List<Questions> findAllQuestions();

    @Select("Select * from questions where score > 0")
    List<Questions> findQuestionsValuable();


    @Select("SELECT q.* FROM tags a " +
            "JOIN questions q ON a.question_id = q.question_id " + "WHERE a.name = #{keyword}")
    List<Questions> findQuestionsByTags(String keyword);

    @Select("SELECT * FROM questions WHERE body LIKE CONCAT('%', #{keyword}, '%')")
    List<Questions> findQuestionsByBody(String keyword);
}
