package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Department;
import com.hyc.nsms.model.entity.Dict;
import com.hyc.nsms.model.entity.User;
import com.hyc.nsms.model.result.Result;

import java.util.List;

import com.hyc.nsms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.hyc.nsms.model.entity.Role;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    //条件查询带分页
    @PostMapping("findPageRole/{current}/{limit}")
    public Result findPageRole(@PathVariable Integer current, @PathVariable Integer limit, @RequestBody(required = false) Role role) {
        //创建page对象，传递当前页，每页记录数
        Page<Role> page = new Page<>(current, limit);
        //构建条件
        QueryWrapper<Role> wrapper = new QueryWrapper<>();

        String roleName = role.getRoleName();//Role名称

        if (!StringUtils.isEmpty(roleName)) {
            wrapper.eq("role_name", roleName);
        }

        //调用方法实现分页查询
        Page<Role> RolePage = roleService.page(page, wrapper);
        //返回结果
        return Result.ok(RolePage);
    }

    //根据id获取Role
    @GetMapping("getRoleById/{id}")
    public Result getRoleById(@PathVariable Integer id) {
        Role role = roleService.getById(id);
        return Result.ok(role);
    }

    //查询权限等级大于level的角色（level越小权力越大）
    @GetMapping("getRoleByGtLevel/{level}")
    public Result getRoleByGtLevel(@PathVariable Integer level) {
        List<Role> list = roleService.getRoleByGtLevel(level);
        return Result.ok(list);
    }

    //查询权限等级大于等于level的角色（level越小权力越大）
    @GetMapping("getRoleByGeLevel/{level}")
    public Result getRoleByGeLevel(@PathVariable Integer level) {
        List<Role> list = roleService.getRoleByGeLevel(level);
        return Result.ok(list);
    }

    //添加Role
    @PostMapping("saveRole")
    public Result saveRole(@RequestBody Role role) {
        boolean save = roleService.save(role);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改Role信息
    @PostMapping("updateRole")
    public Result updateRole(@RequestBody Role role) {
        boolean flag = roleService.updateById(role);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除Role
    @DeleteMapping("{id}")
    public Result removeRole(@PathVariable Integer id) {
        boolean flag = roleService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除Role
    @DeleteMapping("batchRemove")
    public Result batchRemoveRole(@RequestBody List<Integer> idList) {
        roleService.removeByIds(idList);
        return Result.ok();
    }

    //设置角色与菜单关系
    @PostMapping("roleMenu/{roleId}")
    public Result setRoleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.ok();
    }

    //获取角色与菜单关系
    @GetMapping("roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId) {
        List<Integer> list = roleService.getRoleMenu(roleId);
        return Result.ok(list);
    }
}

