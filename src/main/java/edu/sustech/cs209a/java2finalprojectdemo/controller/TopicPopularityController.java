package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.mapper.QuestionsMapper;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.AnswersMapper;
import edu.sustech.cs209a.java2finalprojectdemo.model.Questions;
import edu.sustech.cs209a.java2finalprojectdemo.model.Answers;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class TopicPopularityController {

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private AnswersMapper answersMapper;

    private static final Logger logger = LoggerFactory.getLogger(TopicPopularityController.class);

    @ApiOperation("获取主题热度通过averageViewCount排序")
    @GetMapping("/topic-popularity/sortByAverageViewCount/{topics}")
    public Map<String, List<Object>> getTopicPopularitySortByAverageViewCount(@PathVariable List<String> topics) {
        Map<String, List<Object>> resultMap = new HashMap<>();

        List<Object> result = new ArrayList<>();
        for (String keyword : topics) {
            List<Questions> questions = questionsMapper.findQuestionsByTags(keyword);
            List<Answers> valuableAnswers = answersMapper.findValuableAnswersByTags(keyword);

            double avgViewCount = calculateAverageViewCount(questions);
            double avgScore = calculateAverageScore(questions);
            double avgValuableAnswerScore = calculateAverageValuableAnswerScore(valuableAnswers);

            // 直接构造 JSON 对象
            result.add(Map.of(
                    "keyword", keyword,
                    "averageViewCount", avgViewCount,
                    "averageScore", avgScore,
                    "averageValuableAnswerScore", avgValuableAnswerScore
            ));

            logger.info("Successfully get topic popularity: " + keyword);
        }
        // 按 averageViewCount 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageViewCount")));

        // 封装到最外层的 Map
        resultMap.put("topics", result);

        // 返回整体的 Map
        return resultMap;
    }

    @ApiOperation("获取主题热度通过avgScore排序")
    @GetMapping("/topic-popularity/sortByAverageScore/{topics}")
    public Map<String, List<Object>> getTopicPopularitySortByAverageScore(@PathVariable List<String> topics) {
        Map<String, List<Object>> resultMap = new HashMap<>();

        List<Object> result = new ArrayList<>();
        for (String keyword : topics) {
            List<Questions> questions = questionsMapper.findQuestionsByTags(keyword);
            List<Answers> valuableAnswers = answersMapper.findValuableAnswersByTags(keyword);

            double avgViewCount = calculateAverageViewCount(questions);
            double avgScore = calculateAverageScore(questions);
            double avgValuableAnswerScore = calculateAverageValuableAnswerScore(valuableAnswers);

            // 直接构造 JSON 对象
            result.add(Map.of(
                    "keyword", keyword,
                    "averageViewCount", avgViewCount,
                    "averageScore", avgScore,
                    "averageValuableAnswerScore", avgValuableAnswerScore
            ));

            logger.info("Successfully get topic popularity: " + keyword);
        }
        // 按 avgScore 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageScore")));

        // 封装到最外层的 Map
        resultMap.put("topics", result);

        // 返回整体的 Map
        return resultMap;
    }

    @ApiOperation("获取主题热度通过avgValuableAnswerScore排序")
    @GetMapping("/topic-popularity/sortByAverageValuableAnswerScore/{topics}")
    public Map<String, List<Object>> getTopicPopularitySortByAverageValuableAnswerScore(@PathVariable List<String> topics) {
        Map<String, List<Object>> resultMap = new HashMap<>();

        List<Object> result = new ArrayList<>();
        for (String keyword : topics) {
            List<Questions> questions = questionsMapper.findQuestionsByTags(keyword);
            List<Answers> valuableAnswers = answersMapper.findValuableAnswersByTags(keyword);

            double avgViewCount = calculateAverageViewCount(questions);
            double avgScore = calculateAverageScore(questions);
            double avgValuableAnswerScore = calculateAverageValuableAnswerScore(valuableAnswers);

            // 直接构造 JSON 对象
            result.add(Map.of(
                    "keyword", keyword,
                    "averageViewCount", avgViewCount,
                    "averageScore", avgScore,
                    "averageValuableAnswerScore", avgValuableAnswerScore
            ));

            logger.info("Successfully get topic popularity: " + keyword);
        }
        // 按 avgScore 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageValuableAnswerScore")));

        // 封装到最外层的 Map
        resultMap.put("topics", result);

        // 返回整体的 Map
        return resultMap;
    }

    private double calculateAverageViewCount(List<Questions> questions) {
        if (questions.isEmpty()) {
            return 0.0;
        }

        int totalViewCount = 0;
        for (Questions question : questions) {
            totalViewCount += question.getView_count();
        }

        return (double) totalViewCount / questions.size();
    }

    private double calculateAverageScore(List<Questions> questions) {
        if (questions.isEmpty()) {
            return 0.0;
        }

        int totalScore = 0;
        for (Questions question : questions) {
            totalScore += question.getScore();
        }

        return (double) totalScore / questions.size();
    }

    private double calculateAverageValuableAnswerScore(List<Answers> valuableAnswers) {
        if (valuableAnswers.isEmpty()) {
            return 0.0;
        }

        int totalScore = 0;
        for (Answers answer : valuableAnswers) {
            totalScore += answer.getScore();
        }

        return (double) totalScore / valuableAnswers.size();
    }
}
