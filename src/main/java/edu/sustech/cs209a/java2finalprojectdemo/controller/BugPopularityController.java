package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.dao.QuestionsWithError;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.ExceptionsMapper;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.FatalErrorsMapper;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.QuestionsMapper;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.SyntaxErrorsMapper;
import edu.sustech.cs209a.java2finalprojectdemo.model.Exceptions;
import edu.sustech.cs209a.java2finalprojectdemo.model.FatalErrors;
import edu.sustech.cs209a.java2finalprojectdemo.model.SyntaxErrors;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class BugPopularityController {

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private ExceptionsMapper exceptionsMapper;

    @Autowired
    private SyntaxErrorsMapper syntaxErrorsMapper;

    @Autowired
    private FatalErrorsMapper fatalErrorsMapper;

    private static final Logger logger = LoggerFactory.getLogger(BugPopularityController.class);

    @ApiOperation("获取三个bug大类中各个小类对应的热度")
    @GetMapping("/bug-show/allDetail")
    public Map<String, List<Object>> getAllThreePopularityDetail(){
        Map<String, List<Object>> resultMap = new HashMap<>();
        List<Object> result = new ArrayList<>();
        List<Object> details = new ArrayList<>();


        result.add(Map.of(
                "name", "exceptions",
                "children", getAllFatalErrorsPopularity()
        ));

        result.add(Map.of(
                "name", "syntaxError",
                "children", getAllSyntaxErrorsPopularity()
        ));

        result.add(Map.of(
                "name", "fatalError",
                "children", getAllFatalErrorsPopularity()
        ));

        logger.info("Successfully get three bug popularity detail:  all exceptions, syntax errors, fatal errors");

        details.add(Map.of(
                "name", "syntaxError",
                 "children", result
        ));

        // 封装到最外层的 Map
        resultMap.put("bugPopularityDetails", details);

        // 返回整体的 Map
        return resultMap;

    }

    @ApiOperation("获取三个bug大类对应的热度")
    @GetMapping("/bug-show/all")
    public Map<String, List<Object>> getAllThreePopularity(){
        Map<String, List<Object>> resultMap = new HashMap<>();
        List<Object> result = new ArrayList<>();

        // 构建三个set
        List<SyntaxErrors> syntaxErrors_ = syntaxErrorsMapper.findAllSyntaxErrors();
        Set<String> syntaxErrors = new HashSet<>();
        for (SyntaxErrors s: syntaxErrors_) {
            syntaxErrors.add(s.name);
        }


        List<Exceptions> exceptions = exceptionsMapper.findAllExceptions();
        Set<String>  exceptionNames  = new HashSet<>();
        for (Exceptions e: exceptions) {
            exceptionNames.add(e.name);
        }

        List<FatalErrors> fatalErrorsList = fatalErrorsMapper.findAllFatalErrors();
        Set<String> fatalErrors = new HashSet<>();
        for (FatalErrors f: fatalErrorsList) {
            fatalErrors.add(f.name);
        }

        double avgViewCount = 0;
        for (String exceptionName: exceptionNames) {
            List<QuestionsWithError> questionsWithErrorList = questionsMapper.findQuestionsWithExceptionByName(exceptionName);
            avgViewCount += getViewCountFrequency(questionsWithErrorList);
        }
        result.add(Map.of(
                "className", "exceptionClass",
                "averageViewCountSum", avgViewCount
        ));


        avgViewCount = 0;
        for (String syntaxError: syntaxErrors) {
            List<QuestionsWithError> questionsWithErrorList = questionsMapper.findQuestionsWithSyntaxErrorByName(syntaxError);
            avgViewCount += getViewCountFrequency(questionsWithErrorList);
            System.out.println(syntaxError);
        }
        result.add(Map.of(
                "className", "syntaxErrorsClass",
                "averageViewCountSum", avgViewCount
        ));

        avgViewCount = 0;
        for (String fatalError: fatalErrors) {
            List<QuestionsWithError> questionsWithErrorList = questionsMapper.findQuestionsWithFatalErrorByName(fatalError);
            avgViewCount = getViewCountFrequency(questionsWithErrorList);
        }
        result.add(Map.of(
                "className", "fatalErrorsClass",
                "averageViewCountSum", avgViewCount
        ));

        logger.info("Successfully get three bug popularity: all exceptions, syntax errors, fatal errors");

        // 按 averageViewCount 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageViewCountSum")));

        // 封装到最外层的 Map
        resultMap.put("ThreeClassFrequencySum", result);

        // 返回整体的 Map
        return resultMap;

    }


    @ApiOperation("获取所有exception对应的热度")
    @GetMapping("/bug-show/exception/all")
    public Map<String, List<Object>> getAllExceptionPopularity(){
        Map<String, List<Object>> resultMap = new HashMap<>();
        List<Object> result = new ArrayList<>();
        List<Exceptions> exceptions = exceptionsMapper.findAllExceptions();
        Set<String>  exceptionNames  = new HashSet<>();
        for (Exceptions e: exceptions) {
            exceptionNames.add(e.name);
        }
        for (String exceptionName: exceptionNames) {
            List<QuestionsWithError> questionsWithErrorList = questionsMapper.findQuestionsWithExceptionByName(exceptionName);
            double avgViewCount = getViewCountFrequency(questionsWithErrorList);
            // 直接构造 JSON 对象
            result.add(Map.of(
                    "exceptionName", exceptionName,
                    "averageViewCount", avgViewCount
            ));

            logger.info("Successfully get bug popularity: all exceptions");
        }
        // 按 averageViewCount 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageViewCount")));

        // 封装到最外层的 Map
        resultMap.put("exceptionFrequency", result);

        // 返回整体的 Map
        return resultMap;
    }



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

            logger.info("Successfully get exception bug popularity: " + exceptionName);
        }
        // 按 averageViewCount 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageViewCount")));

        // 封装到最外层的 Map
        resultMap.put("exceptionFrequency", result);

        // 返回整体的 Map
        return resultMap;
    }

    @ApiOperation("获取所有syntaxErrors对应的热度")
    @GetMapping("/bug-show/syntax/all")
    public Map<String, List<Object>> getAllSyntaxErrorsPopularity(){

        Map<String, List<Object>> resultMap = new HashMap<>();
        List<Object> result = new ArrayList<>();

        List<SyntaxErrors> syntaxErrors_ = syntaxErrorsMapper.findAllSyntaxErrors();
        Set<String> syntaxErrors = new HashSet<>();
        for (SyntaxErrors s: syntaxErrors_) {
            syntaxErrors.add(s.name);
        }
        for (String syntaxError: syntaxErrors) {
            List<QuestionsWithError> questionsWithErrorList = questionsMapper.findQuestionsWithSyntaxErrorByName(syntaxError);
            double avgViewCount = getViewCountFrequency(questionsWithErrorList);
            // 直接构造 JSON 对象
            result.add(Map.of(
                    "exceptionName", syntaxError,
                    "averageViewCount", avgViewCount
            ));
        }

        logger.info("Successfully get bug popularity: all syntax errors");

        // 按 averageViewCount 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageViewCount")));

        // 封装到最外层的 Map
        resultMap.put("syntaxErrorFrequency", result);

        // 返回整体的 Map
        return resultMap;
    }

    @ApiOperation("获取单个syntaxErrors对应的热度")
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

            logger.info("Successfully get syntaxError bug popularity: " + syntaxError);
        }
        // 按 averageViewCount 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageViewCount")));

        // 封装到最外层的 Map
        resultMap.put("syntaxErrorFrequency", result);

        // 返回整体的 Map
        return resultMap;
    }



    @ApiOperation("获取所有fatalErrors对应的热度")
    @GetMapping("/bug-show/fatal/all")
    public Map<String, List<Object>> getAllFatalErrorsPopularity(){
        Map<String, List<Object>> resultMap = new HashMap<>();
        List<Object> result = new ArrayList<>();

        List<FatalErrors> fatalErrorsList = fatalErrorsMapper.findAllFatalErrors();
        Set<String> fatalErrors = new HashSet<>();
        for (FatalErrors f: fatalErrorsList) {
            fatalErrors.add(f.name);
        }

        for (String fatalError: fatalErrors) {
            List<QuestionsWithError> questionsWithErrorList = questionsMapper.findQuestionsWithFatalErrorByName(fatalError);
            double avgViewCount = getViewCountFrequency(questionsWithErrorList);
            // 直接构造 JSON 对象
            result.add(Map.of(
                    "exceptionName", fatalError,
                    "averageViewCount", avgViewCount
            ));
        }
        logger.info("Successfully get bug popularity: all fatal errors");

        // 按 averageViewCount 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageViewCount")));

        // 封装到最外层的 Map
        resultMap.put("fatalErrorFrequency", result);

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
            logger.info("Successfully get fatalError bug popularity: " + fatalError);
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
            System.out.println(question);
            count += question.view_count;
        }
        return count / questionsMapper.findAllQuestions().size();
    }

}
