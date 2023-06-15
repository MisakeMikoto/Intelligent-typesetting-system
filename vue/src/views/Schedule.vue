<template>
    <div>
        <!-- 查询框开始 -->
        <div style="padding: 10px 0;">
            <el-input style="width: 200px" placeholder="请输入用户姓名" v-model="searchObj.name"
                      suffix-icon="el-icon-search"></el-input>
            <el-select style="width: 200px" filterable placeholder="请选择科室" v-model="searchObj.departmentName"
                       class="ml-5" clearable v-if="departmentId==null">
                <el-option
                        v-for="item in departmentOptions"
                        :key="item.id"
                        :label="item.departmentName"
                        :value="item.departmentName">
                </el-option>
            </el-select>
            <el-select style="width: 200px" filterable placeholder="请选择班次" v-model="searchObj.shiftName"
                       class="ml-5" clearable>
                <el-option
                        v-for="item in shiftOptions"
                        :key="item.id"
                        :label="item.shiftName"
                        :value="item.shiftName">
                </el-option>
            </el-select>
            <el-button class="ml-5" type="primary" icon="el-icon-search" @click="getList()">查询
            </el-button>
            <el-button type="primary" plain icon="el-icon-refresh-right" @click="resetData()">重置</el-button>
        </div>
        <!-- 查询框结束 -->

        <div style="margin: 10px 0">
            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleAddSchedule()">新增
            </el-button>
            <el-button type="danger" icon="el-icon-remove-outline" @click="batchRemoveSchedule()">批量删除
            </el-button>
            <el-button type="primary" plain icon="el-icon-edit-outline" @click="autoSchedule()" v-if="departmentId">自动排班
            </el-button>
            <el-button type="info" plain icon="el-icon-download" @click="handleExportSchedule()" v-if="departmentId">
                导出排班表
            </el-button>
        </div>

        <!-- 表格开始 -->
        <el-table :data="tableData" border stripe v-loading="loading"
                  :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange"
                  :cell-style="{textAlign:'center'}" :header-cell-style="{textAlign:'center'}">
            <el-table-column type="selection" width="40" align="center"></el-table-column>
            <el-table-column prop="shiftId" label="班次编号" width="80">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>医院编号: {{ scope.row.hospitalId }}</p>
                        <p>医院名称: {{ scope.row.hospitalName }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium" type="danger">{{ scope.row.shiftId }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column prop="shiftName" label="班次名称">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p v-if="scope.row.departmentId!=null">科室编号: {{ scope.row.departmentId }}</p>
                        <p v-if="scope.row.departmentName!=null">科室名称: {{ scope.row.departmentName }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium" type="warning">{{ scope.row.shiftName }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
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
            <el-table-column prop="scheduleDate" label="排班日期"></el-table-column>
            <el-table-column prop="departmentName" label="科室名称"></el-table-column>
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="success" icon="el-icon-edit" @click="handleEditSchedule(scope.row)">编辑
                    </el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="removeSchedule(scope.row)">删除
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
        <el-dialog title="排班信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" :model="form" :rules="rules" ref="scheduleForm">
                <el-form-item label="用户编号" prop="userId">
                    <el-input v-model="form.userId" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="班次编号" prop="shiftId">
                    <el-input v-model="form.shiftId" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="排班日期" prop="scheduleDate">
                    <el-date-picker
                            placeholder="选择日期"
                            v-model="form.scheduleDate"
                            type="date"
                            value-format="yyyy-MM-dd"
                            format="yyyy 年 MM 月 dd 日"
                            style="width: 100%">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveOrUpdate()">保 存</el-button>
            </div>
        </el-dialog>
        <!-- 表单结束 -->

        <!-- 导出排班表单开始 -->
        <el-dialog title="导出排班表" :visible.sync="dialogScheduleFormVisible" width="20%">
            <el-form label-width="80px" :model="form">
                <el-form-item label="用户编号" prop="userId">
                    <el-date-picker
                            v-model="form.month"
                            type="month"
                            placeholder="选择月份"
                            value-format="MM"
                            format="MM 月"
                            style="width: 100%">
                    </el-date-picker>
                </el-form-item>
            </el-form>

            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogScheduleFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="exportSchedule(month)">导 出</el-button>
            </div>
        </el-dialog>
        <!-- 导出排班表单结束 -->
    </div>
</template>

<script>
    import schedule from "@/api/schedule";
    import shift from "@/api/shift";
    import department from "@/api/department";
    import {serverIp} from "../../public/config";

    export default {
        name: "Schedule",
        data() {
            return {
                serverIp: serverIp,//ip地址
                current: 1,//当前页
                limit: 10,//每页显示记录数
                hospitalId: 0,//医院id
                hospitalName: '',//医院名称
                departmentId: 0,//科室id
                searchObj: {},//条件封装对象
                tableData: [],//所有数据
                total: 0,//总记录数
                form: {},//数据表单
                showForm: {},//展示表单
                dialogFormVisible: false,//展示表单
                dialogScheduleFormVisible: false,//展示表单
                multipleSelection: [],//多选框
                departmentOptions: [],//科室下拉选择
                shiftOptions: [],//班次下拉菜单
                autoScheduleData: [],//自动排班后的数据
                loading: true,//加载
                userId: 0,//用户id
                scheduleDate: '',//日期
                month: '',//月份
                rules: {
                    userId: [
                        {required: true, message: '请输入用户编号', trigger: 'blur'},],
                    shiftId: [
                        {required: true, message: '请输入班次编号', trigger: 'blur'},
                    ],
                    scheduleDate: [
                        {required: true, message: '请选择日期', trigger: 'blur'},
                    ],
                    scheduleMonth: [
                        {required: true, message: '请选择月份', trigger: 'blur'},
                    ],
                }
            }
        },
        created() {
            //初始化数据
            this.initialize()

            //获得医院、科室的信息
            this.getDropDownInformation()

            //打开页面显示数据
            this.getList()
        },
        methods: {
            //初始化数据
            initialize() {
                //从本地存储空间获取信息
                this.hospitalId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).hospitalId : 0
                this.departmentId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).departmentId : 0
            },
            //切换每页数据量
            handleSizeChange(limit) {
                this.limit = limit
                this.getList()
            },
            //新增排班按钮
            handleAddSchedule() {
                this.dialogFormVisible = true
                this.form = {}
                this.userId = 0//重置要修改用户原来的id
                this.scheduleDate = ""//重置要修改原来的日期
            },
            //编辑排班按钮
            handleEditSchedule(row) {
                this.form = row
                this.dialogFormVisible = true
                this.userId = this.form.userId//保存要修改用户原来的id
                this.scheduleDate = this.form.scheduleDate//保存要修改原来的日期
            },
            //导出排班表按钮
            handleExportSchedule() {
                this.form = {}
                this.dialogScheduleFormVisible = true
            },
            //批量选择
            handleSelectionChange(selection) {
                this.multipleSelection = selection;
            },
            //自动排班按钮
            autoSchedule() {
                const h = this.$createElement;
                this.$msgbox({
                    title: '注意',
                    type: 'warning',
                    message: h('p', null, [
                        h('div', null, '自动排班之前先确定您是否设置了班次！'),
                        h('span', null, '只设置早班和晚班代表：'),
                        h('i', {style: 'color: teal'}, '两班制'),
                        h('span', null, '，设置了白班、小夜班和大夜班代表：'),
                        h('i', {style: 'color: teal'}, '三班制'),
                        h('span', null, '。'),
                    ]),
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    beforeClose: (action, instance, done) => {
                        if (action === 'confirm') {
                            instance.confirmButtonLoading = true;
                            instance.confirmButtonText = '调用算法中...';
                            setTimeout(() => {
                                schedule.autoSchedule(this.departmentId)
                                    .then(res => {
                                        this.autoScheduleData = res.data
                                        if (res.code === 200) {
                                            //输出提示
                                            this.$message({
                                                type: 'success',
                                                message: '自动排班成功'
                                            });
                                            this.getList()
                                        }
                                    }).catch(res => {
                                    console.error(res)
                                })
                                done();
                                setTimeout(() => {
                                    instance.confirmButtonLoading = false;
                                }, 100);
                            }, 1000);
                        } else {
                            done();
                        }
                    }
                })
            },
            //导出排班表
            exportSchedule() {
                let url = 'http://' + this.serverIp + '/files/exportSchedule/' + this.departmentId + '/' + this.form.month
                window.open(url)
            },
            //重置查询表单
            resetData() {
                this.searchObj = {}
                this.getList()
            },
            //获得排班信息
            getList(current = 1) {
                this.current = current
                schedule.findPageSchedule(this.current, this.limit, this.hospitalId, this.departmentId, this.searchObj)
                    .then(res => {
                        this.loading = false
                        this.tableData = res.data.records
                        this.total = res.data.total
                    }).catch(res => {
                    console.error(res)
                })
            },
            //获得医院、科室的信息
            getDropDownInformation() {
                //根据hospitalId获取科室
                department.getDepartmentByHospitalId(this.hospitalId)
                    .then(res => {
                        this.departmentOptions = res.data
                    })

                //根据departmentId获取班次
                shift.getShiftByDepartmentId(this.departmentId)
                    .then(res => {
                        this.shiftOptions = res.data
                    })
            },
            //保存排班
            submitSave() {
                this.$refs['scheduleForm'].validate((valid) => {
                    if (valid) {//表单校验合法
                        schedule.saveSchedule(this.form).then(res => {
                            if (res.code === 200) {
                                this.$message.success("保存成功")
                                //关闭窗口
                                this.dialogFormVisible = false
                                //刷新页面
                                this.getList()
                            }
                        })
                    }
                });
            },
            //修改排班
            submitEdit() {
                this.$refs['scheduleForm'].validate((valid) => {
                    if (valid) {//表单校验合法
                        schedule.editSchedule(this.userId, this.scheduleDate, this.form).then(res => {
                            if (res.code === 200) {
                                this.$message.success("修改成功")
                                //关闭窗口
                                this.dialogFormVisible = false
                                //刷新页面
                                this.getList()
                            }
                        })
                    }
                });
            },
            //判断添加还是修改
            saveOrUpdate() {
                if (!this.userId) { //没有id，做添加
                    this.submitSave();
                } else {//修改
                    this.submitEdit()
                }
            },
            //删除排班
            removeSchedule(row) {
                this.$confirm('此操作将永久删除排班信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => { //确定执行then方法
                    //调用接口
                    schedule.removeSchedule(row)
                        .then(res => {
                            this.$message.success("删除成功")
                            //刷新页面
                            this.getList()
                        })
                })
            },
            //批量删除排班
            batchRemoveSchedule() {
                this.$confirm('此操作将永久删除多条排班信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => { //确定执行then方法
                    //调用接口
                    schedule.batchDeleteSchedule(this.multipleSelection)
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
