package com.hy.modules.tag.controller;

import com.hy.common.base.entity.PageData;
import com.hy.common.tool.Utils;
import com.hy.common.tool.R;
import com.hy.common.base.entity.QueryParam;
import com.hy.modules.tag.entity.Tag;
import com.hy.modules.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 查询列表
     */
    @PostMapping("/list")
    public R<List<Tag>> list(@RequestBody(required = false) QueryParam param) {
        List<Tag> list = tagService.selectList(param);
        return R.data(list);
    }

    /**
     * 查询单页数据
     */
    @PostMapping("/page")
    public R<PageData> page(@RequestBody(required = false) QueryParam param) {
        List<Tag> list = tagService.selectList(param);
        int total = tagService.selectCount(param);
        return R.data(new PageData(total, list));
    }

    /**
     * 查询单条
     */
    @GetMapping("/{id}")
    public R<Tag> detail(@PathVariable String id) {
        Tag detail = tagService.selectById(id);
        return R.data(detail);
    }

    /**
     * 添加
     */
    @PostMapping("")
    public R insert(@RequestBody Tag tag) {
        return R.status(Utils.retBool(tagService.insert(tag)));
    }

    /**
     * 修改
     */
    @PutMapping("")
    public R update(@RequestBody Tag tag) {
        if (tag.getId() > 0) {
            return R.status(Utils.retBool(tagService.updateById(tag)));
        } else {
            return R.missParam("userId");
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/ids")
    public R delete(@RequestParam String ids) {
        return R.status(Utils.retBool(tagService.delete(ids)));
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/logic/ids")
    public R logicDelete(@RequestParam String ids) {
        return R.status(Utils.retBool(tagService.logicDelete(ids)));
    }

}
