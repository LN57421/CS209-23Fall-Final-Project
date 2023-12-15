package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.model.Questions;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.QuestionsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
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

    // 添加其他根据需求的方法

}
