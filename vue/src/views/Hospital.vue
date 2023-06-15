<template>
    <div>
        <!-- 查询框开始 -->
        <div style="padding: 10px 0;">
            <el-input style="width: 200px" placeholder="请输入医院名称" v-model="searchObj.hospitalName"
                      suffix-icon="el-icon-search" class="ml"></el-input>
            <el-button class="ml-5" type="primary" icon="el-icon-search" @click="getList()">查询
            </el-button>
            <el-button type="primary" plain icon="el-icon-refresh-right" @click="resetData()">重置</el-button>
        </div>
        <!-- 查询框结束 -->

        <div style="margin: 10px 0">
            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleAddHospital()">新增
            </el-button>
            <el-button type="danger" icon="el-icon-remove-outline" @click="batchRemoveHospital()">批量删除
            </el-button>
        </div>

        <!-- 表格开始 -->
        <el-table :data="tableData" border stripe
                  :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange"
                  :cell-style="{textAlign:'center'}" :header-cell-style="{textAlign:'center'}">
            <el-table-column type="selection" width="40" align="center"></el-table-column>
            <el-table-column prop="id" label="ID" width="40"></el-table-column>
            <el-table-column prop="hospitalName" label="医院名称"></el-table-column>
            <el-table-column prop="province" label="省"></el-table-column>
            <el-table-column prop="provinceCode" label="省id"></el-table-column>
            <el-table-column prop="cityCode" label="市id"></el-table-column>
            <el-table-column prop="districtCode" label="区id"></el-table-column>
            <el-table-column prop="address" label="详细地址"></el-table-column>
            <el-table-column prop="intro" label="医院简介" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="医院logo" width="100" align="center">
                <template slot-scope="scope" v-if="scope.row.hospitalLogo">
                    <el-button type="primary" @click="preview(scope.row.hospitalLogo)">预览</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
            <el-table-column prop="updateTime" label="修改时间" width="150"></el-table-column>

            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="success" icon="el-icon-edit" @click="handleEditHospital(scope.row)">编辑
                    </el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="removeHospital(scope.row.id)">删除
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
        <el-dialog title="医院信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" v-model="form">
                <el-form-item label="医院名称">
                    <el-input v-model="form.hospitalName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="省份">
                    <el-input v-model="form.province" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="详细介绍">
                    <el-input v-model="form.intro" autocomplete="off"></el-input>
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
    import hospital from "@/api/hospital";

    export default {
        name: "Hospital",
        data() {
            return {
                current: 1,//当前页
                limit: 10,//每页显示记录数
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
            //打开页面显示数据
            this.getList()
        },
        methods: {
            //切换每页数据量
            handleSizeChange(limit) {
                this.limit = limit
                this.getList()
            },
            //新增医院按钮
            handleAddHospital() {
                this.dialogFormVisible = true
                this.form = {}
            },
            //编辑医院按钮
            handleEditHospital(row) {
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
            //获得医院信息
            getList(current = 1) {
                this.current = current
                hospital.findPageHospital(this.current, this.limit, this.searchObj).then(res => {
                    this.tableData = res.data.records
                    this.total = res.data.total
                }).catch(res => {
                    console.error(res)
                })
            },
            preview(url) {
                // console.log(url)
                window.open('https://file.keking.cn/onlinePreview?url=' + encodeURIComponent(window.btoa((url))))
            },
            //保存医院
            submitSave() {
                hospital.saveHospital(this.form).then(res => {
                    if (res.code === 200) {
                        this.$message.success("保存成功")
                        //关闭窗口
                        this.dialogFormVisible = false
                        //刷新页面
                        this.getList()
                    }
                })
            },
            //修改医院
            submitEdit() {
                hospital.editHospital(this.form).then(res => {
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
            //删除医院
            removeHospital(id) {
                this.$confirm('此操作将永久删除医院信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//确定执行then方法
                    //调用接口
                    hospital.deleteHospitalById(id)
                        .then(res => {
                            this.$message.success("删除成功")
                            //刷新页面
                            this.getList()
                        })
                })
            },
            //批量删除医院
            batchRemoveHospital() {
                this.$confirm('此操作将永久删除多条医院信息, 是否继续?', '提示', {
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
                    hospital.batchDeleteHospital(idList)
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
