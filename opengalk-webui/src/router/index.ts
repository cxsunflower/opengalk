import {createRouter, createWebHistory} from 'vue-router'
import {getAuthorityByToken, getToken} from "../utils/TokenUtil";

//路由
const routes = [
    {
        //页面布局
        path: '/',
        name: 'Layout',
        redirect: '/index',
        component: () => import('../views/mainlayout/LayoutPage.vue'),
        children: [
            {
                //首页导航
                path: '/index',
                name: 'Index',
                component: () => import('../views/main/IndexPage.vue')
            },
            {
                //个人设置
                path: '/userCenter',
                name: 'UserCenterSetting',
                component: () => import('../views/main/UserCenterPage.vue')
            },
            {
                //
                path: '/paperList',
                name: 'PaperList',
                component: () => import('../views/main/PaperListPage.vue'),
            },
        ]
    },

    {
        //页面布局
        path: '/login',
        name: 'Login',
        component: () => import('../views/LoginPage.vue')
    },
    {
        //注册
        path: '/register',
        name: 'Register',
        component: () => import('../views/RegisterPage.vue'),
    },

]

const router = createRouter({
    history: createWebHistory(),
    routes: routes,
})

let flag = 0
router.beforeEach(async (to, from, next) => {
    router.addRoute({
        path: '/:catchAll(.*)',
        name: '404',
        component: () => import('../views/components/EmptyPage.vue')
    })

    const token = getToken()

    if ((to.path === '/login' || to.path === '/register') && !token) {
        next()
    }

    if (token) {
        if (to.path === '/login') {
            next('/')
        }

        if (flag == 0) {

            router.addRoute({
                path: '/GZPaper',
                name: 'GZPaper',
                component: () => import('../views/paper/GZPaperPage.vue'),
            })

            router.addRoute({
                path: '/XCPaper',
                name: 'XCPaper',
                component: () => import('../views/paper/XCPaperPage.vue'),
            })

            const authority = getAuthorityByToken()

            if (authority <= 1) {
                router.addRoute(
                    'Layout',
                    {
                        path: '/paperManagement',
                        name: 'PaperManagement',
                        component: () => import('../views/main/PaperManagementPage.vue')
                    }
                )
                router.addRoute(
                    {
                        path: '/addGZPaper',
                        name: 'AddGZPaper',
                        component: () => import('../views/addgzpaperlayout/AddGZPaperLayoutPage.vue')
                    }
                )
                router.addRoute(
                    {
                        path: '/viewGZpaper',
                        name: 'ViewGZPaper',
                        component: () => import('../views/viewgzpaperlayout/ViewGZPaperLayoutPage.vue')
                    }
                )
            }

            if (authority == 0) {
                router.addRoute(
                    'Layout',
                    {
                        path: '/userManagement',
                        name: 'UserManagement',
                        component: () => import('../views/main/UserManagementPage.vue')
                    }
                )
                router.addRoute(
                    'Layout',
                    {
                        path: '/collegeManagement',
                        name: 'CollegeManagement',
                        component: () => import('../views/main/CollegeManagementPage.vue')
                    }
                )
            }
            flag++
            next({...to, replace: true})
        } else {
            next()
        }

    } else {
        next('/login')
    }
})

export default router;