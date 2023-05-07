import {ElMessage} from "element-plus";

export const showMessage = (response: any) => {
    ElMessage({
        message: response.data.响应消息,
        grouping: true,
        type: response.data.响应状态 === 1 ? 'success' : 'error',
        center: true,
    })
}


