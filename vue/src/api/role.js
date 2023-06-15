import request from '@/utils/request'

const api_name = 'role'

export default {
    //条件查询带分页获得角色信息
    findPageRole(current, limit, searchObj) {
        return request({
            url: `${api_name}/findPageRole/${current}/${limit}`,
            method: 'post',
            data: searchObj
        })
    },
    //查询权限等级大于level的角色（level越小权力越大）
    getRoleByGtLevel(level) {
        return request({
            url: `${api_name}/getRoleByGtLevel/${level}`,
            method: 'get',
        })
    },
    //通过角色id获得相应的菜单id数组
    getRoleMenu(roleId) {
        return request({
            url: `${api_name}/roleMenu/${roleId}`,
            method: 'get',
        })
    },
    //添加角色
    saveRole(form) {
        return request({
            url: `${api_name}/saveRole`,
            method: 'post',
            data: form
        })
    },
    //修改角色
    editRole(form) {
        return request({
            url: `${api_name}/updateRole`,
            method: 'post',
            data: form
        })
    },
    //绑定角色和菜单的关系
    setRoleMenu(roleId, menuIds) {
        return request({
            url: `${api_name}/roleMenu/${roleId}`,
            method: 'post',
            data: menuIds
        })
    },
    //删除角色
    deleteRoleById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete',
        })
    },
    //批量删除角色
    batchDeleteRole(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
}
