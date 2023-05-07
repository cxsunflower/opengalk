package com.opengalk.server.响应类;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {

    /**
     *  响应状态（0错误，1正确，2验证码出错）
     */
    private Integer 响应状态;

    /**
     * 响应消息，用于前端弹出框的消息展示
     */
    private String 响应消息;

    /**
     * 返回给前端的数据
     */
    private T 响应数据;
}
