<template>
  <div>
    <div class="scoreBarChart">
      <div>
        <el-select v-model="scoreValue" placeholder="please select the order" @change="handleSelection">
          <el-option
              v-for="item in scoreOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div>
        <div ref="scoreBarChartContainer"></div>
      </div>
    </div>
    <div class="viewBarChart">
      <div ref="viewBarChartContainer">
      </div>

    </div>
  </div>
</template>

<script>
import axios from "axios";
import * as d3 from "d3";

export default {
  name: "topicPopularity",
  mounted() {
    this.fetchTopicPopularityData();
  },
  data() {
    return {
      topicsData: [],
      scoreValue: '',
      scoreOptions: [
        {value: "-1", label: "Alphabetical order of keyword"},
        {value: "0", label: "Ascending by TotalScore"},
        {value: "1", label: "Descending by TotalScore"},
        {value: "2", label: "Ascending by AverageScore"},
        {value: "3", label: "Descending by AverageScore"},
        {value: "4", label: "Ascending by AverageValuableAnswerScore"},
        {value: "5", label: "Descending by AverageValuableAnswerScore"},
      ],
      scoreBarChart: '',
      viewValue: '',
      viewOption: [],
      viewBarChart: '',
    }
  },
  methods: {
    drawscoreBarChart() {
      const width = 640;
      const height = 700;
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
          .domain(data.map(d => d.keyword))
          .range([height - marginBottom, marginTop])
          .padding(0.1);

      const xAxis = d3.axisTop(x).tickSizeOuter(0);
      const yAxis = d3.axisLeft(y).tickSizeOuter(0);

      // Create the SVG container.
      const svg = d3.select(this.$refs.scoreBarChartContainer)
          .append("svg")
          .attr("viewBox", [0, 0, width, height])
          .attr("style", `max-width: ${width}px; height: auto; font: 10px sans-serif; overflow: visible;`);

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
          .attr("width", d => x(d.averageScore) - x(0));


      const averageValuableAnswerScoreBar = svg.append("g")
          .attr("fill", "steelblue")
          .selectAll("rect")
          .data(data)
          .join("rect")
          .style("mix-blend-mode", "multiply")
          .attr("x", d => x(-d.averageValuableAnswerScore))
          .attr("y", d => y(d.keyword))
          .attr("height", y.bandwidth())
          .attr("width", d => x(0) - x(-d.averageValuableAnswerScore));



      // Create the axes.
      svg.append("g")
          .attr("transform", `translate(0,${marginTop})`)
          .call(xAxis);

      const gy = svg.append("g")
          .attr("transform", `translate(${marginLeft},0)`)
          .call(yAxis);

      return Object.assign(svg.node(), {
        update(order) {
          console.log(data)
          console.log(data.sort(order).map(d => d.keyword))
          y.domain(data.sort(order).map(d => d.keyword));

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
    setToolTips() {

    },
    drawViewBarChart() {

    },
    handleSelection() {
      let order = ''
      switch (this.scoreValue) {
        case "-1":
          order = (a, b) => -a.keyword.localeCompare(b.keyword);
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
        default:
          break;
      }
      this.scoreBarChart.update(order);
    },
    fetchTopicPopularityData() {
      const topics = ["android", "hibernate", "jpa", "junit", "lombok", "maven", "spring", "spring-boot", "sql", "oracle"]
      axios.get(`http://localhost:8090/topic-popularity/${topics}`)
          .then(response => {
            console.log(response.data)
            this.topicsData = response.data.topics;
            this.scoreBarChart = this.drawscoreBarChart();

          })
          .catch(error => {
            console.log(error)
          })
    }
  },
}

</script>

<style scoped>



</style>