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
  base: '/front/',
  routes
})

//提供一个重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: '/front/',
    routes
  })
}

const storeMenus = [
  {
    "id": 1,
    "isDelete": 0,
    "menuName": "首页",
    "description": null,
    "path": "/home",
    "pagePath": "Home",
    "icon": "el-icon-s-home",
    "pid": null,
    "children": []
  },
  {
    "id": 21,
    "createTime": "2022-05-01 18:14:46",
    "updateTime": "2022-05-16 12:16:45",
    "isDelete": 0,
    "menuName": "用户管理",
    "description": null,
    "path": "/user",
    "pagePath": "User",
    "icon": "el-icon-user",
    "pid": 2, "children": null
  },
  {
    "id": 23,
    "createTime": "2022-05-06 08:42:44",
    "updateTime": "2022-05-16 12:16:48",
    "isDelete": 0,
    "menuName": "班次管理",
    "description": null,
    "path": "/shift",
    "pagePath": "Shift",
    "icon": "el-icon-s-order",
    "pid": 2,
    "children": null
  },
  {
    "id": 24,
    "createTime": "2022-05-08 11:55:36",
    "updateTime": "2022-05-16 12:16:50",
    "isDelete": 0, "menuName": "排班管理",
    "description": null, "path": "/schedule",
    "pagePath": "Schedule",
    "icon": "el-icon-date",
    "pid": 2,
    "children": null
  },
  {
    "id": 25,
    "createTime": "2022-05-13 08:29:16",
    "updateTime": "2022-05-16 12:16:55",
    "isDelete": 0,
    "menuName": "请假管理",
    "description": null,
    "path": "/leave",
    "pagePath": "Leave",
    "icon": "el-icon-time",
    "pid": 2,
    "children": null
  },
  {
    "id": 26,
    "createTime": "2022-05-16 12:18:27",
    "updateTime": "2022-05-16 13:15:57",
    "isDelete": 0,
    "menuName": "换班管理",
    "description": null,
    "path": "/changeShift",
    "pagePath": "ChangeShift",
    "icon": "el-icon-refresh",
    "pid": 2,
    "children": null
  },
  {
    "id": 27,
    "createTime": "2022-05-16 12:18:27",
    "updateTime": "2022-05-16 13:15:57",
    "isDelete": 0,
    "menuName": "期望管理",
    "description": null,
    "path": "/expectation",
    "pagePath": "Expectation",
    "icon": "el-icon-opportunity",
    "pid": 2,
    "children": null
  },
  {
    "id": 33,
    "createTime": "2022-05-01 19:11:13",
    "updateTime": "2022-05-31 14:43:07",
    "isDelete": 0,
    "menuName": "科室管理",
    "description": null,
    "path": "/department",
    "pagePath": "Department",
    "icon": "el-icon-office-building",
    "pid": 3,
    "children": null
  }
]

// 注意：刷新页面会导致页面路由重置
export const setRoutes = () => {
  //const storeMenus = localStorage.getItem("menus");
  //console.log(storeMenus)
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
      //const menus = JSON.parse(storeMenus)
      storeMenus.forEach(item => {
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
    //const storeMenus = localStorage.getItem("menus")
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
VueRouter.prototype.push = function push (to) {
  return VueRouterPush.call(this, to).catch(err => err)
}

export default router
