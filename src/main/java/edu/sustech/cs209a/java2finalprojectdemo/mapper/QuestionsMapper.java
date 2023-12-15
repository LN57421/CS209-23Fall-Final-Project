package edu.sustech.cs209a.java2finalprojectdemo.mapper;


import edu.sustech.cs209a.java2finalprojectdemo.model.Questions;
import edu.sustech.cs209a.java2finalprojectdemo.model.Tags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Mapper
public interface QuestionsMapper {
    @Select("select * from questions")
    List<Questions> findAllQuestions();

    @Select("Select * from questions where score > 0")
    List<Questions> findQuestionsValuable();


    @Select("SELECT * FROM questions WHERE tags LIKE CONCAT('%', #{keyword}, '%')")
    List<Questions> findQuestionsByKeyword(String keyword);
}
