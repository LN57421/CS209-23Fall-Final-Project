package edu.sustech.cs209a.java2finalprojectdemo.mapper;

import edu.sustech.cs209a.java2finalprojectdemo.model.Exceptions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExceptionsMapper {
    @Select("SELECT * FROM Exceptions")
    List<Exceptions> findAllExceptions();

    @Select("SELECT * FROM Exceptions WHERE name = #{name}")
    List<Exceptions> findExceptionsByName(String name);
}
