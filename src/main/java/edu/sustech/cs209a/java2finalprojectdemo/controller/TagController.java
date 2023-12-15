package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.model.Tags;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.TagsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagsMapper tagsMapper;

    @ApiOperation("获取所有Tags")
    @GetMapping("/all")
    public List<Tags> getAllTags() {
        return tagsMapper.findAllTags();
    }

    @ApiOperation("根据问题ID获取Tags")
    @GetMapping("/question/{questionId}")
    public List<Tags> getTagsByQuestionId(@PathVariable Integer questionId) {
        return tagsMapper.findTagsByQuestionId(questionId);
    }

    @ApiOperation("根据标签名获取Tags")
    @GetMapping("/name/{tagName}")
    public List<Tags> getTagsByName(@PathVariable String tagName) {
        return tagsMapper.findTagsByName(tagName);
    }

    @ApiOperation("根据得分获取有价值的Tags")
    @GetMapping("/positive-score")
    public List<Tags> getPositiveScoreTags() {
        return tagsMapper.findTagsValuable();
    }

    // 添加其他根据需求的方法

}
