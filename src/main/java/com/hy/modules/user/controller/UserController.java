package com.hy.modules.user.controller;

import com.hy.common.base.entity.PageData;
import com.hy.common.tool.Utils;
import com.hy.common.tool.R;
import com.hy.common.base.entity.QueryParam;
import com.hy.modules.user.entity.User;
import com.hy.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询列表
     */
    @PostMapping("/list")
    public R<List<User>> list(@RequestBody(required = false) QueryParam param) {
        List<User> list = userService.selectList(param);
        return R.data(list);
    }

    /**
     * 查询单页数据
     */
    @PostMapping("/page")
    public R<PageData> page(@RequestBody(required = false) QueryParam param) {
        List<User> list = userService.selectList(param);
        int total = userService.selectCount(param);
        return R.data(new PageData(total, list));
    }

    /**
     * 查询单条
     */
    @GetMapping("/{id}")
    public R<User> detail(@PathVariable String id) {
        User detail = userService.selectById(id);
        return R.data(detail);
    }

    /**
     * 添加
     */
    @PostMapping("register")
    public R insert(@RequestBody User user) {
        String password = Utils.saltPassword(user.getPassword());
        user.setPassword(password);
        return R.status(Utils.retBool(userService.insert(user)));
    }

    /**
     * 修改
     */
    @PutMapping("")
    public R update(@RequestBody User user) {
        if (user.getId() > 0) {
            return R.status(Utils.retBool(userService.updateById(user)));
        } else {
            return R.missParam("userId");
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/ids")
    public R delete(@RequestParam String ids) {
        return R.status(Utils.retBool(userService.delete(ids)));
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/logic/ids")
    public R logicDelete(@RequestParam String ids) {
        return R.status(Utils.retBool(userService.logicDelete(ids)));
    }

}
