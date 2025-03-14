import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from "@/router/router.js";

const request = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000,
})


request.interceptors.request.use(
    config => {
        // 在请求发送之前做一些处理
        // 例如添加请求头、修改请求参数等
        config.headers['Content-Type'] = 'application/json;charset=UTF-8'
        let user = JSON.parse(localStorage.getItem('user_code')||'{}')
        config.headers['token'] = user.token
        return config
    },
    error => {
        // 请求错误时做些什么
        ElMessage.error('请求失败')
        return Promise.reject(error)
    }
)

request.interceptors.response.use(
    response => {
        let res = response.data;
        if(typeof res == 'string'){
            res=res?JSON.parse(res):res
        }
        if(res.code === '401'){
            ElMessage.error(res.msg)
            router.push('/login')
        }else {
            return res
        }

    },
    error => {
        if (error.response.status === 404){
            ElMessage.error('未找到请求接口')
        } else if (error.response.status === 500){
            ElMessage.error('系统异常/服务器错误')
        }else{
            console.error(error.message)
        }
        return Promise.reject(error)
    }


)

export default request