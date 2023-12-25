<template>
  <div class="bug">

    <div class="head">
      <h2>
        The visualization of bugs popularity
      </h2>

      <p>
        <i>
          -the metrics are
          <strong data-tippy-content="Average Score per Question (Sum of scores divided by the number of questions)" class="tooltip-trigger">averageViewCount</strong>

        </i>
      </p>
    </div>

    <div class="chart">
      <div class="sun">
        <div>
          <div ref="icicleChartContainer"></div>
        </div>
      </div>

      <div class="barchart">
        <div>
          <div ref="barChartContainer">
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

export default {
  data() {
    return {
      bugsData: {
        name: "bugs", children: [
          {name: 'exceptions', children: []},
          {name: 'syntaxError', children: []},
          {name: 'fatalError', children: []},
        ]
      },
      testData: '',
    // {
    //   name: "bugs", children: [
    //   {name: "exceptions", children: [
    //       {name: "numberformatexception", value: "0.04"},
    //       {name: "unsupportedoperationexception", value: "0.046"},
    //       {name: "nullpointerexception", value: "4.244"},
    //       {name: "classcastexception", value: "10.05"},
    //       {name: "sqlexception", value: "25.946"},
    //       {name: "ioexception", value: "46.756"},
    //       {name: "illegalargumentexception", value: "80.854"},
    //       {name: "filenotfoundexception", value: "443.326"},
    //       {name: "illegalstateexception", value: "1175.128"}
    //     ]
    //   },
    //   {name: "syntaxError", children: [
    //       {name: "syntaxerror", value: "0.112"},
    //       {name: "complieerror", value: "3187.256"}
    //     ]
    //   },
    //   {name: "fatalError", children: [
    //       {name: "unexceptederror", value: "0.098"},
    //       {name: "fatalerror", value: "0.352"},
    //       {name: "criticalerror", value: "48.42"}
    //     ]}
    // ]
    // }
      exceptionsData: '',
      fatalErrorsData: '',
      syntaxErrorsData: '',
      barCharData: '',
      basicData: '',
    }
  },
  mounted() {
    this.fetchData()
    this.addIntroToolTipTrigger()
  },

  methods: {
    fetchAllBugsPopularity() {
      //   获取三个bug大类对应的热度
      axios.get('http://localhost:8090/bug-show/all')
          .then(response => {
           this.barCharData = response.data.ThreeClassFrequencySum
            this.basicData = response.data.ThreeClassFrequencySum
            this.drawBarChartSum()
          })
          .catch(error => {
            console.log(error)
          })
    },

    fetchAllExceptionPopularity() {
      // 获取所有exception对应的热度
      axios.get('http://localhost:8090/bug-show/exception/all')
          .then(reponse => {
            this.exceptionsData = reponse.data.exceptionFrequency;
            console.log(this.exceptionsData)
            for (const data of this.exceptionsData) {
              this.bugsData.children[0].children.push({
                name: data.exceptionName,
                value: data.averageViewCount
              })
            }
            console.log(this.exceptionsData)
          })
          .catch(error => {
            console.log(error)
          })
    },

    fetchAllFatalErrorPopularity() {
      axios.get('http://localhost:8090/bug-show/fatal/all')
          .then(response => {
            this.fatalErrorsData = response.data.fatalErrorFrequency;
            for (const data of this.fatalErrorsData) {
              this.bugsData.children[1].children.push({
                name: data.exceptionName,
                value: data.averageViewCount
              })
            }
            console.log(this.fatalErrorsData)
          })
          .catch(error => {
            console.log(error)
          })
    },

    fetchAllSyntaxErrorPopularity() {
      axios.get('http://localhost:8090/bug-show/syntax/all')
          .then(response => {
            this.syntaxErrorsData = response.data.syntaxErrorFrequency;
            for (const data of this.syntaxErrorsData) {
              this.bugsData.children[2].children.push({
                name: data.exceptionName,
                value: data.averageViewCount
              })
            }
            console.log(this.syntaxErrorsData)
          })
          .catch(error => {
            console.log(error)
          })

    },

    fetchSingleExceptionPopularity() {
      // 获取某个exceptionName对应的热度
      axios.get('http://localhost:8090/bug-show/exception/"kkk"')
          .then(response => {
            console.log(response.data);
          })
          .catch(error => {
            console.log(error)
          })
    },

    fetchSingleSyntaxErrorPopularity() {
      // 获取某个syntaxError对应的热度
      axios.get('http://localhost:8090/bug-show/syntax/"kkk"')
          .then(response => {
            console.log(response.data);
          })
          .catch(error => {
            console.log(error)
          })
    },

    fetchFatalSyntaxErrorPopularity() {
      // 获取某个fatalError对应的热度
      axios.get('http://localhost:8090/bug-show/fatal/"kkk"')
          .then(response => {
            console.log(response.data);
          })
          .catch(error => {
            console.log(error)
          })
    },

    fetchAllPopularity(vm) {
      // 获取某个fatalError对应的热度
      axios.get('http://localhost:8090/bug-show/all/Detail')
          .then(response => {
            this.testData = response.data.bugPopularityDetails[0]
            this.drawCircleChart(vm)
          })
          .catch(error => {
            console.log(error)
          })
    },


    drawCircleChart(vm) {
      const data = this.testData
      console.log(data)
      const width = 600;
      const height = 600;
      const radius = width / 6;

      // Create the color scale.
      const color = d3.scaleOrdinal(d3.quantize(d3.interpolateRainbow, data.children.length + 1));

      // Compute the layout.
      const hierarchy = d3.hierarchy(data)
          .sum(d => d.value)
          .sort((a, b) => b.value - a.value);
      const root = d3.partition()
          .size([2 * Math.PI, hierarchy.height + 1])(hierarchy);

      root.each(d => d.current = d);

      // Create the arc generator.
      const arc = d3.arc()
          .startAngle(d => d.x0)
          .endAngle(d => d.x1)
          .padAngle(d => Math.min((d.x1 - d.x0) / 2, 0.005))
          .padRadius(radius * 1.5)
          .innerRadius(d => d.y0 * radius)
          .outerRadius(d => Math.max(d.y0 * radius, d.y1 * radius - 1))

      // Create the SVG container.
      const svg = d3.select(this.$refs.icicleChartContainer)
          .append("svg")
          .attr("viewBox", [-width / 2, -height / 2, width, width])
          .style("font", "10px sans-serif");

      // Append the arcs.
      const path = svg.append("g")
          .selectAll("path")
          .data(root.descendants().slice(1))
          .join("path")
          .attr("fill", d => {
            while (d.depth > 1) d = d.parent;
            return color(d.data.name);
          })
          .attr("fill-opacity", 0)  // Set initial opacity to 0
          .attr("pointer-events", "none")  // Set initial pointer events to none
          .attr("d", d => arc(d.current));

      path.transition()
          .duration(1500)  // Set the transition duration
          .attr("fill-opacity", d => arcVisible(d.current) ? (d.children ? 0.6 : 0.4) : 0)
          .attr("pointer-events", d => arcVisible(d.current) ? "auto" : "none")
          .attrTween("d", d => () => arc(d.current));
      // Make them clickable if they have children.
      path.filter(d => d.children)
          .style("cursor", "pointer")
          .on("click", clicked);

      const format = d3.format(",d");
      path.append("title")
          .text(d => `${d.ancestors().map(d => d.data.name).reverse().join("/")}\n${format(d.value)}`);

      const label = svg.append("g")
          .attr("pointer-events", "none")
          .attr("text-anchor", "middle")
          .style("user-select", "none")
          .selectAll("text")
          .data(root.descendants().slice(1))
          .join("text")
          .attr("dy", "0.35em")
          .attr("fill-opacity", d => +labelVisible(d.current))
          .attr("transform", d => labelTransform(d.current))
          .text(d => d.data.name);

      const parent = svg.append("circle")
          .datum(root)
          .attr("r", radius)
          .attr("fill", "none")
          .attr("pointer-events", "all")
          .on("click", clicked);

      // Handle zoom on click.
      function clicked(event, p) {
        console.log(p.data.name)
        vm.changeBarChart(p.data.name)
        parent.datum(p.parent || root);

        root.each(d => d.target = {
          x0: Math.max(0, Math.min(1, (d.x0 - p.x0) / (p.x1 - p.x0))) * 2 * Math.PI,
          x1: Math.max(0, Math.min(1, (d.x1 - p.x0) / (p.x1 - p.x0))) * 2 * Math.PI,
          y0: Math.max(0, d.y0 - p.depth),
          y1: Math.max(0, d.y1 - p.depth)
        });

        const t = svg.transition().duration(750);

        // Transition the data on all arcs, even the ones that aren’t visible,
        // so that if this transition is interrupted, entering arcs will start
        // the next transition from the desired position.
        path.transition(t)
            .tween("data", d => {
              const i = d3.interpolate(d.current, d.target);
              return t => d.current = i(t);
            })
            .filter(function (d) {
              return +this.getAttribute("fill-opacity") || arcVisible(d.target);
            })
            .attr("fill-opacity", d => arcVisible(d.target) ? (d.children ? 0.6 : 0.4) : 0)
            .attr("pointer-events", d => arcVisible(d.target) ? "auto" : "none")
            .attrTween("d", d => () => arc(d.current));

        label.filter(function (d) {
          return +this.getAttribute("fill-opacity") || labelVisible(d.target);
        }).transition(t)
            .attr("fill-opacity", d => +labelVisible(d.target))
            .attrTween("transform", d => () => labelTransform(d.current));
      }


      function arcVisible(d) {
        return d.y1 <= 3 && d.y0 >= 1 && d.x1 > d.x0;
      }

      function labelVisible(d) {
        return d.y1 <= 3 && d.y0 >= 1 && (d.y1 - d.y0) * (d.x1 - d.x0) > 0.03;
      }

      function labelTransform(d) {
        const x = (d.x0 + d.x1) / 2 * 180 / Math.PI;
        const y = (d.y0 + d.y1) / 2 * radius;
        return `rotate(${x - 90}) translate(${y},0) rotate(${x < 180 ? 0 : 180})`;
      }

      return svg.node();
    },

    drawBarChart(color) {
      d3.select(this.$refs.barChartContainer).html('');
      const width = 700;
      const height = 670;
      const marginTop = 20;
      const marginRight = 0;
      const marginBottom = 30;
      const marginLeft = 40;
      const data = this.barCharData

      // Create the horizontal scale and its axis generator.
      const x = d3.scaleBand()
          .domain(d3.sort(data, d => -d.averageViewCount).map(d => d.exceptionName))
          .range([marginLeft, width - marginRight])
          .padding(0.05);

      const xAxis = d3.axisBottom(x).tickSizeOuter(0);

      // Create the vertical scale.
      const y = d3.scaleLinear()
          .domain([0, d3.max(data, d => d.averageViewCount)]).nice()
          .range([height - marginBottom, marginTop]);

      // Create the SVG container and call the zoom behavior.
      const svg = d3.select(this.$refs.barChartContainer)
          .append("svg")
          .attr("viewBox", [0, 0, width, height])
          .attr("width", width)
          .attr("height", height)
          .attr("style", "max-width: 100%; height: auto;");


      let head = ''
      let colorScale = ''
      switch (color) {
        case 'yellow':
          head = 'Fatal Error'
          colorScale = d3.scaleLinear()
              .domain([0, d3.max(data, d => d.averageViewCount)])
              .range(["#fad888", "#ff9933"]); // 调整颜色范围
              break;
        case 'green':
          head = 'Exceptions'
          colorScale = d3.scaleLinear()
              .domain([0, d3.max(data, d => d.averageViewCount)])
              .range(["#66ff33", "#06a266"]); // 调整颜色范围
          break;
        case 'purple':
          head = 'Syntax Error'
          colorScale = d3.scaleLinear()
              .domain([0, d3.max(data, d => d.averageViewCount)])
              .range(["#9a7ffc", "#750de5"]); // 调整颜色范围
          break;
        default:
          break;
      }

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
          .text(`0 —— ${parseInt(d3.max(data, d => d.averageViewCount)) + 1}`);

      // Append the bars.
      svg.append("g")
          .attr("class", "bars")
          .attr("fill", "steelblue")
          .selectAll("rect")
          .data(data)
          .join("rect")
          .attr("x", d => x(d.exceptionName))
          .attr("y", y(0)) // 初始位置设置在底部
          .attr("height", 0) // 初始高度为 0
          .attr("width", x.bandwidth())
          .style("fill", d => colorScale(d.averageViewCount))
          .transition() // 添加过渡效果
          .duration(500) // 过渡时间
          .attr("y", d => y(d.averageViewCount)) // 最终位置
          .attr("height", d => y(0) - y(d.averageViewCount)); // 最终高度


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

      svg.select('.bars')
          .selectAll('rect')
          .on('mouseenter', function (event, d) {
            tippy(this, {
              content: `ExceptionName: ${d.exceptionName}<br>Average View Count: ${d.averageViewCount}`,
              allowHTML: true,
            }).show();
          })
          .on('mouseleave', function () {
            tippy(this).hide();
          })

      svg.append("text")
          .attr("x", 340) // 调整名称的水平位置
          .attr("y", 20)  // 调整名称的垂直位置
          .attr("text-anchor", "middle") // 文本锚点居中对齐
          .text(`The averageViewCount of ${head}`) // 添加名称文本
          .style("font-size", "15px")
          .style("font-style", "Times New Roman");

      const extent = [
        [marginLeft, marginTop],
        [width - marginRight, height - marginTop]
      ];

      const zoom = d3.zoom()
          .scaleExtent([1, 64])
          .translateExtent(extent)
          .extent(extent)
          .on("zoom", (event) => {
            const x = d3.scaleBand()
                .domain(d3.sort(data, d => -d.averageViewCount).map(d => d.exceptionName))
                .range([marginLeft, width - marginRight].map(d => event.transform.applyX(d)))
                .padding(0.05);

            svg.select(".bars")
                .selectAll("rect")
                .attr("x", d => x(d.exceptionName))
                .attr("width", x.bandwidth());

            svg.select(".x-axis").call(d3.axisBottom(x));
          });
      svg.call((svg) => {

        // 设置初始放大倍数为2，可以根据需要调整
        const initialTransform = d3.zoomIdentity.scale(1).translate(0, 0);
        // 在svg上调用缩放行为，并设置初始放大倍数
        svg.call(zoom.transform, initialTransform).call(zoom);
      })

          },

    drawBarChartSum() {
      d3.select(this.$refs.barChartContainer).html('');
      const width = 700;
      const height = 670;
      const marginTop = 20;
      const marginRight = 0;
      const marginBottom = 30;
      const marginLeft = 40;
      const data = this.barCharData

      // Create the horizontal scale and its axis generator.
      const x = d3.scaleBand()
          .domain(d3.sort(data, d => -d.averageViewCountSum).map(d => d.className))
          .range([marginLeft, width - marginRight])
          .padding(0.05);

      const xAxis = d3.axisBottom(x).tickSizeOuter(0);

      // Create the vertical scale.
      const y = d3.scaleLinear()
          .domain([0, d3.max(data, d => d.averageViewCountSum)]).nice()
          .range([height - marginBottom, marginTop]);

      // Create the SVG container and call the zoom behavior.
      const svg = d3.select(this.$refs.barChartContainer)
          .append("svg")
          .attr("viewBox", [0, 0, width, height])
          .attr("width", width)
          .attr("height", height)
          .attr("style", "max-width: 100%; height: auto;");


      const colorScale = d3.scaleLinear()
          .domain([0, d3.max(data, d => d.averageViewCountSum)])
          .range(["#facfcf", "#f60505"]); // 调整颜色范围

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
          .text(`0 —— ${parseInt(d3.max(data, d => d.averageViewCountSum)) + 1}`);
      // Append the bars.
      svg.append("g")
          .attr("class", "bars")
          .attr("fill", "steelblue")
          .selectAll("rect")
          .data(data)
          .join("rect")
          .attr("x", d => x(d.className))
          .attr("y", y(0)) // 初始位置设置在底部
          .attr("height", 0) // 初始高度为 0
          .attr("width", x.bandwidth())
          .style("fill", d => colorScale(d.averageViewCountSum))
          .transition() // 添加过渡效果
          .duration(500) // 过渡时间
          .attr("y", d => y(d.averageViewCountSum)) // 最终位置
          .attr("height", d => y(0) - y(d.averageViewCountSum)); // 最终高度


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

      svg.select('.bars')
          .selectAll('rect')
          .on('mouseenter', function (event, d) {
            tippy(this, {
              content: `ClassName: ${d.className}<br>Average View Count: ${d.averageViewCountSum}`,
              allowHTML: true,
            }).show();
          })
          .on('mouseleave', function () {
            tippy(this).hide();
          })

      svg.append("text")
          .attr("x", 340) // 调整名称的水平位置
          .attr("y", 20)  // 调整名称的垂直位置
          .attr("text-anchor", "middle") // 文本锚点居中对齐
          .text("The averageViewCount of bugs") // 添加名称文本
          .style("font-size", "15px")
          .style("font-style", "Times New Roman");

    },
    fetchData() {
      this.fetchAllBugsPopularity()
      this.fetchAllExceptionPopularity()
      this.fetchAllFatalErrorPopularity()
      this.fetchAllSyntaxErrorPopularity()
      const vm = this
      this.fetchAllPopularity(vm)
    },

    changeBarChart(tag) {
      let color = ''
      if (tag === 'bugs') {
        this.barCharData = this.basicData
        this.drawBarChartSum()
      } else if (tag === 'exceptions') {
        this.barCharData = this.exceptionsData
        color = 'yellow'
        this.drawBarChart(color)
      } else if (tag === 'syntaxError') {
        this.barCharData = this.syntaxErrorsData
        color = 'purple'
        this.drawBarChart(color)
      } else {
        this.barCharData = this.fatalErrorsData
        color = 'green'
        this.drawBarChart(color)
      }
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
  }
}
</script>

<style scoped>
.bug {
  display: block;
}

.head {
  display: grid;
  place-items: center;
  height: 110px;
}


.chart {
  display: flex;
  gap: 10px;
}
.sun {
  width: 48%;
  border-radius: 4px;
  border: 1px solid #919292;
}

.barchart {
  width: 50%;
  border-radius: 4px;
  border: 1px solid #919292;
}

</style>