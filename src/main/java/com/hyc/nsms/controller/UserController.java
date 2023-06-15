package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.nsms.model.entity.Workload;
import com.hyc.nsms.model.vo.pageVo.InformationPageVo;
import com.hyc.nsms.model.vo.pageVo.UserPageVo;
import com.hyc.nsms.model.entity.User;
import com.hyc.nsms.model.result.ResultCodeEnum;
import com.hyc.nsms.model.vo.UserAccountVo;
import com.hyc.nsms.model.vo.queryVo.UserQueryVo;
import com.hyc.nsms.model.result.Result;
import com.hyc.nsms.model.vo.UserStorageVo;
import com.hyc.nsms.service.UserService;
import com.hyc.nsms.service.WorkloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private WorkloadService workloadService;

    //登录
    @PostMapping("login")
    public Result login(@RequestBody UserAccountVo userAccountVo) {
        String username = userAccountVo.getUsername();
        String password = userAccountVo.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Result.build(ResultCodeEnum.PARAM_ERROR.getCode(), ResultCodeEnum.PARAM_ERROR.getMessage());
//          return Result.fail()
//                    .code(ResultCodeEnum.PARAM_ERROR.getCode())
//                    .message(ResultCodeEnum.PARAM_ERROR.getMessage());
        }
        UserStorageVo login = userService.login(userAccountVo);//调用方法
        return Result.ok(login);
    }

    //注册
    @PostMapping("register")
    public Result register(@RequestBody UserAccountVo userAccountVo) {
        String username = userAccountVo.getUsername();
        String password = userAccountVo.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Result.build(ResultCodeEnum.PARAM_ERROR.getCode(), ResultCodeEnum.PARAM_ERROR.getMessage());
        }
        User register = userService.register(userAccountVo);//调用方法
        return Result.ok(register);
    }

    //修改密码
    @PostMapping("updatePassword")
    public Result updatePassword(@RequestBody UserAccountVo userAccountVo) {
        boolean flag = userService.updatePassword(userAccountVo);
        return Result.ok(flag);
    }

    //条件查询带分页
    @PostMapping("findPageUser/{current}/{limit}")
    public Result findPageUser(@PathVariable Integer current,
                               @PathVariable Integer limit,
                               @RequestParam Integer level,
                               @RequestParam(required = false) Integer hospitalId,
                               @RequestParam(required = false) Integer departmentId,
                               @RequestBody(required = false) UserQueryVo userQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<User> page = new Page<>(current, limit);

        //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询用户信息
        Page<User> userPage = userService.findPageUser(page, level, hospitalId, departmentId, userQueryVo);

//        //获取当前用户信息
//        User currentUser = TokenUtils.getCurrentUser();
//        System.out.println("===========================>当前用户信息:" + currentUser);

        //返回结果
        return Result.ok(userPage);
    }

    //根据id获取用户信息
    @GetMapping("getUserById/{id}")
    public Result getUserById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.ok(user);
    }

    //根据id获取用户详细信息
    @GetMapping("getInformationById/{id}")
    public Result getInformationById(@PathVariable Integer id) {
        InformationPageVo information = userService.getInformationById(id);
        return Result.ok(information);
    }

    //添加用户
    @PostMapping("saveUser")
    public Result saveUser(@RequestBody User user) {
        boolean save = userService.save(user);

        //增加相应用户的工作量数据
        Workload workload = new Workload();
        workload.setUserId(user.getId());
        //调用workloadService
        workloadService.save(workload);

        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改用户信息
    @PostMapping("updateUser")
    public Result updateUser(@RequestBody User user) {
        boolean flag = userService.updateById(user);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //删除用户
    @DeleteMapping("{id}")
    public Result removeUser(@PathVariable Integer id) {
        boolean flag = userService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除用户
    @DeleteMapping("batchRemove")
    public Result batchRemoveUser(@RequestBody List<Integer> idList) {
        userService.removeByIds(idList);
        return Result.ok();
    }
}
