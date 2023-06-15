<template>
    <div>
        <!-- 查询框开始 -->
        <div style="padding: 10px 0;">
            <el-select style="width: 200px" filterable placeholder="请选择角色" v-model="searchObj.roleName" clearable>
                <el-option
                        v-for="item in roleOptions"
                        :key="item.id"
                        :label="item.roleName"
                        :value="item.roleName">
                </el-option>
            </el-select>
            <el-button class="ml-5" type="primary" icon="el-icon-search" @click="getList()">查询
            </el-button>
            <el-button type="primary" plain icon="el-icon-refresh-right" @click="resetData()">重置</el-button>
        </div>
        <!-- 查询框结束 -->

        <div style="margin: 10px 0">
            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleAddRole()">新增
            </el-button>
            <el-button type="danger" icon="el-icon-remove-outline" @click="batchRemoveRole()">批量删除
            </el-button>
        </div>

        <!-- 表格开始 -->
        <el-table :data="tableData" border stripe
                  :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange"
                  :cell-style="{textAlign:'center'}" :header-cell-style="{textAlign:'center'}">
            <el-table-column type="selection" width="40" align="center"></el-table-column>
            <el-table-column prop="id" label="ID" width="40"></el-table-column>
            <el-table-column prop="roleName" label="名称" width="100"></el-table-column>
            <el-table-column prop="level" label="等级" width="150"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
            <el-table-column prop="updateTime" label="修改时间" width="150"></el-table-column>

            <el-table-column label="操作" width="300" align="center">
                <template slot-scope="scope">
                    <el-button type="info" icon="el-icon-menu" @click="handleDistributeMenu(scope.row)">分配菜单
                    </el-button>
                    <el-button type="success" icon="el-icon-edit" @click="handleEditRole(scope.row)">编辑
                    </el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="removeRole(scope.row.id)">删除
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
        <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form label-width="80px" v-model="form">
                <el-form-item label="角色">
                    <el-input v-model="form.roleName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="等级">
                    <el-input v-model="form.level" autocomplete="off"></el-input>
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

        <!-- 分配菜单表单开始 -->
        <el-dialog title="菜分配单" :visible.sync="menuDialogVisible" width="30%">
            <el-tree
                    :props="props"
                    :data="menuData"
                    node-key="id"
                    ref="tree"
                    :default-expanded-keys="[2,3]"
                    :default-checked-keys=this.checks
                    show-checkbox>
                <span class="custom-tree-node" slot-scope="{node,data}">
                    <span><i :class="data.icon"></i> {{data.menuName}}</span>
                </span>
            </el-tree>
            <div slot="footer" class="dialog-footer">
                <el-button @click="menuDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveRoleMenu()">保 存</el-button>
            </div>
        </el-dialog>
        <!-- 分配菜单表单结束 -->
    </div>
</template>

<script>
    import role from "@/api/role";
    import menu from "@/api/menu";

    export default {
        name: "Role",
        data() {
            return {
                current: 1,//当前页
                limit: 10,//每页显示记录数
                searchObj: {},//条件封装对象
                tableData: [],//所有数据
                userData: [],//所有角色数据
                total: 0,//总记录数
                form: {},//数据表单
                showForm: {},//展示表单
                dialogFormVisible: false,//展示表单
                menuDialogVisible: false,//展示分配菜单表单
                roleOptions: [],//角色选择下拉菜单
                multipleSelection: [],//多选框
                menuData: [],//分配菜单
                props: {
                    label: 'menuName'
                },
                roleId: 0,//角色id
                roleName: "",//角色名称
                checks: [],//选中的菜单
                menuIds: [],//所有menuId集合
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
            //新增角色按钮
            handleAddRole() {
                this.dialogFormVisible = true
                this.form = {}
            },
            //编辑角色按钮
            handleEditRole(row) {
                this.form = row
                this.dialogFormVisible = true
            },
            //批量选择
            handleSelectionChange(selection) {
                this.multipleSelection = selection;
            },
            //菜单分配按钮
            async handleDistributeMenu(row) {
                this.roleId = row.id
                this.roleName = row.roleName

                //获取所有菜单数据
                menu.findAllMenu(this.searchObj).then(res => {
                    this.menuData = res.data
                })

                //通过roleId获得相应角色的所有menusId集合
                role.getRoleMenu(this.roleId).then(res => {
                    this.checks = res.data
                    this.menuIds.forEach(id => {
                        if (!this.checks.includes(id)) {
                            //可能会报错：Uncaught (in promise) TypeError: Cannot read properties of undefined (reading 'setChecked')
                            //使用$nextTick处理未来元素，等到这个元素渲染完成之后，再去使用它
                            this.$nextTick(() => {
                                this.$refs.tree.setChecked(id, false)
                            })
                        }
                    })
                    this.menuDialogVisible = true
                })
            },
            //重置查询表单
            resetData() {
                this.searchObj = {}
                this.getList()
            },
            //获得角色信息
            getList(current = 1) {
                this.current = current
                role.findPageRole(this.current, this.limit, this.searchObj).then(res => {
                    this.tableData = res.data.records
                    this.total = res.data.total

                    //第一次执行时，将所有角色存入roleOptions
                    if (this.roleOptions.length === 0) {
                        this.roleOptions = this.tableData
                    }
                }).catch(res => {
                    console.error(res)
                })

                //获得所有菜单id
                menu.findAllMenuIds().then(res => {
                    this.menuIds = res.data
                })
            },
            //保存菜单和角色之间的关系
            saveRoleMenu() {
                role.setRoleMenu(this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
                    if (res.code === 200) {
                        this.$message.success("保存成功")
                        this.menuDialogVisible = false

                        //当角色为管理员时，分配完菜单需要退出
                        if (this.roleName === "管理员") {
                            this.$store.commit("logout")
                        }
                    } else {
                        console.error(res)
                    }
                })
            },
            //保存角色
            submitSave() {
                role.saveRole(this.form).then(res => {
                    if (res.code === 200) {
                        this.$message.success("保存成功")
                        //关闭窗口
                        this.dialogFormVisible = false
                        //刷新页面
                        this.getList()
                    }
                })
            },
            //修改角色
            submitEdit() {
                role.editRole(this.form).then(res => {
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
                if (!this.form.id) { //没有id，做添加
                    this.submitSave();
                } else {//修改
                    this.submitEdit()
                }
            },
            //删除角色
            removeRole(id) {
                this.$confirm('此操作将永久删除角色信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => { //确定执行then方法
                    //调用接口
                    role.deleteRoleById(id)
                        .then(res => {
                            this.$message.success("删除成功")
                            //刷新页面
                            this.getList()
                        })
                })
            },
            //批量删除角色
            batchRemoveRole() {
                this.$confirm('此操作将永久删除多条角色信息, 是否继续?', '提示', {
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
                    role.batchDeleteRole(idList).then(res => {
                        this.$message.success("删除成功")
                        //刷新页面
                        this.getList()
                    })
                })
            },
        }
    }
</script>
