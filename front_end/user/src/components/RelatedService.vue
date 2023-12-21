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
        submit
      </el-button>
    </div>

    <div class="chart">

    </div>
  </div>
</template>


<script>
import axios from "axios";

export default {
  name: "RelatedService",
  mounted() {
    this.fetchRelatedData()
  },
  data() {
    return {
      phase: 'java',
      relatedData: [],
    }
  },
  methods: {
    fetchRelatedData() {
      if (this.phase === '') return;
      axios.get(`http://localhost:8090/related-topic/${this.phase}`)
          .then(response => {
            this.relatedData = response.data.relatedPopularity;
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