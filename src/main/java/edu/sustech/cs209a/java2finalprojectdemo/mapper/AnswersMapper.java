package edu.sustech.cs209a.java2finalprojectdemo.mapper;


import edu.sustech.cs209a.java2finalprojectdemo.model.Answers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface AnswersMapper {

    @Select("select * from answers")
    List<Answers> findAllAnswers();

    @Select("Select * from answers where question_id = #{question_id}")
    List<Answers> findAnswersByQuestionId(Integer question_id);

    @Select("Select * from answers where answer_id = #{answer_id}")
    List<Answers> findAnswersByAnswerId(Integer answer_id);

    // 对于body进行特定匹配 包含某个关键词的查询等

    @Select("Select * from answers where score > 0")
    List<Answers> findAnswersValuable();


    // 信息存储在tags表中
    @Select("SELECT q.* FROM tags a " +
            "JOIN answers q ON a.question_id = q.question_id " +
            "WHERE a.name =  #{keyword} AND q.score > 0")
    List<Answers> findValuableAnswersByTags(String keyword);
}