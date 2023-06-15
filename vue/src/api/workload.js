import request from '@/utils/request'

const api_name = 'workload'

export default {
    //条件查询带分页获得工作量信息
    findPageWorkload(current, limit, level, hospitalId, departmentId, searchObj) {
        return request({
            url: `${api_name}/findPageWorkload/${current}/${limit}`,
            method: 'post',
            data: searchObj,
            params: {
                level: level,
                hospitalId: hospitalId,
                departmentId: departmentId,
            }
        })
    },
    //根据userId获取Workload
    getWorkloadByUserId(userId) {
        return request({
            url: `${api_name}/getWorkloadByUserId/${userId}`,
            method: 'get',
        })
    },
    //添加工作量
    saveWorkload(form) {
        return request({
            url: `${api_name}/saveWorkload`,
            method: 'post',
            data: form
        })
    },
    //修改工作量
    editWorkload(form) {
        return request({
            url: `${api_name}/updateWorkload`,
            method: 'post',
            data: form
        })
    },
    //根据userId删除工作量
    deleteWorkloadByUserId(userId) {
        return request({
            url: `${api_name}/userId/${userId}`,
            method: 'delete',
        })
    },
    //根据userIds批量删除工作量
    batchDeleteWorkloadByUserId(idList) {
        return request({
            url: `${api_name}/userId/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
    //删除工作量
    deleteWorkloadById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete',
        })
    },
    //批量删除工作量
    batchDeleteWorkload(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
}
