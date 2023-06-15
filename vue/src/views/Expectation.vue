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
            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleAddExpectation()">新增
            </el-button>
            <el-button type="danger" icon="el-icon-remove-outline" @click="batchRemoveExpectation()">批量删除
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
            <el-table-column prop="startDate" label="期望开始日期"></el-table-column>
            <el-table-column prop="endDate" label="期望结束日期"></el-table-column>
            <el-table-column prop="expectedDays" label="期望天数"></el-table-column>
            <el-table-column prop="shiftName" label="班次名称">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>医院编号: {{ scope.row.hospitalId }}</p>
                        <p>医院名称: {{ scope.row.hospitalName }}</p>
                        <p v-if="scope.row.departmentId!=null">科室编号: {{ scope.row.departmentId }}</p>
                        <p v-if="scope.row.departmentName!=null">科室名称: {{ scope.row.departmentName }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium" type="warning" v-if="scope.row.shiftSign===1">白班</el-tag>
                            <el-tag size="medium" type="warning" v-if="scope.row.shiftSign===2">小夜班</el-tag>
                            <el-tag size="medium" type="warning" v-if="scope.row.shiftSign===3">大夜班</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column prop="reason" label="期望原因"></el-table-column>
            <el-table-column label="采用状态" align="center" width="70">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ccc"
                               @change="handleStatusChange(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="完成状态" align="center">
                <template slot-scope="scope">
                    <el-tag size="medium" type="warning" v-if="scope.row.status==true && scope.row.complete === false">未完成</el-tag>
                    <el-tag size="medium" type="success" v-else-if="scope.row.status==true && scope.row.complete === true">已完成</el-tag>
                    <el-tag size="medium" type="info" v-else-if="scope.row.status==false">待采用</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
            <el-table-column prop="updateTime" label="修改时间" width="150"></el-table-column>

            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="success" icon="el-icon-edit" @click="handleEditExpectation(scope.row)">编辑
                    </el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="removeExpectation(scope.row.id)">删除
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
        <el-dialog title="期望信息" :visible.sync="dialogFormVisible" width="30%">
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
                <el-form-item label="班次标识">
                    <el-select v-model="form.shiftSign" filterable style="width: 100%">
                        <el-option
                                v-for="item in shiftSignOptions"
                                :key="item.id"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="期望原因">
                    <el-input type="textarea" :rows="2" placeholder="请输入期望原因" v-model="form.reason"
                              autocomplete="off"></el-input>
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
    import expectation from "@/api/expectation";

    export default {
        name: "Expectation",
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
                shiftSignOptions: [
                    {label: '白班', value: 1},
                    {label: '小夜班', value: 2},
                    {label: '大夜班', value: 3},
                ],//班次标识
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
            //新增期望按钮
            handleAddExpectation() {
                this.dialogFormVisible = true
                this.form = {}
            },
            //编辑期望按钮
            handleEditExpectation(row) {
                this.form = row
                this.dialogFormVisible = true
            },
            //批量选择
            handleSelectionChange(selection) {
                this.multipleSelection = selection;
            },
            //期望采用按钮
            handleStatusChange(row) {
                expectation.editExpectation(row).then(res => {
                    if (res.code === 200) {
                        if (row.status === true) {
                            this.$message.success("采用期望成功")
                        } else {
                            this.$message.warning("取消采用")
                        }
                    }
                })
            },
            //重置查询表单
            resetData() {
                this.searchObj = {}
                this.getList()
            },
            //获得期望信息
            getList(current = 1) {
                this.current = current
                expectation.findPageExpectation(this.current, this.limit, this.level, this.hospitalId, this.departmentId, this.searchObj)
                    .then(res => {
                        console.log(res.data.records)
                        this.tableData = res.data.records
                        this.total = res.data.total
                    }).catch(res => {
                    console.error(res)
                })
            },
            //保存期望
            submitSave() {
                expectation.saveExpectation(this.form).then(res => {
                    if (res.code === 200) {
                        this.$message.success("保存成功")
                        //关闭窗口
                        this.dialogFormVisible = false
                        //刷新页面
                        this.getList()
                    }
                })
            },
            //修改期望
            submitEdit() {
                expectation.editExpectation(this.form).then(res => {
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
            //删除期望
            removeExpectation(id) {
                this.$confirm('此操作将永久删除期望信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//确定执行then方法
                    //调用接口
                    expectation.deleteExpectationById(id)
                        .then(res => {
                            this.$message.success("删除成功")
                            //刷新页面
                            this.getList()
                        })
                })
            },
            //批量删除期望
            batchRemoveExpectation() {
                this.$confirm('此操作将永久删除多条期望信息, 是否继续?', '提示', {
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
                    expectation.batchDeleteExpectation(idList)
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
