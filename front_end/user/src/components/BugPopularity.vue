<template>
  <div class="bug">

    <div class="head">
      <h2>
        The visualization of bug/exception popularity
      </h2>
    </div>

    <div class="chart">
      <div ref="icicleChartContainer"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
// import * as d3 from  "d3";
export default {
  data() {
    return {
      bugsData: '',
    }
  },
  mounted() {
    this.fetchAllExceptionPopularity();
  },

  methods: {
    fetchAllExceptionPopularity() {
      axios.get('http://localhost:8090/bug-show/exception/all')
          .then(reponse => {
            this.bugsData = reponse.data;
            console.log(this.bugsData)
          })
          .catch(error => {
            console.log(error)
          })
    },
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
  height: 200px;
}
</style>