package com.hyc.nsms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.nsms.exception.MyException;
import com.hyc.nsms.model.vo.pageVo.InformationPageVo;
import com.hyc.nsms.model.vo.pageVo.UserPageVo;
import com.hyc.nsms.model.entity.Menu;
import com.hyc.nsms.model.entity.Role;
import com.hyc.nsms.model.entity.User;
import com.hyc.nsms.mapper.UserMapper;
import com.hyc.nsms.model.result.ResultCodeEnum;
import com.hyc.nsms.model.vo.UserAccountVo;
import com.hyc.nsms.model.vo.queryVo.UserQueryVo;
import com.hyc.nsms.model.vo.UserStorageVo;
import com.hyc.nsms.service.MenuService;
import com.hyc.nsms.service.RoleService;
import com.hyc.nsms.service.UserService;
import com.hyc.nsms.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    //登录
    @Override
    public UserStorageVo login(UserAccountVo userAccountVo) {
        //查询是该用户是否存在
        User one = getUserInfo(userAccountVo);
        if (one != null) {
            UserStorageVo userStorageVo = new UserStorageVo();
            //将one复制到userStorageVo
            BeanUtils.copyProperties(one, userStorageVo);
            //通过用户id和用户名获得token
            String token = TokenUtils.getToken(one.getId().toString(), one.getUsername());
            //1.设置token
            userStorageVo.setToken(token);

            //获得用户的角色id
            Integer roleId = one.getRoleId();
            //通过roleId获得权限信息
            Role role = roleService.getById(roleId);
            //2.设置权限等级
            userStorageVo.setLevel(role.getLevel());

            //通过角色名称获得用户的菜单列表
            List<Menu> roleMenus = getRoleMenus(roleId);
            //3.设置用户的菜单列表
            userStorageVo.setMenus(roleMenus);
            return userStorageVo;
        } else {
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }
    }

    //注册
    @Override
    public User register(UserAccountVo userAccountVo) {
        //查询是该用户是否存在
        User one = getUserInfo(userAccountVo);
        if (one == null) {
            one = new User();
            //userAccountVo复制给one
            BeanUtils.copyProperties(userAccountVo, one);
            save(one);
        } else {
            throw new MyException(ResultCodeEnum.USER_ALREADY_EXISTS);
        }
        return one;
    }

    //修改密码
    @Override
    public boolean updatePassword(UserAccountVo userAccountVo) {
        int update = userMapper.updatePassword(userAccountVo);
        if (update < 1) {
            throw new MyException(ResultCodeEnum.PASSWORD_ERROR);
        }
        return true;
    }

    //通过传递当前页、每页记录数、用户权限等级、医院id、科室id、查询条件分页查询用户信息
    @Override
    public Page<User> findPageUser(Page<User> page, Integer level, Integer hospitalId, Integer departmentId, UserQueryVo userQueryVo) {
        String departmentName = userQueryVo.getDepartmentName();
        String name = userQueryVo.getName();
        //创建page对象，传递当前页，每页记录数
        Page<User> pageUser = userMapper.findPageUser(page, level, hospitalId, departmentId, departmentName, name);
        return pageUser;
    }

    //根据id获取角色详细信息
    @Override
    public InformationPageVo getInformationById(Integer id) {
        InformationPageVo informationPageVo = userMapper.getInformationById(id);
        return informationPageVo;
    }

    //根据hospitalId或者departmentId获取角色信息
    @Override
    public List<UserPageVo> getUserByHospitalIdOrDepartmentId(Integer level, Integer hospitalId, Integer departmentId) {
        List<UserPageVo> list = userMapper.getUserByHospitalIdOrDepartmentId(level, hospitalId, departmentId);
        return list;
    }

    //查询是该用户是否存在
    private User getUserInfo(UserAccountVo userAccountVo) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userAccountVo.getUsername());
        queryWrapper.eq("password", userAccountVo.getPassword());
        User one;
        try {
            one = getOne(queryWrapper);//从数据库查询信息
            return one;
        } catch (Exception e) {
            throw new MyException(ResultCodeEnum.SERVICE_ERROR);
        }
    }

    //通过角色id获得用户的菜单列表
    private List<Menu> getRoleMenus(Integer roleId) {
        //1 通过角色id查询所有关联的菜单
        List<Integer> menuIds = roleService.getRoleMenu(roleId);
        //2 查询系统所有菜单
        List<Menu> allMenu = menuService.findAllMenu("");
        //3 new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        //4 筛选当前用户角色的菜单
        for (Menu menu : allMenu) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            //获取子菜单集合children
            List<Menu> children = menu.getChildren();
            //移除子菜单集合children里面不在menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}

