import { createRouter, createWebHistory} from "vue-router";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {path: '/', redirect: '/login'},
        {
            path : '/manager', name:'manager',component: ()=>import('../views/manager.vue'),
            children:[
                {path : '/manager/admin',meta: {name:"管理员信息"}, name:'home',component: ()=>import('../views/Admin.vue'),},
                {path : '/manager/about',meta: {name:"关于"}, name:'about',component: ()=>import('../views/About.vue'),},
                {path : '/manager/Welcome',meta: {name:"欢迎"}, name:'welcome',component: ()=>import('../views/Welcome.vue'),},
                {path : '/manager/user',meta: {name:"用户信息"}, name:'user',component: ()=>import('../views/User.vue'),},
            ],
        },
        {
            path : '/guest', name:'guest',component: ()=>import('../views/User/Guest.vue'),
            children:[
                {path : '/guest/Welcome',meta: {name:"欢迎"}, name:'welcome_user',component: ()=>import('../views/User/Welcome.vue'),},
                {path : '/guest/about',meta: {name:"关于"}, name:'about_user',component: ()=>import('../views/User/About.vue'),},
            ]
        },
        {path : '/notFound', name:'notfound',component: ()=> import('../views/404.vue'),},
        {path : '/login', name:'login',component: ()=>import('../views/Login.vue'),},
        {path : '/register', name:'register',component: ()=>import('../views/Register.vue'),},
        {path : '/:pathMatch(.*)*', redirect: '/notFound'}],
})

export default router