package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.dao.QuestionsWithError;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.QuestionsMapper;
import edu.sustech.cs209a.java2finalprojectdemo.model.Questions;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class BugPopularityController {

    @Autowired
    private QuestionsMapper questionsMapper;

    @ApiOperation("获取所有Questions")
    @GetMapping("/bug-show/all")
    public List<QuestionsWithError> getAllQuestions() {
        return questionsMapper.findQuestionsWithExceptionByName("sqlexception");
    }

}
