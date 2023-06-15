import request from '@/utils/request'

const api_name = 'shift'

export default {
    //条件查询带分页获得班次信息
    findPageShift(current, limit, hospitalId, departmentId, searchObj) {
        return request({
            url: `${api_name}/findPageShift/${current}/${limit}`,
            method: 'post',
            data: searchObj,
            params: {
                hospitalId: hospitalId,
                departmentId: departmentId,
            }
        })
    },
    //根据departmentId获得科室
    getShiftByDepartmentId(departmentId){
        return request({
            url: `${api_name}/getShiftByDepartmentId`,
            method: 'get',
            params: {
                departmentId: departmentId,
            }
        })
    },
    //添加班次
    saveShift(form) {
        return request({
            url: `${api_name}/saveShift`,
            method: 'post',
            data: form
        })
    },
    //修改班次
    editShift(form) {
        return request({
            url: `${api_name}/updateShift`,
            method: 'post',
            data: form
        })
    },
    //删除班次
    deleteShiftById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete',
        })
    },
    //批量删除班次
    batchDeleteShift(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
}
