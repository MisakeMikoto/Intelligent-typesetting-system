import axios from 'axios'
import ElementUI from 'element-ui';
import {serverIp} from "../../public/config";
import store from "@/store";

const request = axios.create({
    baseURL: `http://${serverIp}/`,
    timeout: 5000
})

//request 拦截器
//可以自请求发送前对请求做一些处理
//比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null
    if (user) {
        config.headers['token'] = user.token;  // 设置请求头
    }

    return config
}, error => {
    return Promise.reject(error)
});

// 响应拦截器
let isToken = false
//response 拦截器
//可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        //如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        //兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        //当抛出异常的时候给出提示
        if (res.code > 200 && res.code < 300) {
            ElementUI.Message({
                message: res.message,
                type: 'error'
            });
        }
        //token失效时
        if (res.code > 300 && res.code < 400) {
            // 防止失效导致出现多个提示框的解决办法
            if (!isToken) {
                isToken = true
                //执行退出登录方法
                store.commit("logout")
                //给出提示
                ElementUI.Message({
                    message: res.message,
                    type: 'error'
                });
            }
        }
        return res;
    }, error => {
        console.log('err' + error)//for debug
        return Promise.reject(error)
    }
)


export default request
