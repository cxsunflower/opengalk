import axios, {AxiosResponse} from 'axios';
import router from "../router";
import {getToken} from "./TokenUtil";

export const baseURL = "http://localhost:8081";

const request = axios.create({
    baseURL: baseURL,
    timeout: 50000,
    responseType: 'json',
});

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(
    (requestConfig: any) => {
        if (requestConfig.headers) {
            //设置请求头
            requestConfig.headers['Content-Type'] = 'application/json;charset=utf-8';

            const setToken = (requestConfig.headers || {}).isToken === false;
            if (getToken() && !setToken) {
                requestConfig.headers.token = getToken();
            }

            if (requestConfig.headers.token == null) {
                router.push("/login").then();
            }
        }
        return requestConfig;
    },

    (error: any) => {
        console.log('错误：' + error);
        return Promise.reject(error);
    }
);

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    (response: AxiosResponse) => {
        return response;
    },
    (error: any) => {
        console.log('错误：' + error);
        return Promise.reject(error);
    }
);

export default request;
