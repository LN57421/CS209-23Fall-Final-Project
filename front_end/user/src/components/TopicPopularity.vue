<template>
  <div class="topic">
    <div class="head">
      <h2>
        The visualization of topic popularity
      </h2>
      <p>
        <i>
          -the metrics are
          <strong data-tippy-content="Average Score per Question (Sum of scores divided by the number of questions)" class="tooltip-trigger">AverageScore</strong>,
          <strong data-tippy-content="Average Upvotes for Valuable Answered Questions (Sum of scores for valuable answers divided by the number of answers)
" class="tooltip-trigger">AverageValuableAnswerScore</strong>,
          <strong data-tippy-content="Average View Count per Question (Sum of view_count divided by the number of questions)"
                  class="tooltip-trigger">AverageViewCount</strong>
        </i>
      </p>
      <div>
        <el-select v-model="scoreValue" placeholder="please select the order" @change="handleScoreOrderSelection">
          <el-option
              v-for="item in scoreOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </div>
    </div>
    <div class="chart">
      <div class="scoreBarChart">
        <div>
          <div ref="scoreBarChartContainer"></div>
        </div>
      </div>

      <div class="viewBarChart">
        <div>
          <div ref="viewBarChartContainer">
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import * as d3 from "d3";
import tippy from "tippy.js";
// import * as zoom from "d3-zoom"

