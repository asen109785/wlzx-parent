package net.wanho.service_ucenter.controller;

import net.wanho.api.ucenter.UcenterControllerApi;
import net.wanho.common.web.BaseController;
import net.wanho.service_ucenter.service.UserService;
import net.wano.po.ucenter.ext.WlzxUserExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ucenter")
public class UcenterController extends BaseController implements UcenterControllerApi {

    @Autowired
    private UserService userService;

    @Override
    @GetMapping("/getuserext")
    public WlzxUserExt getUserext(String username) {
        return userService.getUserext(username);
    }
}
