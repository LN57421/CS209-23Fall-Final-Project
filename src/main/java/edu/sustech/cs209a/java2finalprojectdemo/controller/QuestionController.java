package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.model.Questions;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.QuestionsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "http://localhost:8080")
public class QuestionController {


    @Autowired
    private QuestionsMapper questionsMapper;

    @ApiOperation("获取所有Questions")
    @GetMapping("/all")
    public List<Questions> getAllQuestions() {
        return questionsMapper.findAllQuestions();
    }

    @ApiOperation("根据得分获取有价值的Questions")
    @GetMapping("/positive-score")
    public List<Questions> getPositiveScoreQuestions() {
        return questionsMapper.findQuestionsValuable();
    }

    @ApiOperation("获取包含Error的Question")
    @GetMapping("/GetAllError")
    public List<Questions> getErrorQuestion() {
        return questionsMapper.findQuestionsByBody("Error");
    }

    @ApiOperation("获取包含Exception的Question")
    @GetMapping("/GetAllException")
    public List<Questions> getExceptionQuestion() {
        return questionsMapper.findQuestionsByBody("Exception");
    }

    // 添加其他根据需求的方法
}
