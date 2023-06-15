<template>
    <div align="center" style="margin: 50px auto">
        <el-card style="width: 650px; border-radius: 10px;">
            <div style="width: 500px;">
                <el-form label-width="80px" style="margin: 20px auto">
                    <el-upload
                            class="avatar-uploader"
                            :action="'http://' + this.serverIp + '/files/upload'"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess">
                        <img v-if="this.form.avatarUrl" :src="this.form.avatarUrl" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    <el-form-item label="医院名称">
                        <el-input size="big" v-model="form.hospitalName" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="科室名称">
                        <el-input size="big" v-model="form.departmentName" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="角色">
                        <el-input size="big" v-model="form.roleName" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="用户名" style="margin-top: 20px">
                        <el-input size="big" v-model="form.username" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="姓名">
                        <el-input size="big" v-model="form.name" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="性别">
                        <el-input size="big" v-model="form.sex" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号码">
                        <el-input size="big" v-model="form.phone" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item style="margin-top: 15px; margin-right: 80px; text-align: center;">
                        <el-button type="primary" size="big" @click="save">
                            修改并保存个人信息
                        </el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-card>
    </div>
</template>

<script>
    import user from "@/api/user";
    import {serverIp} from "../../public/config";

    export default {
        name: "Information",
        data() {
            return {
                serverIp: serverIp,//ip地址
                form: {},//数据表单
                user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
            }
        },
        created() {
            this.getUser()
        },
        methods: {
            async getUser() {
                let res = await user.getInformationById(this.user.id)
                this.form = res.data
                return res.data
            },
            //获取头像成功钩子
            handleAvatarSuccess(res) {
                this.form.avatarUrl = res.data
            },
            save() {
                user.editUser(this.form).then(res => {
                    if (res.code === 200) {
                        this.$message.success("修改成功")

                        // 触发父级更新User的方法
                        this.$emit("refreshUser")

                        // 更新浏览器存储的用户信息
                        this.getUser().then(res => {
                            this.user.name = res.name
                            localStorage.setItem("user", JSON.stringify(this.user))
                            // res.token = JSON.parse(localStorage.getItem("user")).token
                        })
                    }
                })
            },
        }
    }
</script>

<style>
    .avatar-uploader {
        text-align: center;
        padding-bottom: 10px;
    }

    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 200px;
        height: 200px;
        line-height: 200px;
        text-align: center;
    }

    .avatar {
        width: 138px;
        height: 138px;
        display: block;
    }
</style>
