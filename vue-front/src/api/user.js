import request from '@/utils/request'

const api_name = 'user'

export default {
    //登录
    login(form) {
        return request({
            url: `${api_name}/login`,
            method: 'post',
            data: form
        })
    },
    //注册
    register(form) {
        return request({
            url: `${api_name}/register`,
            method: 'post',
            data: form
        })
    },
    //修改密码
    updatePassword(form) {
        return request({
            url: `${api_name}/updatePassword`,
            method: 'post',
            data: form
        })
    },
    //条件查询带分页获得用户信息
    findPageUser(current, limit, level, hospitalId, departmentId, searchObj) {
        return request({
            url: `${api_name}/findPageUser/${current}/${limit}`,
            method: 'post',
            data: searchObj,
            params: {
                level: level,
                hospitalId: hospitalId,
                departmentId: departmentId,
            }
        })
    },
    //根据id查询用户信息
    getUserById(id) {
        return request({
            url: `${api_name}/getUserById/${id}`,
            method: 'get'
        })
    },
    //根据id查询用户详细信息
    getInformationById(id) {
        return request({
            url: `${api_name}/getInformationById/${id}`,
            method: 'get'
        })
    },
    //添加用户
    saveUser(form) {
        return request({
            url: `${api_name}/saveUser`,
            method: 'post',
            data: form
        })
    },
    //修改用户
    editUser(form) {
        return request({
            url: `${api_name}/updateUser`,
            method: 'post',
            data: form
        })
    },
    //删除用户
    deleteUserById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete',
        })
    },
    //批量删除用户
    batchDeleteUser(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
}
