<template>
  <div id="detail">
    <div class="sidebar">
      <vs-sidebar
          relative
          hover-expand
          reduce
          v-model="active"
          open
      >
        <vs-sidebar-item id="intro" @click.native="handleLink('TopicPopularity')">
          <template #icon>
            <i class="bi-bar-chart"></i>
          </template>
          TopicPopularity
        </vs-sidebar-item>

        <vs-sidebar-item id="blog" @click.native="handleLink('BugPopularity')">
          <template #icon>
            <i class='bi-bug'></i>
          </template>
          BugPopularity
        </vs-sidebar-item>

        <vs-sidebar-item id="room" @click.native="handleLink('RelatedService')">
          <template #icon>
            <i class='bi-boxes'></i>
          </template>
          RelatedService
        </vs-sidebar-item>

      </vs-sidebar>
    </div>
  </div>
</template>

<script>
export default {
  components: {

  },
  data:() => ({
    active: 'TopicPopularity',
  }),
  methods: {
    handleLink(link){
      console.log(link)
      this.$router.push({name:link}).catch(err=>err)
    },
  }
}
</script>

<style scoped>


</style>