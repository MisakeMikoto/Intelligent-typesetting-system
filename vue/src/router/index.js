import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
    {
        path: '/login',
        name: '登录',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/register',
        name: '注册',
        component: () => import('../views/Register.vue')
    },
    {
        path: '/404',
        name: '404',
        component: () => import('../views/404.vue')
    },
]

const router = new VueRouter({
    mode: 'history',
    base: '/background/',
    routes
})

//提供一个重置路由的方法
export const resetRouter = () => {
    router.matcher = new VueRouter({
        mode: 'history',
        base: '/background/',
        routes
    })
}

// 注意：刷新页面会导致页面路由重置
export const setRoutes = () => {
    const storeMenus = localStorage.getItem("menus");
    if (storeMenus) {
        // 获取当前的路由对象名称数组
        const currentRouteNames = router.getRoutes().map(v => v.name)
        if (!currentRouteNames.includes('Dashboard')) {
            // 拼装固定路由
            const manageRoute = {
                name: 'Dashboard',
                path: '/',
                redirect: "/home",
                component: () => import('../views/Layout.vue'),
                children: [
                    {
                        path: 'information',
                        name: '个人信息',
                        component: () => import('../views/Information.vue'),
                    },
                    {
                        path: 'myWorkload',
                        name: '工作天数',
                        component: () => import('../views/MyWorkload.vue'),
                    },
                    {
                        path: 'password',
                        name: '修改密码',
                        component: () => import('../views/Password.vue')
                    }
                ]
            }
            // 拼装动态路由
            const menus = JSON.parse(storeMenus)
            menus.forEach(item => {
                if (item.path) {  // 当且仅当path不为空的时候才去设置路由
                    let itemMenu = {
                        path: item.path.replace("/", ""),
                        name: item.menuName,
                        component: () => import('../views/' + item.pagePath + '.vue'),
                    }
                    manageRoute.children.push(itemMenu)
                } else if (item.children.length) {
                    item.children.forEach(item => {
                        if (item.path) {
                            let itemMenu = {
                                path: item.path.replace("/", ""),
                                name: item.menuName,
                                component: () => import('../views/' + item.pagePath + '.vue'),
                            }
                            manageRoute.children.push(itemMenu)
                        }
                    })
                }
            })
            // 动态添加到现在的路由对象中去
            router.addRoute(manageRoute)
        }
    }
}

// 重置就再set一次路由
setRoutes()

router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称
    store.commit("setPath")

    //未找到路由的情况
    if (!to.matched.length) {
        const storeMenus = localStorage.getItem("menus")
        if (storeMenus) {
            next("/404")
        } else {
            //跳回登录页面
            next("/login")
        }
    }
    //其他的情况都放行
    next()
})

//解决重复点击导航时出现报错
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(to) {
    return VueRouterPush.call(this, to).catch(err => err)
}

export default router
