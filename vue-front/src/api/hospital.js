import request from '@/utils/request'

const api_name = 'hospital'

export default {
    //查询所有医院
    findAllHospital(){
        return request({
            url: `${api_name}/findAllHospital`,
            method: 'get',
        })
    },
    //根据id获取Hospital
    getHospitalById(id){
        return request({
            url: `${api_name}/getHospitalById/${id}`,
            method: 'get',
        })
    },
    //条件查询带分页获得医院信息
    findPageHospital(current, limit, searchObj) {
        return request({
            url: `${api_name}/findPageHospital/${current}/${limit}`,
            method: 'post',
            data: searchObj
        })
    },
    //添加医院
    saveHospital(form) {
        return request({
            url: `${api_name}/saveHospital`,
            method: 'post',
            data: form
        })
    },
    //修改医院
    editHospital(form) {
        return request({
            url: `${api_name}/updateHospital`,
            method: 'post',
            data: form
        })
    },
    //删除医院
    deleteHospitalById(id){
        return request({
            url: `${api_name}/${id}`,
            method: 'delete',
        })
    },
    //批量删除医院
    batchDeleteHospital(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
}
