<template>
    <div style="height: 100%">
        <el-container style="height: 100%; border: 1px solid #eee">
            <el-aside width="sideWidth + 'px'"
                      style="background-color: rgb(238, 241, 246); height:100%; box-shadow:2px 0 6px rgb(0 21 41 / 35%)">
                <Aside :isCollapse="isCollapse"/>
            </el-aside>

            <el-container>
                <el-header style="border-bottom: 1px solid #ccc;">
                    <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user"/>
                </el-header>

                <el-main>
                    <router-view @refreshUser="getUser"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    import Aside from "@/components/Aside";
    import Header from "@/components/Header";
    import user from "@/api/user";

    export default {
        name: 'Layout',
        components: {
            Aside,
            Header
        },
        data() {
            return {
                collapseBtnClass: 'el-icon-s-fold',//按钮状态
                isCollapse: false,//LOGO显示
                sideWidth: 200,//收缩宽度
                user: {}//user数据
            }
        },
        created() {
            //从后台获取最新的user数据
            this.getUser()
        },
        methods: {
            //点击收缩按钮触发
            collapse() {
                this.isCollapse = !this.isCollapse
                if (this.isCollapse) {
                    this.sideWidth = 64
                    this.collapseBtnClass = 'el-icon-s-unfold'
                } else {
                    this.sideWidth = 200
                    this.collapseBtnClass = 'el-icon-s-fold'
                }
            },
            getUser() {
                //获得后台userId
                let id = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).id : ""
                //通过id从后台获取User数据
                user.getUserById(id).then(res => {
                    //重新赋值后台的最新user数据
                    this.user = res.data
                })
            },
        }
    }
</script>
