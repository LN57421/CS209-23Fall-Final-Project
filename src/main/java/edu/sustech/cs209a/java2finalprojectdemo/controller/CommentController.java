package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.model.Comments;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.CommentsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:8080")
public class CommentController {

    @Autowired
    private CommentsMapper commentsMapper;

    @ApiOperation("获取所有Comments")
    @GetMapping("/all")
    public List<Comments> getAllComments() {
        return commentsMapper.findAllComments();
    }

    @ApiOperation("根据问题ID获取Comments")
    @GetMapping("/question/{questionId}")
    public List<Comments> getCommentsByQuestionId(@PathVariable Integer questionId) {
        return commentsMapper.findCommentsByQuestionId(questionId);
    }

    @ApiOperation("根据评论ID获取Comments")
    @GetMapping("/comment/{commentId}")
    public List<Comments> getCommentsByCommentId(@PathVariable Integer commentId) {
        return commentsMapper.findCommentsByCommentId(commentId);
    }

    // 可以添加其他根据需求的方法，比如根据关键词查询等

    @ApiOperation("根据得分获取有价值的Comments")
    @GetMapping("/positive-score")
    public List<Comments> getPositiveScoreComments() {
        return commentsMapper.findCommentsValuable();
    }

    // 添加其他根据需求的方法

}
