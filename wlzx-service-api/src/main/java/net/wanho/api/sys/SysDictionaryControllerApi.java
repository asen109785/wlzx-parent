package net.wanho.api.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.wano.po.system.SysDictionary;

@Api(value = "数据字典接口", tags = "提供数据字典接口的管理、查询功能")
public interface SysDictionaryControllerApi {
    //数据字典
    @ApiOperation(value = "数据字典查询接口")
    public SysDictionary getByType(String type);
}
