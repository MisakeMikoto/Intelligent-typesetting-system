package com.hyc.nsms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyc.nsms.model.entity.Menu;
import com.hyc.nsms.mapper.MenuMapper;
import com.hyc.nsms.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    //通过父级菜单名称获得所有子级菜单
    @Override
    public List<Menu> findAllMenu(String menuName) {
        //构建条件
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        //判断是否为空
        if (!StringUtils.isEmpty(menuName)) {
            wrapper.like("menu_name" , menuName);
        }
        //查询所有数据
        List<Menu> list = list(wrapper);
        //找出pid为null的一级菜单
        List<Menu> parentMenus = list.stream()
                .filter(menu -> menu.getPid() == null)
                .collect(Collectors.toList());
        //找出一级菜单的子菜单
        for (Menu menu : parentMenus) {
            //筛选所有数据中pid=父级id的数据就是二级菜单
            List<Menu> childrenMenu = list.stream()
                    .filter(m -> menu.getId().equals(m.getPid()))
                    .collect(Collectors.toList());
            menu.setChildren(childrenMenu);
        }
        return parentMenus;
    }
}
