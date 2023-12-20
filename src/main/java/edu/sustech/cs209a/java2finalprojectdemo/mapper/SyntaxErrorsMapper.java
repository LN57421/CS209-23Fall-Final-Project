package edu.sustech.cs209a.java2finalprojectdemo.mapper;

import edu.sustech.cs209a.java2finalprojectdemo.model.Questions;
import edu.sustech.cs209a.java2finalprojectdemo.model.SyntaxErrors;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SyntaxErrorsMapper {
    @Select("SELECT * FROM SyntaxErrors")
    List<SyntaxErrors> findAllSyntaxErrors();

    @Select("SELECT * FROM SyntaxErrors WHERE name = #{name}")
    List<SyntaxErrors> findSyntaxErrorsByName(String name);


}
