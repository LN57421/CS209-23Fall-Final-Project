package edu.sustech.cs209a.java2finalprojectdemo.mapper;

import edu.sustech.cs209a.java2finalprojectdemo.model.Comments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Mapper
public interface CommentsMapper extends JpaRepository<Comments, String> {
    @Select("Select * from comments")
    List<Comments> findAllComments();

    @Select("Select * from comments where question_id = #{question_id}")
    List<Comments> findCommentsByQuestionId(Integer question_id);

    @Select("Select * from comments where comment_id = #{comment_id}")
    List<Comments> findCommentsByCommentId(Integer answer_id);

    // 对于body进行特定匹配 包含某个关键词的查询等

    @Select("Select * from comments where score > 0")
    List<Comments> findCommentsValuable();
}
