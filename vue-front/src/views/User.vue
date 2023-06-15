<template>
    <div>
        <!-- 查询框开始 -->
        <div style="padding: 10px 0;">
            <el-input style="width: 200px" placeholder="请输入姓名" v-model="searchObj.name"
                      suffix-icon="el-icon-search"></el-input>
            <el-select style="width: 200px" filterable placeholder="请选择科室" v-model="searchObj.departmentName"
                       class="ml-5" clearable v-if="this.departmentId==null">
                <el-option
                        v-for="item in departmentOptions"
                        :key="item.id"
                        :label="item.departmentName"
                        :value="item.departmentName">
                </el-option>
            </el-select>
            <el-button class="ml-5" type="primary" icon="el-icon-search" @click="getList()">查询
            </el-button>
            <el-button type="primary" plain icon="el-icon-refresh-right" @click="resetData()">重置</el-button>
        </div>
        <!-- 查询框结束 -->

        <div style="margin: 10px 0">
            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleAddUser()">新增
            </el-button>
            <el-button type="danger" icon="el-icon-remove-outline" @click="batchRemoveUser()">批量删除
            </el-button>
        </div>

        <!-- 表格开始 -->
        <el-table :data="tableData" border stripe
                  :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange"
                  :cell-style="{textAlign:'center'}" :header-cell-style="{textAlign:'center'}">
            <el-table-column type="selection" width="40" align="center"></el-table-column>
            <el-table-column prop="id" label="ID" width="40"></el-table-column>
            <el-table-column prop="name" label="姓名" align="center">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>医院编号: {{ scope.row.hospitalId }}</p>
                        <p>医院名称: {{ scope.row.hospitalName }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium">{{ scope.row.name }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column prop="username" label="用户名"></el-table-column>
            <el-table-column prop="password" label="密码"></el-table-column>
            <el-table-column prop="sex" label="性别"></el-table-column>
            <el-table-column prop="phone" label="手机号码"></el-table-column>
            <el-table-column prop="roleName" label="角色">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p v-if="scope.row.departmentId!=null">科室编号: {{ scope.row.departmentId }}</p>
                        <p v-if="scope.row.departmentName!=null">科室名称: {{ scope.row.departmentName }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium" type="success">{{ scope.row.roleName }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
            <el-table-column prop="updateTime" label="修改时间" width="150"></el-table-column>
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="success" icon="el-icon-edit" @click="handleEditUser(scope.row)">编辑
                    </el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="removeUser(scope.row.id)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 表格结束 -->

        <!-- 分页栏开始 -->
        <div style="padding: 10px">
            <el-pagination
                    align="center"
                    :current-page="current"
                    :page-sizes="[10, 15, 20, 25]"
                    :page-size="limit"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
                    @current-change="getList"
                    @size-change="handleSizeChange">
            </el-pagination>
        </div>
        <!-- 分页栏结束 -->

        <!-- 表单开始 -->
        <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" v-model="form">
                <el-form-item label="用户名">
                    <el-input v-model="form.username" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="form.password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="姓名">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                    <el-select v-model="form.sex" style="width: 100%">
                        <el-option label="男" value="男"></el-option>
                        <el-option label="女" value="女"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="手机号码">
                    <el-input v-model.number="form.phone" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="医院名称" v-if="this.hospitalId==null">
                    <el-select v-model="form.hospitalId" filterable style="width: 100%">
                        <el-option
                                v-for="item in hospitalOptions"
                                :key="item.id"
                                :label="item.hospitalName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="医院名称" v-else>
                    <el-input v-model="this.hospitalName" disabled autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="科室名称">
                    <el-select v-model="form.departmentId" filterable style="width: 100%">
                        <el-option
                                v-for="item in departmentOptions"
                                :key="item.id"
                                :label="item.departmentName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="角色">
                    <el-select v-model="form.roleId" filterable style="width: 100%">
                        <el-option
                                v-for="item in roleOptions"
                                :key="item.id"
                                :label="item.roleName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveOrUpdate()">保 存</el-button>
            </div>
        </el-dialog>
        <!-- 表单结束 -->
    </div>
</template>

<script>
    import user from "@/api/user";
    import hospital from "@/api/hospital";
    import department from "@/api/department";
    import role from "@/api/role";
    import workload from "@/api/workload";

    export default {
        name: "User",
        data() {
            return {
                current: 1,//当前页
                limit: 10,//每页显示记录数
                level: 0,//权限等级
                hospitalId: 0,//医院id
                hospitalName: "",//医院名称
                departmentId: 0,//科室id
                searchObj: {},//条件封装对象
                tableData: [],//所有数据
                total: 0,//总记录数
                form: {},//数据表单
                showForm: {},//展示表单
                dialogFormVisible: false,//展示表单
                multipleSelection: [],//多选框
                hospitalOptions: [],//医院下拉菜单
                departmentOptions: [],//科室下拉选择
                roleOptions: [],//角色选择下拉菜单
            }
        },
        created() {
            //初始化数据
            this.initialize();

            //获得医院、科室、角色的信息
            this.getDropDownInformation()

            //打开页面显示数据
            this.getList()
        },
        methods: {
            //初始化数据
            initialize() {
                //从本地存储空间获取信息
                this.level = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).level : 0
                this.hospitalId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).hospitalId : 0
                this.departmentId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).departmentId : 0
                //打印信息
                console.log("this.level:" + this.level + "   this.hospitalId:" + this.hospitalId + "   this.departmentId:" + this.departmentId)
            },
            //切换每页数据量
            handleSizeChange(limit) {
                this.limit = limit
                this.getList()
            },
            //新增用户按钮
            handleAddUser() {
                this.dialogFormVisible = true
                this.form = {}
                //根据hospitalId获取科室
                department.getDepartmentByHospitalId(this.hospitalId)
                    .then(res => {
                        this.departmentOptions = res.data
                    })
            },
            //编辑用户按钮
            handleEditUser(row) {
                this.form = row
                this.dialogFormVisible = true
                let hospId = this.hospitalId == null ? row.hospitalId : this.hospitalId
                //根据hospitalId获取科室
                department.getDepartmentByHospitalId(hospId)
                    .then(res => {
                        this.departmentOptions = res.data
                    })
            },
            //批量选择
            handleSelectionChange(selection) {
                this.multipleSelection = selection;
            },
            //重置查询表单
            resetData() {
                this.searchObj = {}
                this.getList()
            },
            //获得用户信息
            getList(current = 1) {
                this.current = current
                user.findPageUser(this.current, this.limit, this.level, this.hospitalId, this.departmentId, this.searchObj)
                    .then(res => {
                        this.tableData = res.data.records
                        this.total = res.data.total
                        console.log(this.tableData)
                    }).catch(res => {
                    console.error(res)
                })
            },
            //获得医院、科室、角色的信息
            getDropDownInformation() {
                //点击新增，获得用户自身所属医院id
                //点击编辑，获取表格中用户所属医院id
                if (this.level === 0) {
                    //获取所有医院信息
                    hospital.findAllHospital()
                        .then(res => {
                            this.hospitalOptions = res.data
                        })
                } else {
                    //根据hospitalId获取医院信息
                    hospital.getHospitalById(this.hospitalId)
                        .then(res => {
                            this.hospitalName = res.data.hospitalName
                        })
                }
                //根据hospitalId获取科室
                department.getDepartmentByHospitalId(this.hospitalId)
                    .then(res => {
                        this.departmentOptions = res.data
                    })
                //根据查询权限等级大于level的角色
                role.getRoleByGtLevel(this.level)
                    .then(res => {
                        this.roleOptions = res.data
                    })
            },
            //保存用户
            submitSave() {
                //如果用户不为管理员
                if (this.hospitalId != null) {
                    this.form.hospitalId = this.hospitalId
                }
                user.saveUser(this.form).then(res => {
                    if (res) {
                        this.$message.success("保存成功")
                        //关闭窗口
                        this.dialogFormVisible = false
                        //刷新页面
                        this.getList()
                    }
                })
            },
            //修改用户
            submitEdit() {
                user.editUser(this.form).then(res => {
                    if (res) {
                        this.$message.success("修改成功")
                        //关闭窗口
                        this.dialogFormVisible = false
                        //刷新页面
                        this.getList()
                    }
                })
            },
            //判断添加还是修改
            saveOrUpdate() {
                if (!this.form.id) {//没有id，做添加
                    this.submitSave();
                } else {//修改
                    this.submitEdit()
                }
            },
            //删除用户
            removeUser(id) {
                this.$confirm('此操作将永久删除用户信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//确定执行then方法
                    //先删除工作量表
                    workload.deleteWorkloadByUserId(id)
                        .then(res => {
                            console.log("删除用户工作量数据")
                        })

                    //再删除用户表
                    user.deleteUserById(id)
                        .then(res => {
                            this.$message.success("删除成功")
                            //刷新页面
                            this.getList()
                        })

                })
            },
            //批量删除用户
            batchRemoveUser() {
                this.$confirm('此操作将永久删除多条用户信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//确定执行then方法
                    var idList = []
                    //遍历数组得到每个id值，设置到idList里面
                    for (var i = 0; i < this.multipleSelection.length; i++) {
                        var obj = this.multipleSelection[i]
                        var id = obj.id
                        idList.push(id)
                    }
                    //先批量删除工作量表
                    workload.batchDeleteWorkloadByUserId(idList)
                        .then(res => {
                            console.log("批量删除用户工作量数据")
                        })
                    //再批量删除用户表
                    user.batchDeleteUser(idList)
                        .then(res => {
                            this.$message.success("删除成功")
                            //刷新页面
                            this.getList()
                        })

                })
            },
        }
    }
</script>
