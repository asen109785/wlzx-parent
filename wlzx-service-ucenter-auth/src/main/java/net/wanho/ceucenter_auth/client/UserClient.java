package net.wanho.ceucenter_auth.client;

import net.wanho.common.client.WlzxServiceList;
import net.wano.po.ucenter.ext.WlzxUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = WlzxServiceList.WLZX_SERVICE_UCENTER)
public interface UserClient {

    //根据账号查询用户信息
    @GetMapping("/ucenter/getuserext")
    public WlzxUserExt getUserext(@RequestParam("username") String username);
}
