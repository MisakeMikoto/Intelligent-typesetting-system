import request from '@/utils/request'

const api_name = 'files'

export default {
    //上传
    uploadFiles(form) {
        return request({
            url: `${api_name}/upload`,
            method: 'post',
            data: form
        })
    },
    //条件查询带分页获得文件信息
    findPageFiles(current, limit, searchObj) {
        return request({
            url: `${api_name}/findPageFiles/${current}/${limit}`,
            method: 'post',
            data: searchObj
        })
    },
    //修改文件
    updateFiles(form) {
        return request({
            url: `${api_name}/updateFiles`,
            method: 'post',
            data: form
        })
    },
    //删除文件
    deleteFilesById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete',
        })
    },
    //批量删除文件
    batchDeleteFiles(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
}
