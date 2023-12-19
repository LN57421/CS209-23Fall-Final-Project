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
      axios.get('http://localhost:8080/topic-popularity/')
          .then(response => {
              console.log(response.data)
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