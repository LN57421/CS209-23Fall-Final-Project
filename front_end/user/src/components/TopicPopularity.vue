<template>
  <div>
    hello world
  </div>
</template>

<script>
import axios from "axios";
export default {
  mounted() {
    this.fetchTopicPopularityData();
  },
  data() {
    return {

    }
  },
  methods: {
    fetchTopicPopularityData() {
      // const topics = ["sql", "jpa"]
      axios.get(`http://localhost:8090/api/answers/all`)
          .then(response => {
              console.log(response)
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