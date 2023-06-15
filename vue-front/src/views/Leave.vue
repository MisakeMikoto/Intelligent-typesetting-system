<template>
    <div>
        <!-- 查询框开始 -->
        <div style="padding: 10px 0;">
            <el-input style="width: 200px" placeholder="请输入用户姓名" v-model="searchObj.name"
                      suffix-icon="el-icon-search"></el-input>
            <el-button class="ml-5" type="primary" icon="el-icon-search" @click="getList()">查询
            </el-button>
            <el-button type="primary" plain icon="el-icon-refresh-right" @click="resetData()">重置</el-button>
        </div>
        <!-- 查询框结束 -->

        <div style="margin: 10px 0">
            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleAddLeave()">新增
            </el-button>
            <el-button type="danger" icon="el-icon-remove-outline" @click="batchRemoveLeave()">批量删除
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
                        <p>医院编号: {{ scope.row.hospitalId }}</p>
                        <p>医院名称: {{ scope.row.hospitalName }}</p>
                        <p v-if="scope.row.departmentId!=null">科室编号: {{ scope.row.departmentId }}</p>
                        <p v-if="scope.row.departmentName!=null">科室名称: {{ scope.row.departmentName }}</p>
                        <p>用户编号: {{ scope.row.userId }}</p>
                        <p>用 户 名: {{ scope.row.username }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium">{{ scope.row.name }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column prop="startDate" label="请假开始日期"></el-table-column>
            <el-table-column prop="endDate" label="请假结束日期"></el-table-column>
            <el-table-column prop="leaveDays" label="请假天数"></el-table-column>
            <el-table-column prop="reason" label="请假原因"></el-table-column>
            <el-table-column label="批假状态" align="center" width="70">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ccc"
                               @change="handleStatusChange(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column prop="replaceUsername" label="顶班用户姓名">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>用户编号: {{ scope.row.replaceUserId }}</p>
                        <div slot="reference" class="name-wrapper" v-if="scope.row.replaceUserId">
                            <el-tag size="medium">{{ scope.row.replaceUsername }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
            <el-table-column prop="updateTime" label="修改时间" width="150"></el-table-column>

            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="success" icon="el-icon-edit" @click="handleEditLeave(scope.row)">编辑
                    </el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="removeLeave(scope.row.id)">删除
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
        <el-dialog title="请假信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="100px" v-model="form">
                <el-form-item label="用户编号">
                    <el-input v-model="form.userId" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="开始日期" prop="startDate">
                    <el-date-picker
                            placeholder="选择日期"
                            v-model="form.startDate"
                            type="date"
                            value-format="yyyy-MM-dd"
                            format="yyyy 年 MM 月 dd 日"
                            style="width: 100%">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="结束日期" prop="endDate">
                    <el-date-picker
                            placeholder="选择日期"
                            v-model="form.endDate"
                            type="date"
                            value-format="yyyy-MM-dd"
                            format="yyyy 年 MM 月 dd 日"
                            style="width: 100%">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="请假原因">
                    <el-input type="textarea" :rows="2" placeholder="请输入请假原因" v-model="form.reason"
                              autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveOrUpdate()">保 存</el-button>
            </div>
        </el-dialog>
        <!-- 表单结束 -->

        <!-- 合适顶班人选表格开始 -->
        <el-dialog title="合适的顶班人选" :visible.sync="matchStaffVisible" width="25%">
            <el-table :data="matchStaffs" border stripe
                      :cell-style="{textAlign:'center'}" :header-cell-style="{textAlign:'center'}">
                <el-table-column property="name" label="姓名"></el-table-column>

                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button type="success" icon="el-icon-edit" @click="replaceSchedule(scope.row)">顶班
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>

        <!-- 合适顶班人选表格结束 -->

    </div>
</template>

<script>
    import leave from "@/api/leave";
    import schedule from "@/api/schedule";

    export default {
        name: "Leave",
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
                matchStaffs: {},//合适顶班人选
                dialogFormVisible: false,//展示表单
                matchStaffVisible: false,//展示合适的顶班人选
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
            //新增请假按钮
            handleAddLeave() {
                this.dialogFormVisible = true
                this.form = {}
            },
            //编辑请假按钮
            handleEditLeave(row) {
                this.form = row
                this.dialogFormVisible = true
            },
            //批量选择
            handleSelectionChange(selection) {
                this.multipleSelection = selection;
            },
            //批假按钮
            handleStatusChange(row) {
                this.form = row
                leave.editLeave(row).then(res => {
                    if (res.code === 200) {
                        if (row.status === true) {//批假成功
                            //得到合适的顶班人员
                            leave.getMatchStaff(row.userId, row.startDate).then(res => {
                                if (res.data !== null) {
                                    this.matchStaffVisible = true
                                    this.matchStaffs = res.data
                                    this.$message.success("批假成功")
                                }
                            })
                        } else {//取消批假
                            this.$message.warning("取消批假")
                            //有顶班人员
                            if (row.replaceUserId) {
                                let scheduleRow = {
                                    userId: row.replaceUserId,
                                    scheduleDate: row.startDate,
                                    shiftId: 0,
                                }
                                schedule.removeSchedule(scheduleRow).then(res => {
                                    this.$message.warning("删除顶班人员")
                                    //刷新页面
                                    this.getList()
                                })
                            }
                        }
                    }
                })
            },
            //重置查询表单
            resetData() {
                this.searchObj = {}
                this.getList()
            },
            //确认顶班
            replaceSchedule(row) {
                leave.replaceSchedule(this.form.id, this.form.userId, row.id, this.form.startDate)
                    .then(res => {
                        if (res.code === 200) {
                            this.$message.success("顶班成功")
                            //关闭窗口
                            this.matchStaffVisible = false
                            //刷新页面
                            this.getList()
                        }
                    })
            },
            //获得请假信息
            getList(current = 1) {
                this.current = current
                leave.findPageLeave(this.current, this.limit, this.level, this.hospitalId, this.departmentId, this.searchObj)
                    .then(res => {
                        this.tableData = res.data.records
                        this.total = res.data.total
                    }).catch(res => {
                    console.error(res)
                })
            },
            //保存请假
            submitSave() {
                leave.saveLeave(this.form).then(res => {
                    if (res.code === 200) {
                        this.$message.success("保存成功")
                        //关闭窗口
                        this.dialogFormVisible = false
                        //刷新页面
                        this.getList()
                    }
                })
            },
            //修改请假
            submitEdit() {
                leave.editLeave(this.form).then(res => {
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
            //删除请假
            removeLeave(id) {
                this.$confirm('此操作将永久删除请假信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//确定执行then方法
                    //调用接口
                    leave.deleteLeaveById(id)
                        .then(res => {
                            this.$message.success("删除成功")
                            //刷新页面
                            this.getList()
                        })
                })
            },
            //批量删除请假
            batchRemoveLeave() {
                this.$confirm('此操作将永久删除多条请假信息, 是否继续?', '提示', {
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
                    leave.batchDeleteLeave(idList)
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
