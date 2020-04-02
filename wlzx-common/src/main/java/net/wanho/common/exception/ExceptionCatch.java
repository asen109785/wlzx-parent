package net.wanho.common.exception;


import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import net.wanho.common.util.StringUtils;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.vo.response.CommonCode;
import net.wanho.common.vo.response.ResultStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice//控制器增强
@Slf4j
public class ExceptionCatch {

    protected static ImmutableMap.Builder<Class<? extends Throwable>, CommonCode> builder = ImmutableMap.builder();


    //捕获CustomException此类异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public AjaxResult exception(CustomException ex) {
        ExceptionResult result = ex.getExceptionResult();
        //记录日志
        log.error("catch exception:{}", ex.getMessage());
        return new AjaxResult(result.success(), result.code(), result.message());
    }

    //捕获Exception此类异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AjaxResult exception(Exception ex) {
        //记录日志
        log.error("catch exception:{}", ex.getMessage());
        CommonCode commonCode = builder.build().get(ex.getClass());
        if (StringUtils.isNotEmpty(commonCode)) {
            return new AjaxResult(commonCode);
        } else {
            return new AjaxResult(false, ResultStatus.EXCEPTION_FAIL, ex.getMessage());
        }
    }
}
