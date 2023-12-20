package edu.sustech.cs209a.java2finalprojectdemo.controller;

import edu.sustech.cs209a.java2finalprojectdemo.mapper.QuestionsMapper;
import edu.sustech.cs209a.java2finalprojectdemo.mapper.TagsMapper;
import edu.sustech.cs209a.java2finalprojectdemo.model.Questions;
import edu.sustech.cs209a.java2finalprojectdemo.model.Tags;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class RelatedTopicController {

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private TagsMapper tagsMapper;

    @ApiOperation("获取某个phase对应的热度")
    @GetMapping("/related-topic/{inputPhase}")
    public Map<String, List<Object>> getPhasePopularity(@PathVariable String inputPhase) {
        Map<String, List<Object>> resultMap = new HashMap<>();
        List<Object> result = new ArrayList<>();

        String[] phases = inputPhase.split("\\s+");

        Set<Integer> finalQuestions = new HashSet<>();

        // 先获取所有phase相关的Tags对应的question 注意不要重复
        for (String phase : phases) {
            List<Questions> questions = questionsMapper.findQuestionsByTags(phase);
            for (Questions q : questions) {
                finalQuestions.add(q.question_id);
            }
        }

        // 计算每个标签出现的次数
        Map<String, Integer> tagCountMap = new HashMap<>();

        // 然后对于每个Question对应的所有标签进行梳理
        for (Integer id : finalQuestions) {
            List<Tags> tags = tagsMapper.findTagsByQuestionId(id);
            // 注意排除掉用户输入的
            for (String phase : phases) {
                tags.removeIf(f -> f.name.equals(phase));
            }
            // 计算每个标签出现的次数
            for (Tags tag : tags) {
                tagCountMap.put(tag.name, tagCountMap.getOrDefault(tag.name, 0) + 1);
            }
        }

        // 对于每个tag构造json对象
        for (Map.Entry<String, Integer> entry : tagCountMap.entrySet()) {
            String tagName = entry.getKey();
            Integer tagCount = entry.getValue();
            double averageRelatedCount = (double) tagCount / finalQuestions.size();

            // 构造 JSON 对象
            result.add(Map.of(
                    "tagName", tagName,
                    "averageRelatedCount", averageRelatedCount
            ));
        }

        // 按 averageRelatedCount 从小到大排序
        result.sort(Comparator.comparingDouble(obj -> (double) ((Map<String, Object>) obj).get("averageRelatedCount")));

        // 封装到最外层的 Map
        resultMap.put("related-popularity", result);

        // 返回整体的 Map
        return resultMap;
    }


}
