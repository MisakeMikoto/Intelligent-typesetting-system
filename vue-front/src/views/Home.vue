<template>
    <div>
        <el-container style='height: 100%'>
            <el-main>
                <!-- 排班结果 -->
                <div style='background-color: white;padding-left: 10px;padding-top: 5px;width: 100%;'>
                    <div style='background-color: #ffffff'>
                        <div style='font-size: 20px;text-align: center;font-weight: bold'>排班结果总览</div>
                        <el-table :data="workData" border stripe :header-cell-class-name="'headerBg'">
                            <el-table-column prop="id" label="ID" width="50"></el-table-column>
                            <el-table-column prop="username" label="工号"></el-table-column>
                            <el-table-column prop="name" label="姓名"></el-table-column>
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
                            <el-table-column prop="createTime" label="创建时间" width="200"></el-table-column>
                            <el-table-column prop="updateTime" label="修改时间" width="200"></el-table-column>
                        </el-table>
                    </div>
                </div>

                <!--放在第一位，触发滚动-->
                <el-backtop></el-backtop>
                <h2>{{valueMouth}}排班表</h2>
                <div style='padding-left: 5px;'>
                    <!--白的：空闲； 黄色：日班  灰色：夜班-->
                    <div>
                        <div style='float: left'>
                            <div style='float: left;padding-right: 3px'>班次状态：</div>
                            <div v-for="(item, index) in shiftData" :key="index" style='float: left'>
                                <el-avatar v-if="item.shiftSign === 1" class="first" shape='square' :size='20'
                                           style='float: left'></el-avatar>
                                <el-avatar v-if="item.shiftSign === 2" class="second" shape='square' :size='20'
                                           style='float: left'></el-avatar>
                                <el-avatar v-if="item.shiftSign === 3" class="third" shape='square' :size='20'
                                           style='float: left'></el-avatar>
                                <div style='float: left;padding-left: 5px;padding-right: 25px'>{{item.shiftName}}</div>
                            </div>
                            <el-avatar class="relax" shape='square' :size='20' style='float: left'></el-avatar>
                            <div style='float: left;padding-left: 5px;padding-right: 25px'>请假/休息</div>

                            <!-- <div style='float: left'>
                                <el-avatar shape='square' :size='20' class='first' style='float: left'></el-avatar>
                                <div style='float: left;padding-left: 5px;padding-right: 25px'>第一班</div>
                            </div>

                            <div style='float: left'>
                                <el-avatar shape='square' :size='20' class='second' style='float: left'></el-avatar>
                                <div style='float: left;padding-left: 5px;padding-right: 25px'>第二班</div>
                            </div>

                            <div style='float: left'>
                                <el-avatar shape='square' :size='20' class='third' style='float: left'></el-avatar>
                                <div style='float: left;padding-left: 5px;padding-right: 25px'>第三班</div>
                            </div> -->

                        </div>
                    </div>
                </div>

                <!--@click.native='selectMouth'：使用自定义的监听方法-->

                <div style='margin-left: 1000px' v-if="this.level < 2">
                    <!--选择月份框-->
                    <div>
                        <el-date-picker
                                v-model='valueMouth'
                                type='month'
                                value-format='yyyy-MM'
                                placeholder='请输入年/月'
                                @change='selectMouth'
                                style='margin-right: 10px'
                        >
                        </el-date-picker>
                    </div>
                    <!--选择科室框-->
                    <div>
                        <el-select v-model='value' placeholder='请选择'>
                            <el-option
                                    v-for='item in departmentData'
                                    :key='item.id'
                                    :label='item.departmentName'
                                    :value='item.id'
                                    style='margin-right: 10px'>
                            </el-option>
                        </el-select>
                        <el-button type='primary' icon='el-icon-check' size='medium'
                                   @click.native='submitChange(value)'>完成
                        </el-button>
                        <el-button type='success' icon='el-icon-close' size='medium' @click.native='cancelChange()'>取消
                        </el-button>
                    </div>
                </div>

                <!--选择月份框-->
                <div v-else style='margin-left: 1000px'>
                    <div>
                        <el-date-picker
                                v-model='valueMouth'
                                type='month'
                                value-format='yyyy-MM'
                                placeholder='请输入年/月'
                                @change='selectMouth'
                                style='margin-right: 10px'
                        >
                        </el-date-picker>
                        <el-button type='primary' icon='el-icon-check' size='medium'
                                   @click.native='submitChange(departmentId)'>完成
                        </el-button>
                        <el-button type='success' icon='el-icon-close' size='medium' @click.native='cancelChange()'>取消
                        </el-button>
                    </div>
                </div>

                <div>
                    <div style='margin-bottom: 20px;padding-top: 10px;width: 1400px'>
                        <span style='padding-left:5px;font-size: small'>姓名 \ 日期</span>
                        <!--测试数据rawData-->
                        <!--数据库数据tableData-->
                        <span v-for='(column, index) in days' :key="index" style="text-align: center">

        <el-avatar v-if='(index+1) /10 < 1' style='margin: 1px' shape='square' :size="dateSize">
          0{{index+1}}
        </el-avatar>
        <el-avatar v-else-if='index < days' style='margin: 1px' shape='square' :size="dateSize ">
          {{(index+1)}}
        </el-avatar>

      </span>
                    </div>

                    <!-- 展示数据 -->
                    <div>
                        <div v-if='infosState === false'>

                            <!--手动修改排班数据，点击排班头像弹窗选择排班状态-->
                            <el-dialog
                                    title="手动修改排班数据"
                                    :visible.sync="dialogVisible"
                                    width="15%"
                                    :before-close="handleClose">
                                <el-select v-model='value' placeholder='请选择'>
                                    <el-option
                                            v-for='item in scheduleOptions'
                                            :key='item.value'
                                            :label='item.label'
                                            :value='item.value'
                                            style='margin-right: 10px'>
                                    </el-option>
                                </el-select>
                                <span slot="footer" class="dialog-footer">
                  <el-button @click="dialogVisible = false">取 消</el-button>
                  <el-button v-if="ifUpdate(value) == 1" type="primary" @click="deleteSchedule(value)">确 定</el-button>
                  <el-button v-if="ifUpdate(value) == 2" type="primary" @click="uploadSchedule(value)">确 定</el-button>
                  <el-button v-if="ifUpdate(value) == 3" type="primary" @click="updateSchedule(value)">确 定</el-button>
                </span>
                            </el-dialog>

                            <!--数据库数据tableData-->
                            <div v-for="(Infos,NameIndex) in tableData" :key="NameIndex"
                                 style='width: 1400px;background-color: #58ccde;'>
                                <el-tooltip effect='dark' placement='top'
                                            :content='Infos.name + " " + Infos.roleName'>
                                    <div v-if='userLimit(NameIndex)'
                                         style='float:left;text-align: center;color: #ffffff;margin: 1px;padding-right: 29px;'
                                         shape='square' :size='size'>
                                        <el-avatar v-if="Infos.roleName === '护士长'" style="color: red ;" shape='square'
                                                   :size='size'>
                                            {{Infos.name}}
                                        </el-avatar>
                                        <el-avatar v-else style="" shape='square' :size='size'>
                                            {{Infos.name}}
                                        </el-avatar>
                                    </div>

                                </el-tooltip>
                                <span v-for="(infosColumn, shiftSign, index) in Infos" :key='index' style='float:left'
                                      @click="ClickSchedule(Infos.name, Infos.date, Infos.shiftSign)">
                      <el-tooltip effect='dark' placement='top'
                                  :content='Infos.date + " " + Infos.name + " "'>

                          <el-avatar v-if='infosColumn == 0' class='relax' shape='square' :size='size'>
                            休
                          </el-avatar>

                          <el-avatar v-if='infosColumn == 1' class='first' shape='square' :size='size'>
                            白
                          </el-avatar>

                          <el-avatar v-if='infosColumn == 2' class='second' shape='square' :size='size'>
                            小
                          </el-avatar>

                          <el-avatar v-if='infosColumn == 3' class='third' shape='square' :size='size'>
                            大
                          </el-avatar>

                            <el-avatar v-if='infosColumn == 4' class='relax' shape='square' :size='size'>
                            假
                          </el-avatar>
                        </el-tooltip>
                  </span>
                            </div>
                        </div>
                    </div>

                </div>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import schedule from '@/api/schedule';
    import workload from '@/api/workload';
    import shift from '@/api/shift'
    import department from '@/api/department'

    export default {
        name: 'Home',
        components: {},
        data() {
            return {
                //图标头像大小
                size: 40,
                //日期头像大小
                dateSize: 40,
                //当前页的高度
                windowHeight: 0,
                //当前页的宽度
                windowWidth: 0,
                week: '',// 本周周几
                date_show: '', //本日日期
                //判断是否进行手动排班，是则显示弹窗
                dialogVisible: false,
                //排序Index
                i: 0,
                //用户id
                userId: "",
                //更改前班次id
                shiftId: "",
                //更改后班次id
                shiftId1: "",
                //用户姓名
                userName: '墨菲斯',
                //日期
                scheduleDate: '',
                //条件封装对象
                searchObj: {},
                //所有数据
                tableData: [],
                //总览表
                totalTable: [],
                //开始排班日期
                startDate: '',
                //结束排班日期
                endDate: '',
                //科室Id
                departmentId: 2,
                //选择排班的年月份
                valueMouth: '',
                //个人排班对应的年份
                year: '',
                //个人排班对应的月份
                month: '',
                //个人排班对应的天
                day: '01',
                //个人排班对应的天数
                days: 0,
                // //切换数据源
                infosState: false,
                //缓存数据
                infosCopy: this.tableData,
                //获取角色信息
                usersData: [],
                options: [{
                    value: '2',
                    label: '内科'
                }, {
                    value: '3',
                    label: '外科'
                }, {
                    value: '4',
                    label: '儿科'
                }, {
                    value: '5',
                    label: '妇产科'
                }, {
                    value: '6',
                    label: '耳鼻喉科'
                }, {
                    value: '7',
                    label: '儿科'
                }
                ],
                scheduleOptions: [{
                    value: '0',
                    label: '休息'
                }, {
                    value: '1',
                    label: '第一班'
                }, {
                    value: '2',
                    label: '第二班'
                }, {
                    value: '3',
                    label: '第三班'
                }],
                value: '',
                //数据表单
                form: {
                    scheduleDate: "",
                    shiftId: "",
                    userId: 0,
                },
                message: "hello",
                level: 0,//权限等级
                current: 1,//当前页
                limit: 10000,//每页显示记录数
                hospitalId: 1,//医院id
                //工作量数据
                workData: [],
                //班次数据
                shiftData: [],
                //科室数据
                departmentData: [],
            }
        },
        methods: {
            //初始化数据
            initialize() {
                let departmentId = 2
                //从本地存储空间获取信息
                this.level = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).level : 0
                this.hospitalId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).hospitalId : 0
                this.departmentId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).departmentId : 0

                if (this.departmentId == null) {
                    this.departmentId = departmentId
                }
                console.log('this.level = ' + this.level)
                console.log('this.departmentId = ' + this.departmentId)
                this.getWorkLoad(this.departmentId)
                this.getList(this.departmentId)
                this.getShift(this.departmentId)
                this.getDepartment(this.hospitalId)
            },

            //根据hospitalId获取科室数据
            getDepartment(hospitalId) {
                this.hospitalId = hospitalId
                department.getDepartmentByHospitalId(this.hospitalId)
                    .then(res => {
                        this.departmentData = res.data
                        console.log(this.departmentData)
                    }).catch(res => {
                    console.error(res)
                })
            },

            //获取工作量信息
            getWorkLoad(departmentId) {
                this.departmentId = departmentId
                workload.findPageWorkload(this.current, this.limit, this.level - 1, this.hospitalId, this.departmentId, this.searchObj)
                    .then(res => {
                        this.workData = res.data.records
                        this.total = res.data.total
                        console.log(this.workData)
                    }).catch(res => {
                    console.error(res)
                })
            },

            //获取班次信息
            getShift(departmentId) {
                this.departmentId = departmentId
                shift.getShiftByDepartmentId(this.departmentId)
                    .then(res => {
                        this.shiftData = res.data
                        console.log(this.shiftData)
                    }).catch(res => {
                    console.error(res)
                })
            },

            //判断下一个姓名是否重复,限制数据中的对象重复遍历
            userLimit(NameIndex) {
                if (NameIndex === (this.tableData.length + 1)) {
                    return false
                }
                this.name = this.tableData[NameIndex].name
                if (NameIndex >= 1) {
                    this.name2 = this.tableData[NameIndex - 1].name
                }

                if (NameIndex === 0) {
                    return true
                } else if (this.name === this.name2) {
                    return false
                } else {
                    return true
                }
            },

            showInfo(data) {
                //console.log('--' + data + '--')
                console.log('2. this.level = ' + this.level)
            },

            //当选择的日期发生改变
            selectMouth() {
                this.postUrl = 'date:' + this.valueMouth + '-01';
                this.year = this.valueMouth[0] + this.valueMouth[1] + this.valueMouth[2] + this.valueMouth[3]
                this.month = this.valueMouth[5] + this.valueMouth[6]
                this.days = this.getDays()
                this.startDate = this.year + '-' + this.month + '-' + '01'
                console.log('开始日期 = ' + this.startDate)
                this.endDate = this.year + '-' + this.month + '-' + this.days + ''
                console.log('结束日期 = ' + this.endDate)
            },

            //判断选择的月份对应的天数
            getDays() {
                if ((this.year % 4 === 0 && this.year % 100 != 0) || this.yaer % 400 === 0) {
                    switch (this.month) {
                        case '01':
                        case '03':
                        case '05':
                        case '07':
                        case '08':
                        case '10':
                        case '12':
                            return 31
                        case '04':
                        case '06':
                        case '09':
                        case '11':
                            return 30
                        case '02':
                            return 29
                    }
                } else {
                    switch (this.month) {
                        case '01':
                        case '03':
                        case '05':
                        case '07':
                        case '08':
                        case '10':
                        case '12':
                            return 31
                        case '04':
                        case '06':
                        case '09':
                        case '11':
                            return 30
                        case '02':
                            return 28
                    }
                }
            },

            //获得科室排班信息
            getList(departmentId) {
                this.departmentId = departmentId
                schedule.getScheduleByDepartmentIdAndDate(this.departmentId, this.startDate, this.endDate).then(res => {
                    this.loading = false
                    this.tableData = res.data
                    this.totalTable = res.data
                    console.log(this.tableData)
                }).catch(res => {
                    console.log(res)
                })
            },

            //手动排班对应的点击弹窗事件，更新排班数据
            ClickSchedule(username, scheduleDate, shift_id) {
                this.dialogVisible = true
                this.shiftId = shift_id
                this.userName = username

                this.scheduleDate = scheduleDate
                this.getUserIdByUserName(this.userName)
            },

            //处理关闭弹窗请求
            handleClose(done) {
                this.$confirm('确认关闭？')
                    // eslint-disable-next-line no-unused-vars
                    .then(_ => {
                        done();
                    })
                    // eslint-disable-next-line no-unused-vars
                    .catch(_ => {
                    });
            },

            //判断护士排班修改类型
            ifUpdate(value) {
                //如果护士从值班转为休息，返回：1
                if (value == 0) {
                    return 1
                }
                //如果护士从休息转为值班，返回：2
                else if (this.shiftId == 0) {
                    return 2
                }
                //如果护士更换值班班次，返回：3
                else {
                    return 3
                }
            },

            //删除值班信息，护士从上班调整为休息
            deleteSchedule(shift_id) {
                this.dialogVisible = false
                this.form.shiftId = shift_id
                this.form.scheduleDate = this.scheduleDate
                this.form.userId = this.userId
                //连接接口，修改护士对应的排班数据
                schedule.removeSchedule(this.form).then(res => {
                    if (res) {
                        this.$message.success("修改成功")
                        //刷新页面
                        this.getList(this.departmentId)
                    }
                })
            },

            //增加值班信息，护士从休息调整为上班
            uploadSchedule(shift_id) {
                this.dialogVisible = false
                this.shiftId = shift_id
                this.form.shiftId = shift_id
                this.form.scheduleDate = this.scheduleDate
                this.form.userId = this.userId
                //连接接口，修改护士对应的排班数据
                schedule.saveSchedule(this.form).then(res => {
                    if (res) {
                        this.$message.success("修改成功")
                        //刷新页面
                        this.getList(this.departmentId)
                    }
                })
            },
            //修改值班状态
            updateSchedule(shift_id) {
                this.dialogVisible = false
                this.shiftId = shift_id
                this.form.shiftId = shift_id
                this.form.scheduleDate = this.scheduleDate
                this.form.userId = this.userId
                //连接接口，修改护士对应的排班数据
                schedule.editSchedule(this.userId, this.scheduleDate, this.form).then(res => {
                    if (res) {
                        this.$message.success("修改成功")
                        //刷新页面
                        this.getList(this.departmentId)
                    }
                })
            },

            //获取UserId
            getUserIdByUserName(username) {
                console.log(this.usersData)
                for (this.i = 0; this.i < this.workData.length; this.i++) {
                    if (username == this.workData[this.i].name) {
                        this.userId = this.workData[this.i].id
                    }
                }
            },

            //切换数据源为InfosCopy（数据的备份）
            changeState() {
                this.infosState = !this.infosState;
            },
            //向后台提交改变
            submitChange(value) {
                this.departmentId = value
                this.getList(this.departmentId)
                this.getWorkLoad(this.departmentId)
            },
            //取消改变
            cancelChange() {
                //切换页面
                this.infosState = !this.infosState;
            },
            // 获取当前系统日期
            getDataTime() {
                let yy = new Date().getFullYear();
                let mm = new Date().getMonth() + 1;
                let dd = new Date().getDate();

                this.date_show = yy + "-" + mm + "-" + dd

                this.year = yy
                this.month = mm + 1
                this.days = new Date(yy, mm + 1, 0).getDate()
                this.startDate = this.year + '-' + this.month + '-' + '01' + ''
                this.endDate = this.year + '-' + this.month + '-' + this.days + ''
                console.log(this.days)
                console.log('this.startDate = ' + this.startDate)
                console.log('this.endDate = ' + this.endDate)
            },
        },
        created() {
            this.getDataTime()
            this.initialize()
        },
        mounted() {
        }
    };
</script>

<style scoped>
    .relax {
        background-color: #dfdfdc;
        margin: 1px;
        color: black;
    }

    .first {
        background-color: #67C23A;
        margin: 1px;
        color: black;
    }

    .second {
        background-color: #ffac89;
        margin: 1px;
        color: black;
    }

    .third {
        background-color: #409EFF;
        margin: 1px;
        color: black;
    }
</style>
