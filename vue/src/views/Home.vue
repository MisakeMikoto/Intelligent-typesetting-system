<template>
    <div>
        <!-- 数量数据开始 -->
        <!-- 最高级管理员 -->
        <el-row :gutter="10" style="margin-bottom: 60px" v-if="this.level===0">
            <el-col :span="6">
                <el-card style="color: lightskyblue">
                    <div><i class="el-icon-office-building" style="margin-right: 5px"/>医院总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 20px">
                        {{this.hospitalNumber}}
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card style="color: pink">
                    <div><i class="el-icon-files" style="margin-right: 5px"/>文件总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 20px">
                        {{this.filesNumber}}
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card style="color: orange">
                    <div><i class="el-icon-user" style="margin-right: 5px"/>用户总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 20px">
                        {{this.userNumber}}
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card style="color: forestgreen">
                    <div><i class="el-icon-user-solid" style="margin-right: 5px"/>角色总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 20px">
                        {{this.roleNumber}}
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <!-- 医院管理员 -->
        <el-row :gutter="10" style="margin-bottom: 100px" v-else-if="this.departmentId==null">
            <el-col :span="8">
                <el-card style="color: palevioletred">
                    <div><i class="el-icon-office-building" style="margin-right: 5px"/>科室总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 20px">
                        {{this.departmentNumber}}
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card style="color: orange">
                    <div><i class="el-icon-user" style="margin-right: 5px"/>用户总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 20px">
                        {{this.userNumber}}
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card style="color: forestgreen">
                    <div><i class="el-icon-user-solid" style="margin-right: 5px"/>角色总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 20px">
                        {{this.roleNumber}}
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <!-- 护士长 -->
        <el-row :gutter="10" style="margin-bottom: 100px" v-else>
            <el-col :span="12">
                <el-card style="color: orange">
                    <div><i class="el-icon-user" style="margin-right: 5px"/>用户总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 20px">
                        {{this.userNumber}}
                    </div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card style="color: forestgreen">
                    <div><i class="el-icon-user-solid" style="margin-right: 5px"/>角色总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold; font-size: 20px">
                        {{this.roleNumber}}
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <!-- 数量数据结束 -->
        <!-- 图表、地图开始 -->
        <el-row>
            <el-col id="china_box" :span="24" align="center" v-if="this.level === 0">
                <div id="china" style="width: 100%; height: 600%"></div>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="12" v-if="this.level !== 0">
                <div id="line" style="width: 650px; height: 450px"></div>
            </el-col>
            <el-col :span="12" v-if="this.level !== 0">
                <div id="pie" style="width: 650px; height: 450px"></div>
            </el-col>
        </el-row>
        <!-- 图表、地图结束 -->
    </div>
</template>

<script>
    import * as echarts from 'echarts';
    import {china} from "../utils/china"

    export default {
        name: "Home",
        data() {
            return {
                level: 0,//权限等级
                hospitalId: 0,//医院id
                departmentId: 0,//科室id
                hospitalNumber: 0,//医院数量
                departmentNumber: 0,//科室数量
                userNumber: 0,//用户总数
                roleNumber: 0,//职位总数
                filesNumber: 0,//文件总数
                text: '',
            }
        },
        created() {
            //初始化数据
            this.initialize()

            //获得数量
            this.getNumber()
        },
        //页面元素渲染完之后再触发
        mounted() {
            if (this.level !== 0) {
                //折线图和柱状图
                var lineChartDom = document.getElementById('line');
                var lineChart = echarts.init(lineChartDom);
                var lineOption = {
                    title: {
                        text: this.text,
                        subtext: '趋势图',
                        left: 'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: []
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [
                        {
                            data: [],
                            type: 'line',
                            axisTick: {
                                alignWithLabel: true
                            },
                        },
                        {
                            data: [],
                            type: 'bar',
                            itemStyle: {
                                color: 'rgba(95,214,248,0.78)'
                            }
                        }
                    ]
                };

                //饼图
                var pieChartDom = document.getElementById('pie');
                var pieChart = echarts.init(pieChartDom);
                var pieOption = {
                    title: {
                        text: this.text,
                        subtext: '比例图',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left'
                    },
                    series: [
                        {
                            type: 'pie',
                            radius: '50%',
                            data: [],
                            emphasis: {
                                itemStyle: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                };
                //执行方法
                this.request.get("/echarts/getList/" + this.level, {
                    params: {
                        hospitalId: this.hospitalId,
                        departmentId: this.departmentId
                    }
                }).then(res => {
                    //折线图和柱状图
                    lineOption.xAxis.data = res.data.x
                    lineOption.series[0].data = res.data.y
                    lineOption.series[1].data = res.data.y
                    lineOption && lineChart.setOption(lineOption);

                    //饼图
                    for (let i = 0, len = res.data.x.length; i < len; i++) {
                        pieOption.series[0].data[i] = {name: res.data.x[i], value: res.data.y[i]}
                    }
                    pieOption && pieChart.setOption(pieOption);
                })
            }
            if (this.level === 0) {
                //地图
                var chinaMapDom = document.getElementById('china');
                var chinaMap = echarts.init(chinaMapDom);
                const mapOption = {
                    title: {
                        text: this.text,
                        subtext: '数据源：凝慧',
                        x: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '省份：{b}<br/>数量：{c}'
                    },
                    dataRange: {
                        min: 0,
                        max: 10,
                        inRange: {
                            color: [
                                '#425caa',
                                '#4575b4',
                                '#74add1',
                                '#abd9e9',
                                '#e0f3f8',
                                '#ffffbf',
                                '#fee090',
                                '#fdae61',
                                '#f46d43',
                                '#d73027',
                                '#a50026'
                            ]
                        },
                        x: 'left',
                        y: 'bottom',
                        text: ['高', '低'],// 文本，默认为数值文本
                        calculable: true,
                    },
                    series: [
                        {
                            type: 'map',
                            mapType: 'china',
                            roam: false,
                            itemStyle: {
                                normal: {label: {show: true}},
                                emphasis: {label: {show: true}}
                            },
                            data: []
                        }
                    ]
                };
                //执行方法
                this.request.get("/echarts/getList/" + this.level, {
                    params: {
                        hospitalId: this.hospitalId,
                        departmentId: this.departmentId
                    }
                }).then(res => {
                    for (let i = 0, len = res.data.x.length; i < len; i++) {
                        mapOption.series[0].data[i] = {name: res.data.x[i], value: res.data.y[i]}
                    }
                    chinaMap.setOption(mapOption)
                })
            }
        },
        methods: {
            //初始化数据
            initialize() {
                //从本地存储空间获取信息
                this.level = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).level : 0
                this.hospitalId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).hospitalId : 0
                this.departmentId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).departmentId : 0
                if (this.hospitalId == null) {
                    this.text = '使用本系统医院数量'
                } else if (this.departmentId == null) {
                    this.text = '本医院中所有科室对应的人员数量'
                } else {
                    this.text = '本科室中所有角色对应的人员数量'
                }
            },
            //获得数量
            getNumber() {
                //获得总数
                this.request.get("/echarts/getNumber", {
                    params: {
                        hospitalId: this.hospitalId,
                        departmentId: this.departmentId
                    }
                }).then(res => {
                    this.hospitalNumber = res.data[0]
                    this.departmentNumber = res.data[1]
                    this.userNumber = res.data[2]
                    this.roleNumber = res.data[3]
                    this.filesNumber = res.data[4]
                })
            }
        }
    }
</script>
