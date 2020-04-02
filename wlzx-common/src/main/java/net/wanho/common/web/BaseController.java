package net.wanho.common.web;

import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.vo.response.ResultStatus;


public class BaseController {
    public AjaxResult success(String message,Object data){
        return new AjaxResult(true, ResultStatus.SUCCESS,message,data);
    }

    public AjaxResult success(String message){
        return new AjaxResult(true, ResultStatus.SUCCESS,message,null);
    }

    public AjaxResult success(Object data){
        return new AjaxResult(true, ResultStatus.SUCCESS,"操作成功",data);
    }

    public AjaxResult success(){
        return new AjaxResult(true, ResultStatus.SUCCESS,"操作成功",null);
    }

    public AjaxResult fail(String message,Object data){
        return new AjaxResult(false, ResultStatus.FAIL,message,data);
    }

    public AjaxResult fail(String message,int status,Object data){
        return new AjaxResult(false,status,message,data);
    }

    public AjaxResult fail(String message){
        return new AjaxResult(false, ResultStatus.FAIL,message,null);
    }

    public AjaxResult fail(Object data){
        return new AjaxResult(false, ResultStatus.FAIL,"操作失败",data);
    }

    public AjaxResult fail(){
        return new AjaxResult(false, ResultStatus.FAIL,"操作失败",null);
    }
}
