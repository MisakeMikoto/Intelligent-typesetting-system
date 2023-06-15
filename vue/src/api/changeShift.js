import request from '@/utils/request'

const api_name = 'changeShift'

export default {
    //条件查询带分页获得换班信息
    findPageChangeShift(current, limit, level, hospitalId, departmentId, searchObj) {
        return request({
            url: `${api_name}/findPageChangeShift/${current}/${limit}`,
            method: 'post',
            data: searchObj,
            params: {
                level: level,
                hospitalId: hospitalId,
                departmentId: departmentId,
            }
        })
    },
    //根据userId获取ChangeShift
    getChangeShiftByUserId(userId) {
        return request({
            url: `${api_name}/getChangeShiftByUserId/${userId}`,
            method: 'get',
        })
    },
    //添加换班
    saveChangeShift(form) {
        return request({
            url: `${api_name}/saveChangeShift`,
            method: 'post',
            data: form
        })
    },
    //修改换班
    editChangeShift(form) {
        return request({
            url: `${api_name}/updateChangeShift`,
            method: 'post',
            data: form
        })
    },
    //删除换班
    deleteChangeShiftById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete',
        })
    },
    //批量删除换班
    batchDeleteChangeShift(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
}
