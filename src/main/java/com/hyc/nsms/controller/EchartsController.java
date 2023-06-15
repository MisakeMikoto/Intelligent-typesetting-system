package com.hyc.nsms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyc.nsms.model.vo.pageVo.UserPageVo;
import com.hyc.nsms.model.entity.Department;
import com.hyc.nsms.model.entity.Hospital;
import com.hyc.nsms.model.entity.Role;
import com.hyc.nsms.model.entity.User;
import com.hyc.nsms.model.result.Result;
import com.hyc.nsms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private FilesService filesService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @GetMapping("getList/{level}")
    public Result getList(@PathVariable Integer level, @RequestParam(required = false) Integer hospitalId, @RequestParam(required = false) Integer departmentId) {
        Map<String, Integer> map = new HashMap<>();
        //step1：
        //1.1 当医院id为空，代表该用户职位是最高权限管理员，应该看到医院数量
        if (hospitalId == null) {
            //查询所有医院
            List<Hospital> list = hospitalService.list();
            for (Hospital hospital : list) {
                map.put(hospital.getProvince(), 0);//医院省份初始化为0
            }
        }
        //1.2 当科室id为空，代表该用户职位大于护士长，应该看到科室数量
        else if (departmentId == null) {
            //根据医院id获得所有科室
            List<Department> departmentList = departmentService.getDepartmentByHospitalId(hospitalId);
            for (Department department : departmentList) {
                map.put(department.getDepartmentName(), 0);//科室人数初始化为0
            }
        } else {//1.3 当科室id不为空，代表该用户职位小于等于护士长，应该看到护士和助手对应人数
            //根据level获取权限表中所有职位
            List<Role> roleList = roleService.getRoleByGeLevel(level);
            for (Role role : roleList) {
                map.put(role.getRoleName(), 0);//员工人数初始化为0
            }
        }

        //step2：根据id获得对应人数
        Map<String, Integer> count = this.getCount(map, level, hospitalId, departmentId);

        //step3：将所有职位和相应人数存入数组中
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<Integer> integers = new ArrayList<>();
        for (String key : count.keySet()) {
            strings.add(key);//存入所有名称
            integers.add(count.get(key));//存入对应人数
        }

        //step4：通过map传递数组
        Map<String, Object> coordinate = new HashMap<>();
        coordinate.put("x", strings);
        coordinate.put("y", integers);

        return Result.ok(coordinate);
    }

    @GetMapping("getNumber")
    public Result getNumber(@RequestParam(required = false) Integer hospitalId, @RequestParam(required = false) Integer departmentId) {
        ArrayList<Long> arrayList = new ArrayList<>();
        arrayList.add(hospitalService.count());
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (hospitalId != null) {
            departmentQueryWrapper.eq("hospital_id", hospitalId);
            userQueryWrapper.eq("hospital_id", hospitalId);
        }
        if (departmentId != null) {
            userQueryWrapper.eq("department_id", departmentId);
        }

        arrayList.add(departmentService.count(departmentQueryWrapper));
        arrayList.add(userService.count(userQueryWrapper));
        arrayList.add(roleService.count());
        arrayList.add(filesService.count());
        return Result.ok(arrayList);
    }

    private Map<String, Integer> getCount(Map<String, Integer> map, Integer level, Integer hospitalId, Integer departmentId) {
        if (hospitalId == null) {
            //获得所有医院的省份
            List<Hospital> list = hospitalService.list();
            for (String s : map.keySet()) {
                int count = 0;//记录该职位人数
                for (Hospital hospital : list) {
                    if (s.equals(hospital.getProvince())) {
                        map.put(s, ++count);
                    }
                }
            }
        } else {
            //根据id获得对应人数
            List<UserPageVo> userList = userService.getUserByHospitalIdOrDepartmentId(level, hospitalId, departmentId);
            for (String s : map.keySet()) {
                int count = 0;//记录该职位人数
                for (UserPageVo userPageVo : userList) {
                    if (departmentId == null && s.equals(userPageVo.getDepartmentName())) {
                        map.put(s, ++count);
                    } else if (s.equals(userPageVo.getRoleName())) {
                        map.put(s, ++count);
                    }
                }
            }
        }
        return map;
    }
}
