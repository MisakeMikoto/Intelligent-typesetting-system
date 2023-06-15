package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Dict;
import com.hyc.nsms.model.entity.Role;
import com.hyc.nsms.model.result.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.hyc.nsms.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.hyc.nsms.service.MenuService;
import com.hyc.nsms.model.entity.Menu;


import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private DictService dictService;

    //查询Menu表所有信息(带有条件查询)
    @PostMapping("findAllMenu")
    public Result findAllMenu(@RequestBody(required = false) Menu searchMenu) {
        //获得Menu名称
        String menuName = searchMenu.getMenuName();
        //通过Menu名称查询Menu表所有信息
        List<Menu> allMenu = menuService.findAllMenu(menuName);
        return Result.ok(allMenu);
    }

    //获取所有menuId
    @GetMapping("findAllMenuIds")
    public Result findAllMenuIds() {
        return Result.ok(menuService.list().stream().map(Menu::getId));
    }

    //条件查询带分页
    @PostMapping("findPageMenu/{current}/{limit}")
    public Result findPageMenu(@PathVariable Integer current, @PathVariable Integer limit, @RequestBody(required = false) Menu menu) {
        //创建page对象，传递当前页，每页记录数
        Page<Menu> page = new Page<>(current, limit);
        //构建条件
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();

        String menuName = menu.getMenuName();//Menu名称

        if (!StringUtils.isEmpty(menuName)) {
            wrapper.like("menu_name", menuName);
        }

        //调用方法实现分页查询
        Page<Menu> MenuPage = menuService.page(page, wrapper);
        //返回结果
        return Result.ok(MenuPage);
    }

    //添加Menu
    @PostMapping("saveMenu")
    public Result saveMenu(@RequestBody Menu menu) {
        boolean save = menuService.save(menu);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改Menu信息
    @PostMapping("updateMenu")
    public Result updateMenu(@RequestBody Menu menu) {
        boolean flag = menuService.updateById(menu);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除Menu
    @DeleteMapping("{id}")
    public Result removeMenu(@PathVariable Integer id) {
        boolean flag = menuService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除Menu
    @DeleteMapping("batchRemove")
    public Result batchRemoveMenu(@RequestBody List<Integer> idList) {
        menuService.removeByIds(idList);
        return Result.ok();
    }

    //获得icons
    @GetMapping("icons")
    public Result getIcons() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", "icon");
        List<Dict> list = dictService.list(null);
        return Result.ok(list);
    }
}

