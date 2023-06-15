import request from '@/utils/request'

const api_name = 'leave'

export default {
    //查找合适的顶班人员
    getMatchStaff(userId, scheduleDate) {
        return request({
            url: `${api_name}/getMatchStaff/${userId}/${scheduleDate}`,
            method: 'post',
        })
    },

    //顶班
    replaceSchedule(id, userId, replaceUserId, scheduleDate) {
        return request({
            url: `${api_name}/replaceSchedule/${id}/${userId}/${replaceUserId}/${scheduleDate}`,
            method: 'post',
        })
    },
    //条件查询带分页获得请假信息
    findPageLeave(current, limit, level, hospitalId, departmentId, searchObj) {
        return request({
            url: `${api_name}/findPageLeave/${current}/${limit}`,
            method: 'post',
            data: searchObj,
            params: {
                level: level,
                hospitalId: hospitalId,
                departmentId: departmentId,
            }
        })
    },
    //根据userId获取Leave
    getLeaveByUserId(userId) {
        return request({
            url: `${api_name}/getLeaveByUserId/${userId}`,
            method: 'get',
        })
    },
    //添加请假
    saveLeave(form) {
        return request({
            url: `${api_name}/saveLeave`,
            method: 'post',
            data: form
        })
    },
    //修改请假
    editLeave(form) {
        return request({
            url: `${api_name}/updateLeave`,
            method: 'post',
            data: form
        })
    },
    //删除请假
    deleteLeaveById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete',
        })
    },
    //批量删除请假
    batchDeleteLeave(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
}
