package edu.sustech.cs209a.java2finalprojectdemo.mapper;


import edu.sustech.cs209a.java2finalprojectdemo.model.Answers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Mapper
public interface AnswersMapper extends JpaRepository<Answers, String> {

    @Select("select * from answers")
    List<Answers> findAllAnswers();

    @Select("Select * from answers where question_id = #{question_id}")
    List<Answers> findAnswersByQuestionId(Integer question_id);

    @Select("Select * from answers where answer_id = #{answer_id}")
    List<Answers> findAnswersByAnswerId(Integer answer_id);

    // 对于body进行特定匹配 包含某个关键词的查询等

    @Select("Select * from answers where score > 0")
    List<Answers> findAnswersValuable();


    // 假设关键词信息存储在Questions表中
    @Select("SELECT a.* FROM answers a " +
            "JOIN questions q ON a.question_id = q.question_id " +
            "WHERE q.tags =  #{keyword} AND a.score > 0")
    List<Answers> findValuableAnswersByTags(String keyword);
}