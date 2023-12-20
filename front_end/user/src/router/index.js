import Vue from 'vue'
import VueRouter from 'vue-router'
import TopicPopularity from "@/components/TopicPopularity.vue";
import BugPopularity from "@/components/BugPopularity.vue";
import UserView from "@/views/UserView.vue";
import RelatedService from "@/components/RelatedService.vue";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        redirect: '/visualization',


    },
    {
        path: '/visualization',
        redirect: '/visualization/TopicPopularity',
        children: [
            {path: '/visualization/TopicPopularity', name: 'TopicPopularity',component: TopicPopularity},
            {path: '/visualization/BugPopularity', name: 'BugPopularity', component: BugPopularity},
            {path: '/visualization/RelatedService', name: 'RelatedService', component: RelatedService}
        ],
        component: UserView,
    },

    ]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})
export default router
