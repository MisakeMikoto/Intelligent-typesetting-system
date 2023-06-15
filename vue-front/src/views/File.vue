<template>
    <div>
        <!-- 查询框开始 -->
        <div style="padding: 10px 0;">
            <el-input style="width: 200px" placeholder="请输入名称" v-model="searchObj.filesName"
                      suffix-icon="el-icon-search"></el-input>
            <el-button class="ml-5" type="primary" icon="el-icon-search" @click="getList()">查询
            </el-button>
            <el-button type="primary" plain icon="el-icon-refresh-right" @click="resetData()">重置</el-button>
        </div>
        <!-- 查询框结束 -->

        <div style="margin: 10px 0">
            <el-upload :action="'http://' + this.serverIp + '/files/upload'"
                       :show-file-list="false"
                       :on-success="handleUploadSuccess"
                       style="display: inline-block">
                <el-button type="primary" icon="el-icon-upload" style="margin-right: 10px">上传</el-button>
            </el-upload>

            <el-button type="danger" icon="el-icon-remove-outline" @click="batchRemoveFiles()">批量删除
            </el-button>
        </div>
        <!-- 表格开始 -->
        <el-table :data="tableData" border stripe
                  :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange"
                  :cell-style="{textAlign:'center'}" :header-cell-style="{textAlign:'center'}">
            <el-table-column type="selection" width="40" align="center"></el-table-column>
            <el-table-column prop="id" label="ID" width="40"></el-table-column>
            <el-table-column prop="filesName" label="文件名称"></el-table-column>
            <el-table-column prop="type" label="文件类型" width="100"></el-table-column>
            <el-table-column prop="size" label="文件大小(KB)" width="100"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
            <el-table-column prop="updateTime" label="修改时间" width="150"></el-table-column>
            <el-table-column label="启用" align="center" width="70">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ccc"
                               @change="handleStatusChange(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="预览" width="100" align="center">
                <template slot-scope="scope">
                    <el-button type="primary" @click="preview(scope.row.url)">预览</el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="primary" icon="el-icon-download" @click="download(scope.row.url)">下载</el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="removeFiles(scope.row.id)">删除</el-button>
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
    </div>
</template>

<script>
    import file from "@/api/file";
    import {serverIp} from "../../public/config";

    export default {
        name: "File",
        data() {
            return {
                serverIp: serverIp,//ip地址
                current: 1,//当前页
                limit: 10,//每页显示记录数
                searchObj: {},//条件封装对象
                tableData: [],//所有数据
                FilesData: [],//所有文件数据
                total: 0,//总记录数
                form: {},//数据表单
                showForm: {},//展示表单
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
            //上传文件按钮
            handleUploadSuccess(res) {
                if (res.code === 200) {
                    this.$message.success("上传成功")
                    this.getList()
                } else {
                    this.$message.error("上传失败")
                }
            },
            //批量选择
            handleSelectionChange(selection) {
                this.multipleSelection = selection;
            },
            //启用按钮
            handleStatusChange(row) {
                file.updateFiles(row).then(res => {
                    if (res.code === 200) {
                        if (row.status === true) {
                            this.$message.success("启用成功")
                        } else {
                            this.$message.warning(" 取消启用")
                        }
                    } else {
                        this.$message.error("启用失败")
                    }
                })
            },
            //重置查询表单
            resetData() {
                this.searchObj = {}
                this.getList()
            },
            //获得文件信息
            getList(current = 1) {
                this.current = current
                file.findPageFiles(this.current, this.limit, this.searchObj).then(res => {
                    this.tableData = res.data.records
                    this.total = res.data.total
                }).catch(res => {
                    console.error(res)
                })
            },
            preview(url) {
                window.open('https://file.keking.cn/onlinePreview?url=' + encodeURIComponent(window.btoa((url))))
            },
            //下载文件
            download(url) {
                window.open(url)
            },
            //删除文件
            removeFiles(id) {
                this.$confirm('此操作将永久删除文件信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {//确定执行then方法
                    //调用接口
                    file.deleteFilesById(id)
                        .then(res => {
                            this.$message.success("删除成功")
                            //刷新页面
                            this.getList()
                        })
                })
            },
            //批量删除文件
            batchRemoveFiles() {
                this.$confirm('此操作将永久删除多条文件信息, 是否继续?', '提示', {
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
                    file.batchDeleteFiles(idList).then(res => {
                        this.$message.success("删除成功")
                        //刷新页面
                        this.getList()
                    })
                })
            },
        }
    }
</script>
