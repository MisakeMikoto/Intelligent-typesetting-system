import request from '@/utils/request'

const api_name = 'expectation'

export default {
    //条件查询带分页获得期望信息
    findPageExpectation(current, limit, level, hospitalId, departmentId, searchObj) {
        return request({
            url: `${api_name}/findPageExpectation/${current}/${limit}`,
            method: 'post',
            data: searchObj,
            params: {
                level: level,
                hospitalId: hospitalId,
                departmentId: departmentId,
            }
        })
    },
    //根据userId获取Expectation
    getExpectationByUserId(userId) {
        return request({
            url: `${api_name}/getExpectationByUserId/${userId}`,
            method: 'get',
        })
    },
    //添加期望
    saveExpectation(form) {
        return request({
            url: `${api_name}/saveExpectation`,
            method: 'post',
            data: form
        })
    },
    //修改期望
    editExpectation(form) {
        return request({
            url: `${api_name}/updateExpectation`,
            method: 'post',
            data: form
        })
    },
    //删除期望
    deleteExpectationById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete',
        })
    },
    //批量删除期望
    batchDeleteExpectation(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
}
