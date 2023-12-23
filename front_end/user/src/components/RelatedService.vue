<template>
  <div class="related-topic">
    <div class="head">
      <h2>
        The visualization of related-service
      </h2>

      <div class="input">
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
      <div class="input">
              <el-input
                  placeholder="Please input the related value"
                  v-model="updateValue"
                  clearable
                  :style="{ width: '300px' }"
              >
              </el-input>
              <el-button
                  type="primary"
                  @click="update">
                go
              </el-button>
      </div>
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
import tippy from "tippy.js";

export default {
  name: "RelatedService",
  mounted() {
    this.fetchRelatedData()
  },
  data() {
    return {
      phase: 'spring',
      relatedData: [],
      allBarChartInfo: {
        marginLeft: '',
        marginRight: '',
        marginTop: '',
        height: '',
        width: '',
      },
      allBarChart: '',
      cloudChart: '',
      updateValue: '',
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
      const data = this.relatedData;

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
          .duration(1500) // 过渡时间
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


      const zoom = d3.zoom()
          .scaleExtent([1, 80])
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
          });
      svg.call((svg) => {

        // 设置初始放大倍数为2，可以根据需要调整
        const initialTransform = d3.zoomIdentity.scale(data.length / 100 * 6).translate(-33 - data.length / 100, 0);
        // 在svg上调用缩放行为，并设置初始放大倍数
        svg.call(zoom.transform, initialTransform).call(zoom);

        svg.select('.bars')
            .selectAll('rect')
            .on('mouseenter', function (event, d) {
              tippy(this, {
                content: `TagName: ${d.tagName}<br>Average Related Count: ${d.averageRelatedCount}`,
                allowHTML: true,
              }).show();
            })
            .on('mouseleave', function () {
              tippy(this).hide();
            })
            .on('click', (event, d) => {
              this.updateCloud(d.tagName)
            })
      });

      return Object.assign(svg.node(),{
        zoomToBar(tagName) {
          const index = data.findIndex(d => d.tagName === tagName);
          const xValue = x(data[index].tagName);
          // const translateX = width / 2 - xValue - x.bandwidth() / 2;
          const zoomTransform = d3.zoomIdentity.scale(80).translate(- xValue + width / 2 / 80 - 0.5*x.bandwidth(), 0);
          svg.transition().duration(1000).call(zoom.transform, zoomTransform);

        }
          }
      )

    },
    drawWordCloud() {
      d3.select(this.$refs.wordCloud).html('');
      const data = this.relatedData;

      data.forEach((d) => {
        if (d.averageRelatedCount >= 0.5) {
          d.fontSize = d.averageRelatedCount * 300 + "px";
        } else if (d.averageRelatedCount >= 0.1) {
          d.fontSize = d.averageRelatedCount * 600 + "px";
        } else {
          d.fontSize = d.averageRelatedCount * 600 + "px";
        }

      });
      var color = d3.scaleOrdinal(d3.schemeCategory10);

      // 创建缩放行为
      const zoom = d3.zoom()
          .scaleExtent([0.1, 10]); // 设置缩放范围


      const svg = d3.select(this.$refs.wordCloud).append("svg");

      // 创建词云
      var layout = cloud()
          .size([700, 500])
          .words(data)
          .rotate(0)
          .fontSize(function (d) {
            if (d.averageRelatedCount >= 0.5) {
              return d.averageRelatedCount * 100;
            } else if (d.averageRelatedCount >= 0.1) {
              return d.averageRelatedCount * 400;
            } else {
              return d.averageRelatedCount * 1000;
            }
          })
          .on("end", (words => {
            const g = svg.append("g")
                .attr("transform", "translate(" + layout.size()[0] / 2 + "," + layout.size()[1] / 2 + ")");
            g.selectAll("text")
                .data(words)
                .enter().append("text")
                .style("fill", function (d, i) {
                  return color(i);
                }) // 颜色
                .style("font-size", function (d) {
                  return d.fontSize
                })
                .text(function (d) {
                  return d.tagName;
                })
                .on('mouseenter', function (event, d) {
                  // 显示相关信息
                  tippy(this, {
                    content: `TagName: ${d.tagName}<br>Average Related Count: ${d.averageRelatedCount}`,
                    allowHTML: true,
                  }).show();
                })
                .on('mouseleave', function () {
                  // 隐藏信息
                  tippy(this).hide();
                })
                .on('click', (event, d) => {
                 this.updateBar(d.tagName)
                })
                .transition()
                .duration(1500) // 设置过渡时间
                .attr("transform", function (d) {
                  return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                })
                .style("opacity", 1);

              svg.attr("width", layout.size()[0]) // 宽度
                .attr("height", layout.size()[1]) // 高度
                .attr("viewBox", "0 0 700 500") // 可见区域
                .attr("style", "border: 1px  black") // 区域样式
                .attr("preserveAspectRatio", "xMaxYMax meet")
                .attr("class", "wordcloud")
                .call(zoom); // 将缩放行为应用到 SVG

            let i = 0;
            zoom.on("zoom", (event) => {
              const zoomCenter = [layout.size()[0] / 2, layout.size()[1] / 2];
              i++;
              // 如果缩放中心发生变化，重新设置缩放中心
              if (i === 1) {
                const newTransform = d3.zoomIdentity.translate(zoomCenter[0], zoomCenter[1]).scale(event.transform.k);
                svg.call(zoom.transform, newTransform);
              }

              g.attr("transform", event.transform);
            });
          }));
      layout.start();

      return Object.assign(svg.node(),{
          zoomToCloud(tagName) {
            console.log(tagName)
            const index = data.findIndex(d => d.tagName === tagName);
            const xValue = layout.words()[index].x;
            const yValue = layout.words()[index].y;
            console.log(layout.words()[index])
            const zoomTransform = d3.zoomIdentity.translate(-xValue + layout.size()[0] / 2 /2  , -yValue + layout.size()[0] / 2/2 ).scale(2);
            svg.transition().duration(1000).call(zoom.transform, zoomTransform);
            }
          }
      )
    },

    update() {
      this.updateBar(this.updateValue)
      this.updateCloud(this.updateValue)
    },
    updateBar(tagName) {
      if (tagName !== '' && tagName !== this.phase && this.relatedData.findIndex(d => d.tagName === tagName) !== -1) {
        this.allBarChart.zoomToBar(tagName)
      }

    },

    updateCloud(tagName) {
      this.cloudChart.zoomToCloud(tagName)
    },
    fetchRelatedData() {
      if (this.phase === '') return;
      axios.get(`http://localhost:8090/related-topic/${this.phase}`)
          .then(response => {
            this.relatedData = response.data.relatedPopularity;
            this.allBarChart = this.drawAllBarChart()
            this.cloudChart = this.drawWordCloud()
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

.input {
  display: flex;
}
</style>