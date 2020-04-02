package net.wanho.common.vo.response;

/**
 * 200-- 通用成功代码
 * 500-- 通用错误代码
 * 20000-- 抛出异常的代码
 * 21000-- 媒资错误代码
 * 22000-- 用户中心错误代码
 * 23000-- cms错误代码
 * 24000-- 文件系统

 */
public interface ResultStatus {
   int SUCCESS=200;

   int FAIL=500;

   int EXCEPTION_FAIL=20000;
   int MDEIA_FAIL=21000;
   int USERCENTER_FAIL=22000;
   int CMS_FAIL=23000;
   int FILESYSTEM_FAIL=24000;



}
