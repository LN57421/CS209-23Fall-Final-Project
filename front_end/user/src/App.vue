<template>
  <div>
    <div class="background">
      <div ref='vantaRef' style="width: 100%; height: 100%">

      </div>
    </div>
    <router-view/>
  </div>

</template>

<script>
import GLOBE from "vanta/src/vanta.globe";
import * as THREE from "three";
export default {
  name: 'App',
  components: {

  },
  mounted() {
    this.vantaEffect = GLOBE({
      el: this.$refs.vantaRef,
      THREE: THREE,
      mouseControls: true,
      touchControls: true,
      gyroControls: false,
      minHeight: 200.00,
      minWidth: 200.00,
      scale: 1.00,
      scaleMobile: 1.00,
      color: 0xffffff,
      size: 0.6,
      backgroundColor: 0xffffff,
    })
  },
  beforeDestroy() {
    if (this.vantaEffect) {
      this.vantaEffect.destroy()
    }
  }
}
</script>

<style>
.background{
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}
</style>
