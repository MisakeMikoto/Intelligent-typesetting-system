import request from '@/utils/request'

const api_name = 'department'

export default {
    //条件查询带分页获得科室信息
    findPageDepartment(current, limit, hospitalId, departmentId, searchObj) {
        return request({
            url: `${api_name}/findPageDepartment/${current}/${limit}`,
            method: 'post',
            data: searchObj,
            params: {
                hospitalId: hospitalId,
                departmentId: departmentId,
            }
        })
    },
    //根据id获取科室
    getDepartmentById(id) {
        return request({
            url: `${api_name}/getDepartmentById/${id}`,
            method: 'get'
        })
    },
    //根据hospitalId获取科室
    getDepartmentByHospitalId(hospitalId) {
        return request({
            url: `${api_name}/getDepartmentByHospitalId`,
            method: 'get',
            params: {
                hospitalId: hospitalId
            }
        })
    },
    //添加科室
    saveDepartment(form) {
        return request({
            url: `${api_name}/saveDepartment`,
            method: 'post',
            data: form
        })
    },
    //修改科室
    editDepartment(form) {
        return request({
            url: `${api_name}/updateDepartment`,
            method: 'post',
            data: form
        })
    },
    //删除科室
    deleteDepartmentById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete',
        })
    },
    //批量删除科室
    batchDeleteDepartment(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
}
