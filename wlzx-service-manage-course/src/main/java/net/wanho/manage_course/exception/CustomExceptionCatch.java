package net.wanho.manage_course.exception;

import lombok.extern.slf4j.Slf4j;
import net.wanho.common.exception.ExceptionCatch;
import net.wanho.common.vo.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
	课程管理自定义的异常类，其中定义异常类型所对应的错误代码
 */
@ControllerAdvice//控制器增强
@Slf4j
public class CustomExceptionCatch extends ExceptionCatch {
    static {
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}