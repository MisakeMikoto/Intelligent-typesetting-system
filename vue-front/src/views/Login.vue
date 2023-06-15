<template>
    <div class="wrapper">
        <div style="margin: 220px auto; background-color: #fff; width: 1200px; height: 500px; padding: 25px; border-radius: 10px">

            <el-row :gutter="20">
              <el-col :span="13">
                <div class="grid-content bg-purple">
                  <div class="block" :gutter="10">
                    <el-carousel height="400px" style="text-align: center">
                      <el-carousel-item v-for="(item, index) in image" :key="index">
                        <img :src="item.url">
                      </el-carousel-item>
                    </el-carousel>
                  </div>
                </div>
              </el-col>
              <el-col :span="10" >
                <div class="grid-content bg-purple">
                  <div style="padding-top: 60px; text-align: center; font-size: 24px;">
                    <b>登 录</b>
                  </div>
                  <el-form :model="user" :rules="rules" ref="userForm" style="text-align: center;height: 310px" >

                    <el-form-item prop="username">
                      <el-input placeholder="用户名" size="middle" style="margin: 10px 0;width: auto" prefix-icon="el-icon-user"
                                v-model="user.username"></el-input>
                    </el-form-item>

                    <el-form-item prop="password">
                      <el-input placeholder="密码" size="middle" style="margin: 10px 0;width: 215px" prefix-icon="el-icon-lock"
                                show-password v-model="user.password"></el-input>
                    </el-form-item>

                    <el-form-item style="">

                      <el-button type="primary" size="middle" autocomplete="off" @click="login"
                                 style="">登录
                      </el-button>

                      <el-button type="warning" size="middle" autocomplete="off" @click="$router.push('/register')">注册
                      </el-button>

                    </el-form-item>

                  </el-form>
                </div>
              </el-col>
            </el-row>

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
                image:[
                  {url: require("../assets/welcome.png")},
                  {url: require("../assets/manage.png")},
                  {url: require("../assets/data.png")},
                  {url: require("../assets/analyse.png")},
                ],
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
    .el-carousel__item h3 {
      color: #475669;
      font-size: 14px;
      opacity: 0.75;
      line-height: 150px;
      margin: 0;
    }
    .el-row {
      margin-bottom: 20px;
    &:last-child {
       margin-bottom: 0;
     }
    }
    .el-col {
      border-radius: 4px;
    }
    .bg-purple-dark {
      background: #99a9bf;
    }
    .bg-purple {
      background: #d3dce6;
    }
    .bg-purple-light {
      background: #e5e9f2;
    }
    .grid-content {
      border-radius: 4px;
      min-height: 36px;
    }
    .row-bg {
      padding: 10px 0;
      background-color: #f9fafc;
    }
</style>
