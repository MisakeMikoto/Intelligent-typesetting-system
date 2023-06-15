<template>
    <el-menu :default-openeds=this.open style="min-height: 100%;"
             background-color="rgb(50,75,100)"
             text-color="#fff"
             active-text-color="#ffa64d"
             :collapse-transition="false"
             :collapse="isCollapse"
             class="el-menu-vertical-demo"
             router>

        <div style="height: 70px; line-height: 70px; ; text-align: center">
            <img src="../assets/ninghui.png" style="width: 20px; position: relative; top:5px; margin-right: 5px">
            <b style="color: white;" v-show="!isCollapse">护士排班管理系统</b>
        </div>

        <!-- 动态路由开始 -->
        <div v-for="item in menus" :key="item.id">
            <!-- 如果有路径,为一级菜单 -->
            <div v-if="item.path" style="width: 200px">
                <el-menu-item :index="item.path">
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
                menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
                open: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : []
            }
        },
    }
</script>


