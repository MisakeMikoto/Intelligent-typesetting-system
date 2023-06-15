package com.hyc.nsms.service;

import com.hyc.nsms.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface RoleService extends IService<Role> {
    //设置角色与菜单之间关系
    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    //通过角色id与对应菜单数组
    List<Integer> getRoleMenu(Integer roleId);

    //查询权限等级大于level的角色（level越小权力越大）
    List<Role> getRoleByGtLevel(Integer level);

    //查询权限等级大于等于level的角色（level越小权力越大）
    List<Role> getRoleByGeLevel(Integer level);
}
