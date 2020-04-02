package net.wanho.common.exception;

public class ExceptionCast {
    public static void cast(ExceptionResult exceptionResult){
        throw new CustomException(exceptionResult);
    }
}