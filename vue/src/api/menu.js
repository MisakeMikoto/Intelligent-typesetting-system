import request from '@/utils/request'

const api_name = 'menu'

export default {
    //查询所有
    findAllMenu(searchObj){
        return request({
            url: `${api_name}/findAllMenu`,
            method: 'post',
            data: searchObj
        })
    },
    //查询所有id
    findAllMenuIds() {
        return request({
            url: `${api_name}/findAllMenuIds`,
            method: 'get',
        })
    },
    //获得icons
    getIcons(){
        return request({
            url: `${api_name}/icons`,
            method: 'get',
        })
    },
    //添加菜单
    saveMenu(form) {
        return request({
            url: `${api_name}/saveMenu`,
            method: 'post',
            data: form
        })
    },
    //修改菜单
    editMenu(form) {
        return request({
            url: `${api_name}/updateMenu`,
            method: 'post',
            data: form
        })
    },
    //删除菜单
    deleteMenuById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete',
        })
    },
    //批量删除菜单
    batchDeleteMenu(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
}
