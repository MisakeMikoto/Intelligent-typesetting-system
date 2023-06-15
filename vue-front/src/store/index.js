import Vue from 'vue'
import Vuex from 'vuex'
import router, { resetRouter } from "@/router";

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    currentPathName: ''
  },
  mutations: {
    setPath (state) {
      state.currentPathName = localStorage.getItem("currentPathName")
    },
    logout () {
      //清空缓存
      localStorage.removeItem("user")
      localStorage.removeItem("menus")
      localStorage.removeItem("currentPathName")
      //返回登录页面
      router.push("/login")

      // 重置路由
      resetRouter()
    }
  }
})

export default store
