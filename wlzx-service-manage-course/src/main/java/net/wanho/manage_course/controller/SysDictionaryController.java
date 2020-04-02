package net.wanho.manage_course.controller;

import net.wanho.api.sys.SysDictionaryControllerApi;
import net.wanho.common.web.BaseController;
import net.wanho.manage_course.service.SysDictionaryService;
import net.wano.po.system.SysDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class SysDictionaryController extends BaseController implements SysDictionaryControllerApi {

    @Autowired
    private SysDictionaryService sysDictionaryService;


    @GetMapping("/dictionary/get/{type}")
    @Override
    public SysDictionary getByType(@PathVariable String type) {
        return sysDictionaryService.getByType(type);
    }
}
