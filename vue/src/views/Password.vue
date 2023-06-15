<template>
    <div align="center" style="margin: 40px auto">
        <el-card style="width: 500px; border-radius: 10px; background-color: rgba(224,224,224,0.43);">
            <div style="width: 400px; padding: 10px;">
                <div style="margin: 30px 0; text-align: center; font-size: 24px"><b>修改密码</b></div>
                <el-form :model="user" :rules="rules" ref="userForm">
                    <el-form-item label="原密码" prop="password">
                        <el-input size="big" prefix-icon="el-icon-lock" show-password
                                  v-model="user.password" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPassword">
                        <el-input size="big" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                                  v-model="user.newPassword" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="确认新密码" prop="checkPassword">
                        <el-input size="big" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                                  v-model="user.checkPassword" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item style="margin: 30px 0; text-align: center">
                        <el-button type="primary" size="middle" @click="changePassword">
                            修改密码
                        </el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-card>
    </div>
</template>

<script>
    import user from "@/api/user";

    export default {
        name: "Password",
        data() {
            return {
                user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
                rules: {
                    password: [
                        {required: true, message: '请输入原密码', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                    ],
                    newPassword: [
                        {required: true, message: '请输入新密码', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                    ],
                    checkPassword: [
                        {required: true, message: '请再次输入新密码', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                    ],
                }
            }
        },
        methods: {
            changePassword() {
                this.$refs['userForm'].validate((valid) => {
                    if (valid) {//表单校验合法
                        if (this.user.newPassword !== this.user.checkPassword) {
                            this.$message.error("两次输入的密码不一样")
                            return false
                        }
                        user.updatePassword(this.user).then(res => {
                            if (res.code === 200) {
                                this.$message.success("修改成功")
                                this.$store.commit("logout")
                            } else {
                                this.$message.error(res.message)
                            }
                        })
                    }
                });
            }
        }
    }
</script>
