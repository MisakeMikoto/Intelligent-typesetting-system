<template>
    <div style="font-size: 12px; line-height: 60px; display: flex">

        <div class="example-container" style="flex: 1; font-size:18px; width:10px; cursor: pointer">
            <span :class="collapseBtnClass" style="cursor:pointer" @click="collapse"></span>
        </div>

        <!-- 标签页开始 -->
        <div style="flex: 40; cursor: pointer;">
            <el-tabs v-model="editableTabsValue" type="card" closable @tab-remove="removeTab" @tab-click="handleClick">
                <el-tab-pane
                        v-for="(item, index) in editableTabs"
                        :key="item.name"
                        :label="item.title"
                        :name="item.name">
                </el-tab-pane>
            </el-tabs>
        </div>
        <!-- 标签页结束 -->

        <el-dropdown style="width:150px; cursor: pointer; text-align: right; margin-right: 5px">
            <!-- 头像和用户名开始 -->
            <div style="display: inline-block">
                <img :src="user.avatarUrl" alt=""
                     style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
                <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <!-- 头像和用户名结束 -->

            <!-- 头像下拉菜单开始 -->
            <el-dropdown-menu slot="dropdown" style="width: 110px; margin-top:0px; text-align: center" @click="addTab">
                <el-dropdown-item style="font-size: 14px; padding: 5px 0" icon="el-icon-s-custom">
                    <router-link style="text-decoration: none" to="/information">个人信息</router-link>
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px; padding: 5px 0" icon="el-icon-s-claim ">
                    <router-link style="text-decoration: none" to="/myWorkload">工作天数</router-link>
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px; padding: 5px 0" icon="el-icon-unlock">
                    <router-link style="text-decoration: none" to="/password">修改密码</router-link>
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px; padding: 5px 0" icon="el-icon-switch-button">
                    <span style="text-decoration: none" @click="logout">退出登录</span>
                </el-dropdown-item>
            </el-dropdown-menu>
            <!-- 头像下拉菜单结束 -->
        </el-dropdown>
    </div>
</template>

<script>
    export default {
        name: "Header",
        props: {
            collapseBtnClass: String,
            collapse: Function,
            user: Object
        },
        data() {
            return {
                tabList: [],//路由集合
                editableTabsValue: '1',//选中的标签
                editableTabs: [{//标签集合
                    title: '首页',
                    name: '0',
                    path: "/",
                }],
                tabIndex: 0
            }
        },
        watch: {
            $route() {
                //获取路由
                this.getRouteMatched();
                //添加路由到tab
                this.addTab(this.tabList);
            }
        },
        methods: {
            //跳转路由
            handleClick() {
                let activePath;//当前标签的path
                //找到this.editableTabsValue和name相同的标签
                for (const tab of this.editableTabs) {
                    if (tab.name === this.editableTabsValue) {
                        activePath = tab.path
                        break;
                    }
                }
                this.$router.push({path: activePath})//跳转页面
            },
            //获取路由
            getRouteMatched() {
                let matched = this.$route.matched;
                this.tabList = matched[1];
            },
            //添加tab
            addTab(tabList) {
                let newTabName = ++this.tabIndex + '';
                let newTabTitle = tabList.name;
                let newTabPath = tabList.path;
                //防止产生过多的标签
                for (const tab of this.editableTabs) {
                    if (tab.title === newTabTitle) {
                        this.editableTabsValue = tab.name;
                        return;
                    }
                }
                //添加新的标签，并绑定左侧模块的index
                this.editableTabs.push({
                    name: newTabName,
                    title: newTabTitle,
                    path: newTabPath
                });
                this.editableTabsValue = newTabName;
            },
            //删除tab
            removeTab(targetName) {
                if (targetName === '0') {//首页不删除
                    return;
                }
                let tabs = this.editableTabs;
                let activeName = this.editableTabsValue;
                if (activeName === targetName) {
                    tabs.forEach((tab, index) => {
                        if (tab.name === targetName) {
                            let nextTab = tabs[index + 1] || tabs[index - 1];
                            if (nextTab) {
                                activeName = nextTab.name;
                            }
                        }
                    });
                }

                this.editableTabsValue = activeName;
                this.editableTabs = tabs.filter(tab => tab.name !== targetName);
            },
            //退出登陆
            logout() {
                this.$store.commit("logout")
                this.$message.success("退出成功")
            }
        },
    }
</script>

<style scoped>
    .router-link-active {
        text-decoration: none;
        color: #606266;
    }

    a {
        text-decoration: none;
        color: #606266;
    }
</style>
