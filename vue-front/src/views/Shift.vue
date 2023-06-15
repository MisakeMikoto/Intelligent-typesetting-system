<template>
    <div>
        <!-- 查询框开始 -->
        <div style="padding: 10px 0;">
            <el-select style="width: 200px" filterable placeholder="请选择班次" v-model="searchObj.shiftName"
                       class="ml-5" clearable>
                <el-option
                        v-for="item in shiftOptions"
                        :key="item.id"
                        :label="item.shiftName"
                        :value="item.shiftName">
                </el-option>
            </el-select>
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

        <div style="margin: 10px 0" float="left">
            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleAddShift()">新增
            </el-button>
            <el-button type="danger" icon="el-icon-remove-outline" @click="batchRemoveShift()">批量删除
            </el-button>
            <el-tag type="danger" style="margin-left:5px;">两班制不能排小夜班</el-tag>
        </div>

        <!-- 表格开始 -->
        <el-table :data="tableData" border stripe
                  :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange"
                  :cell-style="{textAlign:'center'}" :header-cell-style="{textAlign:'center'}">
            <el-table-column type="selection" width="40" align="center"></el-table-column>
            <el-table-column prop="id" label="ID" width="40"></el-table-column>
            <el-table-column prop="shiftName" label="班次名称">
                <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>医院编号: {{ scope.row.hospitalId }}</p>
                        <p>医院名称: {{ scope.row.hospitalName }}</p>
                        <p v-if="scope.row.departmentId!=null">科室编号: {{ scope.row.departmentId }}</p>
                        <p v-if="scope.row.departmentName!=null">科室名称: {{ scope.row.departmentName }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium" type="warning">{{ scope.row.shiftName }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column prop="shiftSign" label="班次标识"></el-table-column>
            <el-table-column prop="startTime" label="开始时间" width="130"></el-table-column>
            <el-table-column prop="endTime" label="结束时间" width="130"></el-table-column>
            <el-table-column prop="duration" label="班次时长(h)"></el-table-column>
            <el-table-column prop="nurseNumber" label="需要护士人数"></el-table-column>
            <el-table-column prop="carerNumber" label="需要护工人数"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
            <el-table-column prop="updateTime" label="修改时间" width="150"></el-table-column>
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="success" icon="el-icon-edit" @click="handleEditShift(scope.row)">编辑
                    </el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="removeShift(scope.row.id)">删除
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
        <el-dialog title="班次信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" :model="form" ref="shiftForm">
                <el-form-item label="班次名称">
                    <el-input v-model="form.shiftName" autocomplete="off"></el-input>
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
                <el-form-item label="科室名称" v-if="departmentId==null">
                    <el-select v-model="form.departmentId" filterable style="width: 100%">
                        <el-option
                                v-for="item in departmentOptions"
                                :key="item.id"
                                :label="item.departmentName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="科室名称" v-else>
                    <el-select v-model="form.departmentId" disabled filterable style="width: 100%">
                        <el-option
                                v-for="item in departmentOptions"
                                :key="item.id"
                                :label="item.departmentName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="选择时间">
                    <el-time-picker
                            placeholder="开始时间"
                            value-format="HH:mm:ss"
                            v-model="form.startTime"
                            style="width: 100%"></el-time-picker>
                </el-form-item>
                <el-form-item label="结束时间">
                    <el-time-picker
                            placeholder="结束时间"
                            value-format="HH:mm:ss"
                            v-model="form.endTime"
                            style="width: 100%"></el-time-picker>
                </el-form-item>
                <el-form-item label="护士人数">
                    <el-input v-model="form.nurseNumber" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="护工人数">
                    <el-input v-model="form.carerNumber" autocomplete="off"></el-input>
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
    import shift from "@/api/shift";
    import department from "@/api/department";

    export default {
        name: "Shift",
        data() {
            return {
                current: 1,//当前页
                limit: 10,//每页显示记录数
                hospitalId: 0,//医院id
                departmentId: 0,//科室id
                departmentName: "",//科室名称
                searchObj: {},//条件封装对象
                tableData: [],//所有数据
                total: 0,//总记录数
                form: {},//数据表单
                showForm: {},//展示表单
                dialogFormVisible: false,//展示表单
                multipleSelection: [],//多选框
                departmentOptions: [],//科室下拉菜单
                shiftOptions: [],//班次下拉菜单
                shiftSignOptions: [
                    {label: '白班', value: 1},
                    {label: '小夜班', value: 2},
                    {label: '大夜班', value: 3},
                ],//班次标识
                // rules: {
                // shiftName: [
                //     {required: true, message: '请输入班次名称', trigger: 'blur'},
                // ],
                // departmentName: [
                //     {required: true, message: '请选择科室名称', trigger: 'blur'},
                // ],
                // shiftSign: [
                //     {required: true, message: '请选择班次标识', trigger: 'blur'},
                // ],
                // startTime: [
                //     {required: true, message: '请选择开始时间', trigger: 'blur'},]
                // ,
                // endTime: [
                //     {required: true, message: '请输入结束时间', trigger: 'blur'},
                // ],
                // nurseNumber: [
                //     {required: true, message: '请输入需要的护士人数', trigger: 'blur'},
                // ],
                // carerNumber: [
                //     {required: true, message: '请输入需要的护工人数', trigger: 'blur'},
                // ],
                // }
            }
        },
        created() {
            //初始化数据
            this.initialize()

            //获得科室信息
            this.getDepartmentInformation()

            //打开页面显示数据
            this.getList()
        },
        methods: {
            //初始化数据
            initialize() {
                //从本地存储空间获取信息
                this.hospitalId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).hospitalId : 0
                this.departmentId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).departmentId : 0
                console.log('this.departmentId = ' + this.departmentId)
                console.log(localStorage.getItem("user"))
            },
            //切换每页数据量
            handleSizeChange(limit) {
                this.limit = limit
                this.getList()
            },
            //新增班次按钮
            handleAddShift() {
                this.dialogFormVisible = true
                this.form = {}
                this.form.departmentId = this.departmentId
            },
            //编辑班次按钮
            handleEditShift(row) {
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
            //获得科室信息
            getDepartmentInformation() {
                //根据hospitalId获取科室
                department.getDepartmentByHospitalId(this.hospitalId)
                    .then(res => {
                        this.departmentOptions = res.data.records
                    })

                //根据departmentId获取班次
                shift.getShiftByDepartmentId(this.departmentId)
                    .then(res => {
                        this.shiftOptions = res.data.total
                    })

                if (this.departmentId != null) {
                    //根据departmentId获取科室名称
                    department.getDepartmentById(this.departmentId)
                        .then(res => {
                            this.departmentName = res.data.departmentName
                        })
                }
            },
            //获得班次信息
            getList(current = 1) {
                this.current = current
                shift.findPageShift(this.current, this.limit, this.hospitalId, this.departmentId, this.searchObj)
                    .then(res => {
                        this.tableData = res.data.records
                        this.total = res.data.total
                    }).catch(res => {
                    console.error(res)
                })
            },
            //保存班次
            submitSave() {
                this.$refs['shiftForm'].validate((valid) => {
                    if (valid) {//表单校验合法
                        shift.saveShift(this.form).then(res => {
                            if (res) {
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
            //修改班次
            submitEdit() {
                this.$refs['shiftForm'].validate((valid) => {
                    if (valid) {//表单校验合法
                        shift.editShift(this.form).then(res => {
                            if (res) {
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
                if (!this.form.id) {//没有id，做添加
                    this.submitSave();
                } else {//修改
                    this.submitEdit()
                }
            },
            //删除班次
            removeShift(id) {
                this.$confirm('此操作将永久删除班次信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//确定执行then方法
                    //调用接口
                    shift.deleteShiftById(id)
                        .then(res => {
                            this.$message.success("删除成功")
                            //刷新页面
                            this.getList()
                        })
                })
            },
            //批量删除班次
            batchRemoveShift() {
                this.$confirm('此操作将永久删除多条班次信息, 是否继续?', '提示', {
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
                    shift.batchDeleteShift(idList)
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
