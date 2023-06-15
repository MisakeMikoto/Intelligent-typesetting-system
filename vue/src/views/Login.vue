<template>
    <div class="wrapper">
        <div style="position:absolute; left: 0; right: 0; top: 0; bottom: 0; margin: auto; background-color: #fff; width: 400px; height: 350px; padding: 25px; border-radius: 10px">
            <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
            <el-form :model="user" :rules="rules" ref="userForm">
                <el-form-item prop="username">
                    <el-input placeholder="用户名" size="big" style="margin: 10px 0" prefix-icon="el-icon-user"
                              v-model="user.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input placeholder="密码" size="big" style="margin: 10px 0" prefix-icon="el-icon-lock"
                              show-password v-model="user.password"></el-input>
                </el-form-item>
                <el-form-item style="margin: 30px 0; text-align: right">
                    <el-button type="primary" size="middle" autocomplete="off" @click="login"
                               style="margin-right: 15px">登录
                    </el-button>
                    <el-button type="warning" size="middle" autocomplete="off" @click="$router.push('/register')">注册
                    </el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import user from "@/api/user";
    import {setRoutes} from "@/router";

    export default {
        name: "Login",
        data() {
            return {
                user: {},
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                    ],
                }
            }
        },
        methods: {
            login() {
                this.$refs['userForm'].validate((valid) => {
                    if (valid) {//表单校验合法
                        user.login(this.user).then(res => {
                            if (res.code === 200) {
                                this.$message.success("登录成功")
                                localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
                                localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储用户信息到浏览器
                                // 动态设置当前用户的路由
                                setRoutes()
                                //跳转路由
                                this.$router.push("/")
                            }
                        })
                    }
                });
            }
        }
    }
</script>

<style>
    .wrapper {
        height: 100%;
        background-image: linear-gradient(to bottom right, #3F5EFB, #32bdb1);
        overflow: hidden;
    }
</style>
