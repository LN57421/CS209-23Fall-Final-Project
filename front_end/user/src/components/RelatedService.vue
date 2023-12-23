<template>
  <div class="related-topic">
    <div class="head">
      <h2>
        The visualization of related-service
      </h2>

      <el-input
          placeholder="Please input the phase"
          v-model="phase"
          clearable
          :style="{ width: '300px' }"
      >
      </el-input>
      <el-button
          type="primary"
          @click="fetchRelatedData">
        go
      </el-button>
    </div>

    <div class="chart">
      <div class="allBarChart">
        <div ref="allBarChartContainer">
        </div>
      </div>

      <div class="wordCloud">
        <div ref="wordCloud">
        </div>
      </div>


    </div>
  </div>
</template>


<script>
import axios from "axios";
import * as d3 from "d3"
import * as cloud from "d3-cloud"

export default {
  name: "RelatedService",
  mounted() {
    this.fetchRelatedData()
  },
  data() {
    return {
      phase: 'spring boot',
      relatedData: [],
      allBarChartInfo: {
        marginLeft: '',
        marginRight: '',
        marginTop: '',
        height: '',
        width: ''
      },
    }
  },
  methods: {
    drawAllBarChart() {
      d3.select(this.$refs.allBarChartContainer).html('');
      const width = 700;
      const height = 500;
      const marginTop = 20;
      const marginRight = 0;
      const marginBottom = 30;
      const marginLeft = 40;

      this.allBarChartInfo.marginLeft = marginLeft
      this.allBarChartInfo.marginRight = marginRight
      this.allBarChartInfo.marginTop = marginTop
      this.allBarChartInfo.height = height
      this.allBarChartInfo.width = width
      const data = this.relatedData

      // Create the horizontal scale and its axis generator.
      const x = d3.scaleBand()
          .domain(d3.sort(data, d => -d.averageRelatedCount).map(d => d.tagName
          ))
          .range([marginLeft, width - marginRight])
          .padding(0.05);

      const xAxis = d3.axisBottom(x).tickSizeOuter(0);

      // Create the vertical scale.
      const y = d3.scaleLinear()
          .domain([0, d3.max(data, d => d.averageRelatedCount)]).nice()
          .range([height - marginBottom, marginTop]);

      const extent = [
        [marginLeft, marginTop],
        [width - marginRight, height - marginTop]
      ];
      // Create the SVG container and call the zoom behavior.
      const svg = d3.select(this.$refs.allBarChartContainer)
          .append("svg")
          .attr("viewBox", [0, 0, width, height])
          .attr("width", width)
          .attr("height", height)
          .attr("style", "max-width: 100%; height: auto;");


      const colorScale = d3.scaleLinear()
          .domain([0, d3.max(data, d => d.averageRelatedCount)])
          .range(["#facfcf", "#f60505"]); // 调整颜色范围
      // Append the bars.
      svg.append("g")
          .attr("class", "bars")
          .attr("fill", "steelblue")
          .selectAll("rect")
          .data(data)
          .join("rect")
          .attr("x", d => x(d.tagName))
          .attr("y", y(0)) // 初始位置设置在底部
          .attr("height", 0) // 初始高度为 0
          .attr("width", x.bandwidth())
          .style("fill", d => colorScale(d.averageRelatedCount))
          .transition() // 添加过渡效果
          .duration(2000) // 过渡时间
          .attr("y", d => y(d.averageRelatedCount)) // 最终位置
          .attr("height", d => y(0) - y(d.averageRelatedCount)); // 最终高度


      // Append the axes.
      svg.append("g")
          .attr("class", "x-axis")
          .attr("transform", `translate(0,${height - marginBottom})`)
          .call(xAxis);

      svg.append("g")
          .attr("class", "y-axis")
          .attr("transform", `translate(${marginLeft},0)`)
          .call(d3.axisLeft(y))
          .call(g => g.select(".domain").remove());

      svg.call((svg) => {
        svg.call(
            d3.zoom()
                .scaleExtent([1, 32])
                .translateExtent(extent)
                .extent(extent)
                .on("zoom", (event) => {
                  const x = d3.scaleBand()
                      .domain(d3.sort(data, d => -d.averageRelatedCount).map(d => d.tagName))
                      .range([marginLeft, width - marginRight].map(d => event.transform.applyX(d)))
                      .padding(0.05);

                  svg.select(".bars")
                      .selectAll("rect")
                      .attr("x", d => x(d.tagName))
                      .attr("width", x.bandwidth());

                  svg.select(".x-axis").call(d3.axisBottom(x));
                })
        );


      });
    },
    drawWordCloud() {
      d3.select(this.$refs.wordCloud).html('');
      const data = this.relatedData;
      var color = d3.scaleOrdinal(d3.schemeCategory10);

      // 创建缩放行为
      const zoom = d3.zoom()
          .scaleExtent([1, 10]); // 设置缩放范围

      // 创建词云
      var layout = cloud()
          .size([700, 500])
          .words(data)
          .rotate(0)
          .fontSize(function(d) { return d.averageRelatedCount * 1000; })
          .on("end", (words => {
            const svg = d3.select(this.$refs.wordCloud).append("svg")
                .attr("width", layout.size()[0])//宽度
                .attr("height", layout.size()[1])//高度
                .attr("viewBox", "0 0 700 500")//可见区域
                .attr("style", "border: 1px  black")//区域样式
                .attr("preserveAspectRatio", "xMaxYMax meet")
                .attr("class", "wordcloud")
                .call(zoom); // 将缩放行为应用到 SVG

            const g = svg.append("g")
                .attr("transform", "translate(350,250)");

            g.selectAll("text")
                .data(words)
                .enter().append("text")
                .style("font-size", function(d) { return d.averageRelatedCount * 400 + "px"; })
                .style("fill", function(d, i) { return color(i); })//颜色
                .attr("transform", function(d) {//每个词条的初始偏移量在外面
                  return "translate(" + [d.x, -layout.size()[1]] + ")rotate(" + d.rotate + ")";
                })
                .text(function(d) { return d.tagName; })
                .transition()
                .duration(2000) // 设置过渡时间
                .attr("transform", function(d) {
                  return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                })
                .style("opacity", 1); // 逐渐显示文本;//内容
            zoom.on("zoom", (event)=>{
              g.attr("transform", event.transform);
            });
          }));
      layout.start();

      // 缩放回调函数
    },

    fetchRelatedData() {
      if (this.phase === '') return;
      axios.get(`http://localhost:8090/related-topic/${this.phase}`)
          .then(response => {
            this.relatedData = response.data.relatedPopularity;
            this.drawAllBarChart()
            this.drawWordCloud()
            console.log(response.data.relatedPopularity)
          })
          .catch(error => {
            console.log(error)
          })
    },
  }
}

</script>

<style scoped>
.related-topic {
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

.allBarChart {
  width: 50%;
  border-radius: 4px;
  border: 1px solid #919292;
}

.wordCloud {
  width: 50%;
  border-radius: 4px;
  border: 1px solid #919292;
}
</style>