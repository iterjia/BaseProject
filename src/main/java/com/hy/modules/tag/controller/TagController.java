package com.hy.modules.tag.controller;


import com.hy.common.tool.Func;
import com.hy.common.tool.R;
import com.hy.common.base.entity.QueryParam;
import com.hy.modules.tag.entity.Tag;
import com.hy.modules.tag.service.ITagService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tag")
@AllArgsConstructor
public class TagController {

	private ITagService tagService;

	/**
	 * 查询列表
	 */
	@PostMapping("/list")
	public R<List<Tag>> list(@RequestBody(required = false) QueryParam param) {
		List<Tag> list = tagService.selectList(param);
		return R.data(list);
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
		return R.status(Func.retBool(tagService.insert(tag)));
	}

	/**
	 * 修改
	 */
	@PutMapping("")
	public R update(@RequestBody Tag tag) {
		if (tag.getId() > 0) {
			return R.status(Func.retBool(tagService.updateById(tag)));
		} else {
			return R.fail("提交数据中没有id");
		}
	}

	/**
	 * 删除
	 */
	@DeleteMapping("/{id}")
	public R remove(@PathVariable String id) {
		return R.status(Func.retBool(tagService.deleteById(id)));
	}

	/**
	 * 批量删除
	 */
	@DeleteMapping("/ids")
	public R removeByIds(String ids) {
		return R.status(Func.retBool(tagService.deleteByIds(ids)));
	}

}
