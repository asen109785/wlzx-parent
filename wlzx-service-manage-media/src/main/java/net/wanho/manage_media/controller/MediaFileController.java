package net.wanho.manage_media.controller;

import net.wanho.api.media.MediaFileControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wanho.manage_media.service.MediaFileService;
import net.wano.po.media.request.QueryMediaFileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media/file")
public class MediaFileController extends BaseController implements MediaFileControllerApi {

    @Autowired
    private MediaFileService mediaFileService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public AjaxResult findList(@PathVariable int page,@PathVariable int size, QueryMediaFileRequest queryMediaFileRequest) {
        return success("查询成功",mediaFileService.findList(page,size,queryMediaFileRequest));
    }
}
