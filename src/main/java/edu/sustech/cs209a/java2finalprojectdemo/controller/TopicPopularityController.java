package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.mapper.QuestionsMapper;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.AnswersMapper;
import edu.sustech.cs209a.java2finalprojectdemo.model.Questions;
import edu.sustech.cs209a.java2finalprojectdemo.model.Answers;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class TopicPopularityController {

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private AnswersMapper answersMapper;

    @ApiOperation("获取主题热度")
    @GetMapping("/topic-popularity/{topics}")
    public Map<String, Object> getTopicPopularity(@PathVariable List<String> topics) {
        Map<String, Object> topicPopularityMap = new HashMap<>();

        for (String keyword : topics) {
            List<Questions> questions = questionsMapper.findQuestionsByTags(keyword);
            List<Answers> valuableAnswers = answersMapper.findValuableAnswersByTags(keyword);

            double avgViewCount = calculateAverageViewCount(questions);
            double avgScore = calculateAverageScore(questions);
            double avgValuableAnswerScore = calculateAverageValuableAnswerScore(valuableAnswers);

            // 存储结果到Map
            topicPopularityMap.put(keyword, Map.of(
                    "keyword", keyword,
                    "averageViewCount", avgViewCount,
                    "averageScore", avgScore,
                    "averageValuableAnswerScore", avgValuableAnswerScore
            ));
        }

        // 返回整体的Map
        return topicPopularityMap;
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
            System.out.println("empty");
            return 0.0;
        }

        int totalScore = 0;
        for (Answers answer : valuableAnswers) {
            totalScore += answer.getScore();
            System.out.println("1");
            System.out.println(answer.toString());
        }

        return (double) totalScore / valuableAnswers.size();
    }
}
