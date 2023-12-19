package edu.sustech.cs209a.java2finalprojectdemo.mapper;


import edu.sustech.cs209a.java2finalprojectdemo.model.Tags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Mapper
public interface TagsMapper{

    @Select("Select * from tags")
    List<Tags> findAllTags();

    @Select("Select * from tags where question_id = #{question_id}")
    List<Tags> findTagsByQuestionId(Integer question_id);

    @Select("Select * from tags where name = #{tagName}")
    List<Tags> findTagsByName(String tagName);

    @Select("Select * from tags where score > 0")
    List<Tags> findTagsValuable();
}
