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
      <div ref="allBarChartContainer">
      </div>
    </div>
  </div>
</template>


<script>
import axios from "axios";
import * as d3 from "d3"

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
      const width = 1000;
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
          .attr("y", d => y(d.averageRelatedCount))
          .attr("height", d => y(0) - y(d.averageRelatedCount))
          .attr("width", x.bandwidth())
          .style("fill", d => colorScale(d.averageRelatedCount));

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

      // const initialTransform = d3.zoomIdentity.scale(12); // 这里的 12 是初始缩放的倍数，可以根据需求调整
      //
      // // 调用缩放，并设置初始变换
      // svg.call(
      //     d3.zoom()
      //         .scaleExtent([1, 32])
      //         .translateExtent(extent)
      //         .extent(extent)
      //         .on("zoom", (event) => {
      //           const x = d3.scaleBand()
      //               .domain(d3.sort(data, d => -d.averageRelatedCount).map(d => d.tagName))
      //               .range([marginLeft, width - marginRight].map(d => event.transform.applyX(d)))
      //               .padding(0.05);
      //
      //           svg.select(".bars")
      //               .selectAll("rect")
      //               .attr("x", d => x(d.tagName))
      //               .attr("width", x.bandwidth());
      //
      //           svg.select(".x-axis").call(d3.axisBottom(x));
      //         })
      // ).call((zoom) => zoom.transform, initialTransform);

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
    fetchRelatedData() {
      if (this.phase === '') return;
      axios.get(`http://localhost:8090/related-topic/${this.phase}`)
          .then(response => {
            this.relatedData = response.data.relatedPopularity;
            this.drawAllBarChart()
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
</style>