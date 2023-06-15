<template>
    <div>
        <!-- 查询框开始 -->
        <div style="padding: 10px 0;">
            <el-input style="width: 200px" placeholder="请输入姓名" v-model="searchObj.name"
                      suffix-icon="el-icon-search"></el-input>
            <el-button class="ml-5" type="primary" icon="el-icon-search" @click="getList()">查询
            </el-button>
            <el-button type="primary" plain icon="el-icon-refresh-right" @click="resetData()">重置</el-button>
        </div>
        <!-- 查询框结束 -->

        <div style="margin: 10px 0">
            <!--            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleAddWorkload()">新增-->
            <!--            </el-button>-->
            <el-button type="danger" icon="el-icon-remove-outline" @click="batchRemoveWorkload()">批量删除
            </el-button>
        </div>

        <!-- 表格开始 -->
        <el-table :data="tableData" border stripe
                  :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange"
                  :cell-style="{textAlign:'center'}" :header-cell-style="{textAlign:'center'}">
            <el-table-column type="selection" width="40" align="center"></el-table-column>
            <el-table-column prop="id" label="ID" width="40"></el-table-column>
            <el-table-column prop="name" label="姓名">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>用户编号: {{ scope.row.userId }}</p>
                        <p>用 户 名: {{ scope.row.username }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium">{{ scope.row.name }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column prop="realWorkDays" label="实际上班天数"></el-table-column>
            <el-table-column prop="workDays" label="预计上班天数"></el-table-column>
            <el-table-column prop="realMorningShifts" label="实际白班次数"></el-table-column>
            <el-table-column prop="morningShifts" label="预计白班次数"></el-table-column>
            <el-table-column prop="realMiddleShifts" label="实际小夜班次数"></el-table-column>
            <el-table-column prop="middleShifts" label="预计小夜班次数"></el-table-column>
            <el-table-column prop="realEveningShifts" label="实际大夜班次数"></el-table-column>
            <el-table-column prop="eveningShifts" label="预计大夜班次数"></el-table-column>
            <el-table-column prop="leftDays" label="放假天数"></el-table-column>
            <el-table-column prop="annualLeaveDays" label="年休天数"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
            <el-table-column prop="updateTime" label="修改时间" width="150"></el-table-column>

            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="success" icon="el-icon-edit" @click="handleEditWorkload(scope.row)">编辑
                    </el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="removeWorkload(scope.row.id)">删除
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
        <el-dialog title="工作量信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="120px" v-model="form">
                <el-form-item label="姓名">
                    <el-input v-model="form.name" disabled autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="实际上班天数">
                    <el-input v-model="form.realWorkDays" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="预计上班天数">
                    <el-input v-model="form.workDays" disabled autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="实际白班天数">
                    <el-input v-model="form.realMorningShifts" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="预计上班天数">
                    <el-input v-model="form.morningShifts" disabled autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="实际小夜班天数">
                    <el-input v-model="form.realMiddleShifts" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="预计上班天数">
                    <el-input v-model="form.middleShifts" disabled autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="实际大夜班次数">
                    <el-input v-model="form.realEveningShifts" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="预计晚班次数">
                    <el-input v-model="form.eveningShifts" disabled autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="放假天数">
                    <el-input v-model="form.leftDays" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="年休天数">
                    <el-input v-model="form.annualLeaveDays" autocomplete="off"></el-input>
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
    import workload from "@/api/workload";

    export default {
        name: "Workload",
        data() {
            return {
                current: 1,//当前页
                limit: 10,//每页显示记录数
                level: 0,//权限等级
                hospitalId: 0,//医院id
                departmentId: 0,//科室id
                searchObj: {},//条件封装对象
                tableData: [],//所有数据
                total: 0,//总记录数
                form: {},//数据表单
                showForm: {},//展示表单
                dialogFormVisible: false,//展示表单
                multipleSelection: [],//多选框
            }
        },
        created() {
            //初始化数据
            this.initialize()

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
            },
            //切换每页数据量
            handleSizeChange(limit) {
                this.limit = limit
                this.getList()
            },
            //新增工作量按钮
            handleAddWorkload() {
                this.dialogFormVisible = true
                this.form = {}
            },
            //编辑工作量按钮
            handleEditWorkload(row) {
                this.form = row
                this.dialogFormVisible = true
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
            //获得工作量信息
            getList(current = 1) {
                this.current = current
                workload.findPageWorkload(this.current, this.limit, this.level, this.hospitalId, this.departmentId, this.searchObj)
                    .then(res => {
                        console.log(res.data.records)
                        this.tableData = res.data.records
                        this.total = res.data.total
                    }).catch(res => {
                    console.error(res)
                })
            },
            //保存工作量
            submitSave() {
                workload.saveWorkload(this.form).then(res => {
                    if (res.code === 200) {
                        this.$message.success("保存成功")
                        //关闭窗口
                        this.dialogFormVisible = false
                        //刷新页面
                        this.getList()
                    }
                })
            },
            //修改工作量
            submitEdit() {
                workload.editWorkload(this.form).then(res => {
                    if (res.code === 200) {
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
            //删除工作量
            removeWorkload(id) {
                this.$confirm('此操作将永久删除工作量信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//确定执行then方法
                    //调用接口
                    workload.deleteWorkloadById(id)
                        .then(res => {
                            this.$message.success("删除成功")
                            //刷新页面
                            this.getList()
                        })
                })
            },
            //批量删除工作量
            batchRemoveWorkload() {
                this.$confirm('此操作将永久删除多条工作量信息, 是否继续?', '提示', {
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
                    //调用接口
                    workload.batchDeleteWorkload(idList)
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
