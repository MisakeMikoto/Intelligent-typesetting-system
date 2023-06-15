<template>
    <div>
        <!-- 查询框开始 -->
        <div style="padding: 10px 0;">
            <el-input style="width: 200px" placeholder="请输入菜单名称" v-model="searchObj.menuName"
                      suffix-icon="el-icon-search"></el-input>
            <el-button class="ml-5" type="primary" icon="el-icon-search" @click="getList()">查询
            </el-button>
            <el-button type="primary" plain icon="el-icon-refresh-right" @click="resetData()">重置</el-button>
        </div>
        <!-- 查询框结束 -->

        <div style="margin: 10px 0">
            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleAddMenu()">新增
            </el-button>
            <el-button type="danger" icon="el-icon-remove-outline" @click="batchRemoveMenu()">批量删除
            </el-button>
        </div>

        <!-- 表格开始 -->
        <el-table :data="tableData" border stripe row-key="id" default-expand-all
                  :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange"
                  :cell-style="{textAlign:'center'}" :header-cell-style="{textAlign:'center'}">
            <el-table-column type="selection" width="40" align="center"></el-table-column>
            <el-table-column prop="id" label="ID" width="100"></el-table-column>
            <el-table-column prop="menuName" label="名称" width="100"></el-table-column>
            <el-table-column label="图标" width="60" class-name="fontSize20" align="center"
                             label-class-name="fontSize12">
                <template slot-scope="scope">
                    <i :class="scope.row.icon"/>
                </template>
            </el-table-column>
            <el-table-column prop="path" label="路径" width="120"></el-table-column>
            <el-table-column prop="pagePath" label="页面路径" width="120"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="200"></el-table-column>
            <el-table-column prop="updateTime" label="修改时间" width="200"></el-table-column>
            <el-table-column label="新增子菜单" width="100" align="center">
                <template slot-scope="scope">
                    <el-button type="primary" icon="el-icon-plus" v-if="!scope.row.pid && !scope.row.path"
                               @click="handleAddMenu(scope.row.id)">新增
                    </el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="success" icon="el-icon-edit" @click="handleEditMenu(scope.row)">编辑
                    </el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="removeMenu(scope.row.id)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 表格结束 -->

        <!-- 表单开始 -->
        <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" v-model="form">
                <el-form-item label="菜单">
                    <el-input v-model="form.menuName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="图标">
                    <el-select style="width: 100%" filterable placeholder="请选择图标" v-model="form.icon" clearable>
                        <el-option
                                v-for="item in options"
                                :key="item.dictName"
                                :label="item.dictName"
                                :value="item.value">

                            <i :class="item.value" style="margin-right: 10px"/>{{item.dictName}}
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="路径">
                    <el-input v-model="form.path" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="页面路径">
                    <el-input v-model="form.pagePath" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input type="textarea" :rows="2" placeholder="请输入描述" v-model="form.description"
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
    import menu from "@/api/menu";
    import role from "@/api/role";

    export default {
        name: "Menu",
        data() {
            return {
                limit: 5,//每页显示记录数
                searchObj: {},//条件封装对象
                tableData: [],//所有数据
                userData: [],//所有菜单数据
                total: 0,//总记录数
                form: {},//数据表单
                showForm: {},//展示表单
                options: [],//dict
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
            //新增菜单按钮
            handleAddMenu(pid) {
                this.dialogFormVisible = true
                this.form = {}
                if (pid) {
                    this.form.pid = pid
                }
                //点击新增菜单按钮，请求获得图标数据
                this.getIcon()
            },
            //编辑菜单按钮
            handleEditMenu(row) {
                this.form = row
                this.dialogFormVisible = true

                //点击编辑菜单按钮，请求获得图标数据
                this.getIcon()
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
            //获得菜单信息
            getList() {
                menu.findAllMenu(this.searchObj).then(res => {
                    this.tableData = res.data
                }).catch(res => {
                    console.error(res)
                })
            },
            //获得图标
            getIcon() {
                //点击编辑菜单按钮，请求获得图标数据
                menu.getIcons().then(res => {
                    this.options = res.data
                })
            },
            //保存菜单
            submitSave() {
                menu.saveMenu(this.form).then(res => {
                    if (res) {
                        this.$message.success("保存成功")
                        //关闭窗口
                        this.dialogFormVisible = false
                        //刷新页面
                        this.getList()
                    } else {
                        this.$message.error("保存失败")
                    }
                })
            },
            //修改菜单
            submitEdit() {
                menu.editMenu(this.form).then(res => {
                    if (res) {
                        this.$message.success("修改成功")
                        //关闭窗口
                        this.dialogFormVisible = false
                        //刷新页面
                        this.getList()
                    } else {
                        this.$message.error("修改失败")
                    }
                })
            },
            //判断添加还是修改
            saveOrUpdate() {
                if (!this.form.id) { //没有id，做添加
                    this.submitSave();
                } else {//修改
                    this.submitEdit()
                }
            },
            //删除菜单
            removeMenu(id) {
                this.$confirm('此操作将永久删除菜单信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => { //确定执行then方法
                    //调用接口
                    menu.deleteMenuById(id)
                        .then(res => {
                            this.$message.success("删除成功")
                            //刷新页面
                            this.getList()
                        })
                })
            },
            //批量删除菜单
            batchRemoveMenu() {
                this.$confirm('此操作将永久删除多条菜单信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => { //确定执行then方法
                    var idList = []
                    //遍历数组得到每个id值，设置到idList里面
                    for (var i = 0; i < this.multipleSelection.length; i++) {
                        var obj = this.multipleSelection[i]
                        var id = obj.id
                        idList.push(id)
                    }
                    //调用接口
                    menu.batchDeleteMenu(idList).then(res => {
                        this.$message.success("删除成功")
                        //刷新页面
                        this.getList()
                    })
                })
            },
        }
    }
</script>
<style>
    .headerBg {
        background: #eee !important;
    }

    .fontSize20 {
        font-size: 20px;
    }

    .fontSize12 {
        font-size: 12px;
    }
</style>
