package net.wanho.manage_cms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanho.api.cms.CmsPageControllerApi;
import net.wanho.common.exception.CustomException;
import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.exception.ExceptionCatch;
import net.wanho.common.exception.ExceptionResult;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.vo.response.CommonCode;
import net.wanho.common.web.BaseController;
import net.wanho.manage_cms.service.CmsPageService;
import net.wano.po.cms.CmsPage;
import net.wano.po.cms.request.QueryPageRequest;
import net.wano.po.cms.response.CmsCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static net.wanho.common.vo.response.CommonCode.INVALID_PARAM;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController extends BaseController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService cmsPageService;

    @GetMapping("/list/{page}/{size}")
    public AjaxResult findList(@PathVariable int page, @PathVariable int size, QueryPageRequest queryPageRequest) {
        return success(cmsPageService.findList(page, size, queryPageRequest));
    }


    /**
     * 增加 返回增加的主键
     * @param cmsPage
     * @return
     */
    @Override
    @PostMapping("/add")
    public AjaxResult add(@RequestBody CmsPage cmsPage) {
        CmsPage one = cmsPageService.add(cmsPage);
        return success("新增成功",one.getPageId());
    }

    /**
     * 修改回显
     * @param id
     * @return
     */
    @Override
    @GetMapping("/get/{id}")
    public AjaxResult findById(@PathVariable String id) {
        CmsPage cmsPage = cmsPageService.getById(id);
        return success(cmsPage);
    }

    /**
     * 修改
     * @param cmsPage
     * @return
     */
    @Override
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody CmsPage cmsPage) {
        cmsPageService.updateById(cmsPage);
//        ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
//
//        throw  new CustomException(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        return success();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    @DeleteMapping("/del/{id}")
    public AjaxResult delete(@PathVariable String id) {
        cmsPageService.removeById(id);
        return success();
    }

    /**
     * 发布
     * @param pageId
     * @return
     */
    @Override
    @PostMapping("/postPage/{pageId}")
    public AjaxResult post(@PathVariable String pageId) {
        cmsPageService.post(pageId);
        return success();
    }

    /**
     * 课程的发布
     * @param cmsPage
     * @return
     */
    @Override
    @PostMapping("/postPageQuick")
    public AjaxResult postPageQuick(@RequestBody CmsPage cmsPage) {
        String pageUrl = cmsPageService.postPageQuick(cmsPage);
        return  success("",pageUrl);
    }
}