export default {
  name: "topicPopularity",
  mounted() {
    this.fetchTopicPopularityData();
    this.addIntroToolTipTrigger();
  },
  data() {
    return {
      topicsData: [],
      scoreValue: '6',
      scoreOptions: [
        {value: "-1", label: "Alphabetical order of keyword"},
        {value: "0", label: "Ascending by TotalScore"},
        {value: "1", label: "Descending by TotalScore"},
        {value: "2", label: "Ascending by AverageScore"},
        {value: "3", label: "Descending by AverageScore"},
        {value: "4", label: "Ascending by AverageValuableAnswerScore"},
        {value: "5", label: "Descending by AverageValuableAnswerScore"},
        {value: "6", label: "Ascending by AverageViewCount"},
        {value: "7", label: "Descending by AverageViewCount"},
      ],
      scoreBarchart: '',
      viewBarChart: '',
      durationTime: 1000,
      introductionOfMetric: {
        AverageScore: "AverageScore is AverageScore",
        AverageValuableAnswerScore: "AverageValuableAnswerScore is AverageValuableAnswerScore",
        AverageViewCount: "AverageViewCount is AverageViewCount",
      }
    }
  },
  methods: {
    drawScoreBarChart() {
      const width = 640;
      const height = 550;
      const marginTop = 20;
      const marginRight = 0;
      const marginBottom = 30;
      const marginLeft = 40;
      const data = this.topicsData;

      // Declare the x (horizontal position) scale and the corresponding axis generator.
      const x = d3.scaleLinear()
          .domain([-d3.max(data, d => d.averageValuableAnswerScore), d3.max(data, d => d.averageScore)]).nice()
          .range([marginLeft, width - marginRight]);

      // Declare th;ppe y (vertical position) scale.
      const y = d3.scaleBand()
          .domain(data.map(d => d.keyword).reverse())
          .range([height - marginBottom, marginTop])
          .padding(0.1);

      const xAxis = d3.axisTop(x).tickSizeOuter(0);
      const yAxis = d3.axisLeft(y).tickSizeOuter(0);

      // Create the SVG container.
      const svg = d3.select(this.$refs.scoreBarChartContainer)
          .append("svg")
          .attr("viewBox", [0, 0, width, height])
          .attr("style", `max-width: ${width}px; height: auto; font: 10px sans-serif; overflow: visible;`);

      svg.append("text")
          .attr("x", 340) // 调整名称的水平位置
          .attr("y", 540)  // 调整名称的垂直位置
          .attr("text-anchor", "middle") // 文本锚点居中对齐
          .text("The averageScore and averageValuableAnswerScore of topics") // 添加名称文本
          .style("font-size", "15px")
          .style("font-style", "Times New Roman");

      const skyBlue = d3.rgb(135, 206, 250);
      const averageScoreBar = svg.append("g")
          .attr("fill", skyBlue)
          .selectAll("rect")
          .data(data)
          .join("rect")
          .style("mix-blend-mode", "multiply")
          .attr("x", x(0))
          .attr("y", d => y(d.keyword))
          .attr("height", y.bandwidth())
          .attr("width", 0);

      averageScoreBar.transition()
          .duration(1000)
          .attr("x",x(0))
          .attr("width", d => x(d.averageScore) - x(0));


      const averageValuableAnswerScoreBar = svg.append("g")
          .attr("fill", "steelblue")
          .selectAll("rect")
          .data(data)
          .join("rect")
          .style("mix-blend-mode", "multiply")
          .attr("x", x(0))
          .attr("y", d => y(d.keyword))
          .attr("height", y.bandwidth())
          .attr("width", 0);

      averageValuableAnswerScoreBar.transition()
          .duration(1000)
          .attr("x", d => x(-d.averageValuableAnswerScore))
          .attr("width", d => x(0) - x(-d.averageValuableAnswerScore));

      averageScoreBar
          .on('mouseenter', function (event, d) {
            tippy(this, {
              content: `Keyword: ${d.keyword}<br>Average Score: ${d.averageScore}`,
              allowHTML: true,
            }).show();
          })
          .on('mouseleave', function () {
            tippy(this).hide();
          });

      averageValuableAnswerScoreBar
          .on('mouseenter', function (event, d) {
            tippy(this, {
              content: `Keyword: ${d.keyword}<br>Average Valuable Answer Score: ${d.averageValuableAnswerScore}`,
              allowHTML: true,
            }).show();
          })
          .on('mouseleave', function () {
            tippy(this).hide();
          });
      // Create the axes.
      svg.append("g")
          .attr("transform", `translate(0,${marginTop})`)
          .call(xAxis);

      const gy = svg.append("g")
          .attr("transform", `translate(${marginLeft},0)`)
          .call(yAxis);


      // Append the legends
      const legendOffset = 10;

      // Legend for Average Score
      const averageScoreLegend = svg.append("g")
          .attr("class", "legend")
          .attr("transform", `translate(${width - marginRight - 100 -25}, ${marginTop})`);

      averageScoreLegend.append("rect")
          .attr("width", 20)
          .attr("height", 20)
          .style("fill", skyBlue);

      averageScoreLegend.append("text")
          .attr("x", 20)
          .attr("y", 15)
          .text("Average Score");

      // Legend for Average Valuable Answer Score
      const averageValuableAnswerScoreLegend = svg.append("g")
          .attr("class", "legend")
          .attr("transform", `translate(${width - marginRight - 100 - 25}, ${marginTop + legendOffset + 25})`);

      averageValuableAnswerScoreLegend.append("rect")
          .attr("width", 20)
          .attr("height", 20)
          .style("fill", "steelblue");

      averageValuableAnswerScoreLegend.append("text")
          .attr("x", 20)
          .attr("y", 15)
          .text("Average Valuable Answer Score");

      return Object.assign(svg.node(), {
        update(order) {
          console.log(data)
          console.log(data.sort(order).map(d => d.keyword))
          y.domain(data.sort(order).map(d => d.keyword).reverse());

          const t = svg.transition()
              .duration(750);

          averageScoreBar.data(data, d => d.keyword)
              .order()
              .transition(t)
              .delay((d, i) => i * 20)
              .attr("y", d => y(d.keyword));

          averageValuableAnswerScoreBar.data(data, d => d.keyword)
              .order()
              .transition(t)
              .delay((d, i) => i * 20)
              .attr("y", d => y(d.keyword));

          gy.transition(t)
              .call(yAxis)
              .selectAll(".tick")
              .delay((d, i) => i * 20);
        }
      });

    },
    drawViewBarChart() {
      const width = 640;
      const height = 550;
      const marginTop = 20;
      const marginRight = 0;
      const marginBottom = 30;
      const marginLeft = 40;
      const data = this.topicsData;
      // Create the horizontal scale and its axis generator.
      const x = d3.scaleBand()
          .domain(data.map(d => d.keyword))
          .range([marginLeft, width - marginRight])
          .padding(0.1);

      const xAxis = d3.axisBottom(x).tickSizeOuter(0);

      // Create the vertical scale.
      const y = d3.scaleLinear()
          .domain([0, d3.max(data, d => d.averageViewCount)]).nice()
          .range([height - marginBottom, marginTop]);


      // Create the SVG container and call the zoom behavior.
      const svg = d3.select(this.$refs.viewBarChartContainer)
          .append("svg")
          .attr("viewBox", [0, 0, width, height])
          .attr("width", width)
          .attr("height", height)
          .attr("style", "max-width: 100%; height: auto;");
      // .call(zoom);

      svg.append("text")
          .attr("x", 340) // 调整名称的水平位置
          .attr("y", 20)  // 调整名称的垂直位置
          .attr("text-anchor", "middle") // 文本锚点居中对齐
          .text("The averageViewCount of topics") // 添加名称文本
          .style("font-size", "15px")
          .style("font-style", "Times New Roman");

      const colorScale = d3.scaleLinear()
          .domain([0, d3.max(data, d => d.averageViewCount)])
          .range(["#f4facf", "#71f605"]);


      const legend = svg.append("g")
          .attr("class", "legend")
          .attr("transform", `translate(${width - marginRight - 100},${marginTop})`);

      const legendGradient = legend.append("linearGradient")
          .attr("id", "legend-gradient")
          .attr("x1", "0%")
          .attr("y1", "0%")
          .attr("x2", "100%")
          .attr("y2", "0%");

      legendGradient.append("stop")
          .attr("offset", "0%")
          .style("stop-color", colorScale.range()[0]);

      legendGradient.append("stop")
          .attr("offset", "100%")
          .style("stop-color", colorScale.range()[1]);

      legend.append("rect")
          .attr("width", 120)
          .attr("height", 5)
          .style("fill", "url(#legend-gradient)");

      legend.append("text")
          .attr("x", 40)
          .attr("y", -5)
          .attr("text-anchor", "middle")
          .style("font-size", "10px")
          .text("Average View Count");

      legend.append("text")
          .attr("x", 40)
          .attr("y", 10)
          .attr("text-anchor", "middle")
          .style("font-size", "10px")
          .text("0——26000");

      // Append the bars.
      const bars = svg.append("g")
          .attr("class", "bars")
          .attr("fill", "lightgreen")
          .selectAll("rect")
          .data(data)
          .join("rect")
          .style("fill", d => colorScale(d.averageViewCount))
          .attr("x", d => x(d.keyword))
          .attr("y", y(0))
          .attr("height", 0)
          .attr("width", x.bandwidth());

      bars
          .transition() // 添加过渡效果
          .duration(1000) // 过渡时间
          .attr("y", d => y(d.averageViewCount)) // 最终高度
          .attr("height", d => y(0) - y(d.averageViewCount));

      bars
          .on('mouseenter', function (event, d) {
            tippy(this, {
              content: `Keyword: ${d.keyword}<br>Average View Count: ${d.averageViewCount}`,
              allowHTML: true,
            }).show();
          })
          .on('mouseleave', function () {
            tippy(this).hide();
          });


      // Append the axes.
      const gx = svg.append("g")
          .attr("class", "x-axis")
          .attr("transform", `translate(0,${height - marginBottom})`)
          .call(xAxis);

      svg.append("g")
          .attr("class", "y-axis")
          .attr("transform", `translate(${marginLeft},0)`)
          .call(d3.axisLeft(y))
          .call(g => g.select(".domain").remove());



      return Object.assign(svg.node(), {
        update(order) {
          console.log(data)
          console.log(data.sort(order).map(d => d.keyword))
          x.domain(data.sort(order).map(d => d.keyword));

          const t = svg.transition()
              .duration(750);

          bars.data(data, d => d.keyword)
              .order()
              .transition(t)
              .delay((d, i) => i * 20)
              .attr("x", d => x(d.keyword));

          gx.transition(t)
              .call(xAxis)
              .selectAll(".tick")
              .delay((d, i) => i * 20);
        }
      });

    },
    handleScoreOrderSelection() {
      let order = ''
      switch (this.scoreValue) {
        case "-1":
          order = (a, b) => a.keyword.localeCompare(b.keyword);
          break;
        case "0":
          order = (a, b) => (a.averageScore + a.averageValuableAnswerScore - b.averageScore - b.averageValuableAnswerScore);
          break;
        case "1":
          order = (a, b) => -(a.averageScore + a.averageValuableAnswerScore - b.averageScore - b.averageValuableAnswerScore);
          break;
        case "2":
          order = (a, b) => (a.averageScore - b.averageScore);
          break;
        case "3":
          order = (a, b) => -(a.averageScore - b.averageScore);
          break;
        case "4":
          order = (a, b) => (a.averageValuableAnswerScore - b.averageValuableAnswerScore);
          break;
        case "5":
          order = (a, b) => -(a.averageValuableAnswerScore - b.averageValuableAnswerScore);
          break;
        case "6":
          order = (a, b) => a.averageViewCount - b.averageViewCount;
          break;
        case "7":
          order = (a, b) => -(a.averageViewCount - b.averageViewCount);
          break;
        default:
          break;
      }
      this.scoreBarchart.update(order);
      this.viewBarChart.update(order)
    },
    fetchTopicPopularityData() {
      const topics = ["android", "hibernate", "jpa", "junit", "gradle", "maven", "spring", "spring-boot", "javafx", "spring-data-jpa"]
      axios.get(`http://localhost:8090/topic-popularity/sortByAverageViewCount/${topics}`)
          .then(response => {
            console.log(response.data)
            this.topicsData = response.data.topics;
            this.scoreBarchart = this.drawScoreBarChart();
            this.viewBarChart = this.drawViewBarChart();
          })
          .catch(error => {
            console.log(error)
          })
    },
    addIntroToolTipTrigger() {
      const tooltipTriggers = this.$el.querySelectorAll('.tooltip-trigger');
      tooltipTriggers.forEach(element => {
        tippy(element, {
          content: element.getAttribute('data-tippy-content'),
          arrow: true,
        });
      });
    }
  },
}

</script>

<style scoped>
.topic {
  display: block;
}

.head {
  display: grid;
  place-items: center;
  height: 200px;
}

.chart {
  display: flex;
  gap: 10px;
}

.scoreBarChart {
  width: 50%;
  border-radius: 4px;
  border: 1px solid #919292;
}

.viewBarChart {
  width: 50%;
  border-radius: 4px;
  border: 1px solid #919292;
}

strong:hover {
  color: #7f9dff;
  cursor: pointer;
}


</style>