import {baseURL} from "./RequestUtil";
import axios from "axios";

export const getVerificationCode = async (): Promise<string[]> => {
    const data: string[] = [];
    await axios.get(baseURL + "/verify/getVerificationCode").then(result => {
        data[0] = "data:image/png;base64," + result.data.响应数据[0];
        data[1] = result.data.响应数据[1];
    });
    return data;
};