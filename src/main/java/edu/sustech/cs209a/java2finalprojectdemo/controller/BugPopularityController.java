package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.dao.QuestionsWithError;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.QuestionsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:8080")
public class BugPopularityController {

    @Autowired
    private QuestionsMapper questionsMapper;

    @ApiOperation("获取某个exceptionName对应的热度")
    @GetMapping("/bug-show/exception/{exceptionNames}")  // "sqlexception"
    public Map<String, List<Object>> getExceptionPopularity(@PathVariable List<String> exceptionNames){
        Map<String, List<Object>> resultMap = new HashMap<>();
        List<Object> result = new ArrayList<>();

        for (String exceptionName: exceptionNames) {
            List<QuestionsWithError> questionsWithErrorList = questionsMapper.findQuestionsWithExceptionByName(exceptionName);
            double avgViewCount = getViewCountFrequency(questionsWithErrorList);
            // 直接构造 JSON 对象
            result.add(Map.of(
                    "exceptionName", exceptionName,
                    "averageViewCount", avgViewCount
            ));
        }
        // 按 averageViewCount 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageViewCount")));

        // 封装到最外层的 Map
        resultMap.put("exceptionFrequency", result);

        // 返回整体的 Map
        return resultMap;
    }

    @ApiOperation("获取某个syntaxErrors对应的热度")
    @GetMapping("/bug-show/syntax/{syntaxErrors}")  // "compile error"
    public Map<String, List<Object>> getSyntaxErrorsPopularity(@PathVariable List<String> syntaxErrors){
        Map<String, List<Object>> resultMap = new HashMap<>();
        List<Object> result = new ArrayList<>();

        for (String syntaxError: syntaxErrors) {
            List<QuestionsWithError> questionsWithErrorList = questionsMapper.findQuestionsWithSyntaxErrorByName(syntaxError);
            double avgViewCount = getViewCountFrequency(questionsWithErrorList);
            // 直接构造 JSON 对象
            result.add(Map.of(
                    "exceptionName", syntaxError,
                    "averageViewCount", avgViewCount
            ));
        }
        // 按 averageViewCount 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageViewCount")));

        // 封装到最外层的 Map
        resultMap.put("syntaxErrorFrequency", result);

        // 返回整体的 Map
        return resultMap;
    }


    @ApiOperation("获取某个fatalErrors对应的热度")
    @GetMapping("/bug-show/fatal/{fatalErrors}")  // "compile error"
    public Map<String, List<Object>> getFatalErrorsPopularity(@PathVariable List<String> fatalErrors){
        Map<String, List<Object>> resultMap = new HashMap<>();
        List<Object> result = new ArrayList<>();

        for (String fatalError: fatalErrors) {
            List<QuestionsWithError> questionsWithErrorList = questionsMapper.findQuestionsWithFatalErrorByName(fatalError);
            double avgViewCount = getViewCountFrequency(questionsWithErrorList);
            // 直接构造 JSON 对象
            result.add(Map.of(
                    "exceptionName", fatalError,
                    "averageViewCount", avgViewCount
            ));
        }
        // 按 averageViewCount 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageViewCount")));

        // 封装到最外层的 Map
        resultMap.put("fatalErrorFrequency", result);

        // 返回整体的 Map
        return resultMap;
    }

    private double getViewCountFrequency(List<QuestionsWithError> questionsWithErrorList) {
        double count = 0;
        for (QuestionsWithError question: questionsWithErrorList) {
            count += question.view_count;
        }
        return count / questionsMapper.findAllQuestions().size();
    }

}
