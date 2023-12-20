package edu.sustech.cs209a.java2finalprojectdemo.mapper;

import edu.sustech.cs209a.java2finalprojectdemo.model.FatalErrors;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FatalErrorsMapper {
    @Select("SELECT * FROM FatalErrors")
    List<FatalErrors> findAllFatalErrors();

    @Select("SELECT * FROM FatalErrors WHERE name = #{name}")
    List<FatalErrors> findFatalErrorsByName(String name);
}
