package com.hyc.nsms.service;

import com.hyc.nsms.model.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {

    //通过父级菜单名称获得所有子级菜单
    List<Menu> findAllMenu(String menuName);
}
