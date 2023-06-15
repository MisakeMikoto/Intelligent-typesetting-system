package com.hyc.nsms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyc.nsms.mapper.RoleMenuMapper;
import com.hyc.nsms.model.entity.Menu;
import com.hyc.nsms.model.entity.Role;
import com.hyc.nsms.mapper.RoleMapper;
import com.hyc.nsms.model.entity.RoleMenu;
import com.hyc.nsms.service.MenuService;
import com.hyc.nsms.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;

    //设置角色与菜单之间关系
    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(queryWrapper);
        List<Integer> list = new ArrayList<>();
        for (RoleMenu roleMenu : roleMenus) {
            list.add(roleMenu.getMenuId());
        }
        return list;
    }

    //查询权限等级大于level的角色（level越小权力越大）
    @Override
    public List<Role> getRoleByGtLevel(Integer level) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("level", level);
        List<Role> list = roleMapper.selectList(queryWrapper);
        return list;
    }

    //查询权限等级大于等于level的角色（level越小权力越大）
    @Override
    public List<Role> getRoleByGeLevel(Integer level) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("level", level);
        List<Role> list = roleMapper.selectList(queryWrapper);
        return list;
    }

    //通过角色id与对应菜单数组
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        //先删除当前角色id所有绑定关系
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        roleMenuMapper.delete(queryWrapper);

        List<Integer> menuIdsCopy = new ArrayList<>();
        menuIdsCopy.addAll(menuIds);
        //再把前端传过来的菜单id数组绑定要当前的这个角色id上
        for (Integer menuId : menuIds) {
            //通过menuId查询menu数据
            Menu menu = menuService.getById(menuId);
            //如果找到menu的父级菜单，表示该菜单为二级菜单，并且传过来的menuId数组里面没有他的父级id
            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())) {
                //那么将这个父级id补上
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setMenuId(menu.getPid());
                roleMenu.setRoleId(roleId);
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenuMapper.insert(roleMenu);
        }
    }

}

