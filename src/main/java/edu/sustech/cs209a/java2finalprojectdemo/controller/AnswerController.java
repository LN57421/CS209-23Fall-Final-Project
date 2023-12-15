package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.model.Answers;
import io.swagger.annotations.ApiOperation;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.AnswersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/answers") // 修正路径
public class AnswerController {

    @Autowired
    private AnswersMapper answersMapper;

    @ApiOperation("获取所有Answers")
    @GetMapping("/all")
    public List<Answers> getAllAnswers(){
        return answersMapper.findAllAnswers();
    }

    @ApiOperation("根据问题ID获取Answers")
    @GetMapping("/question/{questionId}")
    public List<Answers> getAnswersByQuestionId(@PathVariable Integer questionId){
        return answersMapper.findAnswersByQuestionId(questionId);
    }

    @ApiOperation("根据回答ID获取Answers")
    @GetMapping("/answer/{answerId}")
    public List<Answers> getAnswersByAnswerId(@PathVariable Integer answerId){
        return answersMapper.findAnswersByAnswerId(answerId);
    }

    // 可以添加其他根据需求的方法，比如根据关键词查询等

    @ApiOperation("根据得分获取有价值的Answer")
    @GetMapping("/positive-score")
    public List<Answers> getPositiveScoreAnswers(){
        return answersMapper.findAnswersValuable();
    }

    // 添加其他根据需求的方法

}
