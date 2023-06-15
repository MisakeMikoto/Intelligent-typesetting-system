<template>
    <div class="wrapper">
        <div style="position:absolute;left: 0;right: 0;top: 0;bottom: 0;margin: auto; background-color: #fff; width: 400px; height: 620px; padding: 25px; border-radius: 10px">
            <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>注 册</b></div>
            <el-form :model="user" :rules="rules" ref="userForm">
                <el-form-item label="用户名" prop="username">
                    <el-input size="big" style="margin: 10px 0" prefix-icon="el-icon-user"
                              v-model="user.username" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input size="big" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                              v-model="user.password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="checkPassword">
                    <el-input size="big" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                              v-model="user.checkPassword" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input size="big" style="margin: 10px 0" prefix-icon="el-icon-message"
                              v-model="user.email" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item style="margin: 30px 0; text-align: right">
                    <el-button type="primary" size="middle" autocomplete="off" @click="register"
                               style="margin-right: 15px">注册
                    </el-button>
                    <el-button type="primary" plain size="middle" autocomplete="off" @click="$router.push('/login')">
                        返回
                    </el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import user from "@/api/user";

    export default {
        name: "Register",
        data() {
            return {
                user: {},
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                        {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                    ],
                    checkPassword: [
                        {required: true, message: '请再次输入密码', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                    ],
                    email: [
                        {message: '请输入邮箱', trigger: 'blur'},
                        {min: 5, max: 36, message: '请输入正确格式', trigger: 'blur'}
                    ],
                }
            }
        },
        methods: {
            register() {
                this.$refs['userForm'].validate((valid) => {
                    if (valid) {//表单校验合法
                        if (this.user.password !== this.user.checkPassword) {
                            this.$message.error("两次输入的密码不一样")
                            return false
                        }
                        user.register(this.user).then(res => {
                            if (res.code === 200) {
                                this.$message.success("注册成功")
                                this.$router.push("/login")
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
        height: 100vh;
        background-image: linear-gradient(to bottom right, #3F5EFB, #32bdb1);
        overflow: hidden;
    }
</style>
