<template>
  <div class="bug">

    <div class="head">
      <h2>
        The visualization of bug/exception popularity
      </h2>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  mounted() {
    this.fetchAllExceptionPopularity();
  },

  methods: {
    fetchAllExceptionPopularity() {
      axios.get('http://localhost:8090/bug-show/exception/all')
          .then(reponse => {
            console.log(reponse.data)
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