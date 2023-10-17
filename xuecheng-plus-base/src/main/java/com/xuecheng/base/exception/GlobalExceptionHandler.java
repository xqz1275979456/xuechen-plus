package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理程序
 *
 * @author xuqizheng
 * @date 2023/10/15
 */
@Slf4j
@ControllerAdvice
//@RestControllerAdvice
public class GlobalExceptionHandler {

    //对项目的自定义异常类型进行处理
    @ResponseBody
    @ExceptionHandler(com.xuecheng.base.exception.XueChengPlusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public com.xuecheng.base.exception.RestErrorResponse customException(com.xuecheng.base.exception.XueChengPlusException e){
        //记录异常
        log.error("系统异常{}",e.getErrMessage(),e);

        //解析出异常信息
        String errMessage = e.getErrMessage();
        com.xuecheng.base.exception.RestErrorResponse restErrorResponse = new com.xuecheng.base.exception.RestErrorResponse(errMessage);
        return restErrorResponse;
    }

    //对项目的自定义异常类型进行处理
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public com.xuecheng.base.exception.RestErrorResponse exception(Exception e){
        //记录异常
        log.error("系统异常{}",e.getMessage(),e);

        //解析出异常信息
        com.xuecheng.base.exception.RestErrorResponse restErrorResponse = new com.xuecheng.base.exception.RestErrorResponse(com.xuecheng.base.exception.CommonError.UNKOWN_ERROR.getErrMessage());
        return restErrorResponse;
    }

    //对MethodArgumentNotValidException异常处理
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public com.xuecheng.base.exception.RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> msgList = new ArrayList<>();
        //将错误信息放在msgList
        bindingResult.getFieldErrors().stream().forEach(item->msgList.add(item.getDefaultMessage()));
        //拼接错误信息
        String msg = StringUtils.join(msgList, ",");
        log.error("【系统异常】{}",msg);
        return new com.xuecheng.base.exception.RestErrorResponse(msg);
    }

}
