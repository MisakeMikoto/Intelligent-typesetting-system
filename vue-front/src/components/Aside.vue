<template>
    <el-menu :default-openeds=this.open style="min-height: 100%; overflow-x: hidden;"
             background-color="rgb(255,192,203)"
             text-color="#000"
             active-text-color="#ffa64d"
             :collapse-transition="false"
             :collapse="isCollapse"
             class="el-menu-vertical-demo"
             router>

        <div style="height: 60px; line-height: 60px; ; text-align: center">
            <img src="../assets/ninghui.png" style="width: 20px; position: relative; top:5px; margin-right: 5px">
            <b style="color: white;" v-show="!isCollapse">护士排班管理系统</b>
        </div>

        <!-- 动态路由开始 -->
        <div v-for="item in menus" :key="item.id" >
            <!-- 如果有路径,为一级菜单 -->
            <div v-if="item.path" style="width: 200px">
                <el-menu-item :index="item.path" v-if="item.id != 33 || level < 2">
                    <i :class="item.icon"></i>
                    <span slot="title">{{item.menuName}}</span>
                </el-menu-item>
            </div>

            <div v-else>
                <!-- 如果没有路径，为二级菜单 -->
                <el-submenu :index="item.id+''">
                    <template slot="title">
                        <i :class="item.icon"></i>
                        <span slot="title" v-show="!isCollapse">{{item.menuName}}</span>
                    </template>
                    <!-- 二级菜单子菜单 -->
                    <div v-for="subItem in item.children" :key="subItem.id">
                        <el-menu-item :index="subItem.path">
                            <template slot="title">
                                <i :class="subItem.icon"></i>
                                <span slot="title">{{subItem.menuName}}</span>
                            </template>
                        </el-menu-item>
                    </div>
                </el-submenu>
            </div>
        </div>
        <!-- 动态路由结束 -->
    </el-menu>
</template>

<script>
  export default {
    name: "Aside",
    props: {
      isCollapse: Boolean,
    },
    data() {
      return {
        
        menus: [
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
            "icon": "el-icon-s-opportunity",
            "pid": 2,
            "children": null
          },
          {
            "id":33,
            "createTime":"2022-05-01 19:11:13",
            "updateTime":"2022-05-31 14:43:07",
            "isDelete":0,
            "menuName":"科室管理",
            "description":null,
            "path":"/department",
            "pagePath":"Department",
            "icon":"el-icon-office-building",
            "pid":3,
            "children":null
          }
        ],
        open: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : []
      }
    },
    created() {
      //初始化数据
      this.initialize()
    },
    methods:{
      showInfos(data){
        console.log(data)
      },
      initialize() {
      //从本地存储空间获取信息
      this.level = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).level : 0
      this.hospitalId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).hospitalId : 0
      this.departmentId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).departmentId : 0
    },
    }
  }
</script>


