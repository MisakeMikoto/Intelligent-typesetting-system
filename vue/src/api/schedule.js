import request from '@/utils/request'

const api_name = 'schedule'

export default {
    //自动排班
    autoSchedule(departmentId) {
        return request({
            url: `${api_name}/autoSchedule/${departmentId}`,
            method: 'post',
        })
    },
    //条件查询带分页获得排班信息
    findPageSchedule(current, limit, hospitalId, departmentId, searchObj) {
        return request({
            url: `${api_name}/findPageSchedule/${current}/${limit}`,
            method: 'post',
            data: searchObj,
            params: {
                hospitalId: hospitalId,
                departmentId: departmentId,
            }
        })
    },
    //根据用户id和工作日期获取用户、排班和班次信息
    getScheduleByUserIdAndDate(userId, startDate, endDate) {
        return request({
            url: `${api_name}/getScheduleByUserIdAndDate/${userId}/${startDate}/${endDate}`,
            method: 'get',
        })
    },
    //根据科室id和工作日期获取用户、排班和班次信息
    getScheduleByDepartmentIdAndDate(departmentId, startDate, endDate) {
        return request({
            url: `${api_name}/getScheduleByDepartmentIdAndDate/${departmentId}/${startDate}/${endDate}`,
            method: 'get',
        })
    },
    //添加排班
    saveSchedule(form) {
        return request({
            url: `${api_name}/saveSchedule`,
            method: 'post',
            data: form
        })
    },
    //修改排班
    editSchedule(userId, scheduleDate, form) {
        return request({
            url: `${api_name}/updateSchedule/${userId}/${scheduleDate}`,
            method: 'put',
            data: form,
        })
    },
    //删除排班
    removeSchedule(form) {
        return request({
            url: `${api_name}/removeSchedule`,
            method: 'delete',
            data: form
        })
    },
    //批量删除排班
    batchDeleteSchedule(form) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: form
        })
    },
}
