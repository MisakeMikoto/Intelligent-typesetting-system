<template>
    <div align="center" style="margin: 50px auto">
        <el-card style="width: 650px; border-radius: 10px;">
            <div style="width: 500px;">
                <el-form label-width="100px" style="margin: 20px auto">
                    <div style="margin: 30px 0; text-align: center; font-size: 24px"><b>工作天数</b></div>
                    <el-form-item label="实际上班天数">
                        <el-input size="big" v-model="form.realWorkDays" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="预计上班天数">
                        <el-input size="big" v-model="form.workDays" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="实际晚班次数">
                        <el-input size="big" v-model="form.realEveningShifts" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="预计晚班次数">
                        <el-input size="big" v-model="form.eveningShifts" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="放假天数">
                        <el-input size="big" v-model="form.leftDays" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="年休天数">
                        <el-input size="big" v-model="form.annualLeaveDays" disabled></el-input>
                    </el-form-item>
                </el-form>
            </div>
        </el-card>
    </div>
</template>

<script>
    import workload from "@/api/workload";

    export default {
        name: "myWorkload",
        data() {
            return {
                form: {},//数据表单
                user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : 0,
            }
        },
        created() {
            this.getWorkload()
        },
        methods: {
            //根据userId获得工作量
            getWorkload() {
                workload.getWorkloadByUserId(this.user.id).then(res => {
                    if (res.code === 200) {
                        this.form = res.data
                    } else {
                        console.error(res)
                    }
                })

            },
        }
    }
</script>
