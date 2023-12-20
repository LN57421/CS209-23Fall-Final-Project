import Vue from 'vue'
import App from './App.vue'
import router from "./router";
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import VueSax from 'vuesax'
import 'vuesax/dist/vuesax.css'
import 'bootstrap-icons/font/bootstrap-icons.css';

Vue.config.productionTip = false
Vue.use(Element)
Vue.use(VueSax)
new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
