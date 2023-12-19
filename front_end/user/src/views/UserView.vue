<template>
  <div class="container">
    <div class="detail">
      <div class="sideBar">
        <SideBar></SideBar>
      </div>

      <div class="route">
        <transition name="fade" mode="out-in">
          <router-view></router-view>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
import SideBar from "@/components/SideBar.vue";
export default {
  name: 'UserView',
  components: {
    SideBar,
  }
}
</script>
<style scoped>
.container {
  width: 100%;
  height: 100%;
  overflow: auto;
  overflow-x: hidden;
  //background-image: ;
  background-repeat: no-repeat;
  background-size: cover;
}

.detail {
  display: flex;
  flex-direction: row;
  height: 100%;
}

.sideBar {
  width: 300px;
  position: fixed;
  z-index: 1000;
  top: 40%;
}


.route {
  position: relative;
  left: 5%;
  width: 95%;
  height: 100%;
}

.container{
  height: 100%;
}


</style>