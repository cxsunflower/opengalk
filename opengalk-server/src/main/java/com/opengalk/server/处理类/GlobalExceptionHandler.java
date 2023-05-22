package com.opengalk.server.处理类;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.opengalk.server.响应类.ResponseResult;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //设置状态码为 400
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseResult<?> paramExceptionHandler(@NotNull ConstraintViolationException e) {
        log.error(ExceptionUtil.stacktraceToString(e));
        return new ResponseResult<>(0, "请求参数异常已记录", null);
    }
}