# NSMS

## 平台介绍

**一、项目背景**

- 传统的护士排班中，护士长根据以往经验以及个人判断的手动排班。
- 这样的排班方式基于人工排班或者按照固有模式安排护士值班 ，不能根据护士的实际情况进行调整。
- 本系统采用自动排班的方式，方便护士长对护士和助手的管理、以及简化排班过程。

**二、前提条件**

- 本项目最核心功能味排班管理的**自动排班**功能，能根据规则快速为员工进行排班。
- 排班规则如下：
  - 员工一周最多上6天班；
  - 员工一周最多上3天大夜班；
  - 上完大夜班之后必须休息1天后，才能继续上班；

**三、主要技术**

- 前端采用`Vue`、`Element-UI`、`ECharts`。
- 后端采用`SpringBoot`、MyBatis-Plus、`JWT`。

**四、功能模块概述**

1. 护士长：登录（token验证）、注册、修改密码、用户管理、角色管理、期望管理、请假管理、换班管理、工作量管理、排班管理（手动排班、自动排班、排班表打印）、班次管理、查看个人信息、查看工作量；
2. 总护士长：包含护士长所有功能、科室管理；
3. 系统管理员：包含总护士长所有功能、文件管理、权限管理、菜单分配。


## 安装教程

1. `git clone https://gitee.com/he-yicheng/nsms.git`
2. 导入数据库`nsdb.sql`文件 
3. 打开项目文件后，在`Terminal`下输入

```bash
cd vue
npm install
npm run serve # 启动后台
cd ..
cd vue-front 
npm install
npm run serve # 启动前台
```

3. 再启动后端SpringBoot项目即可进入页面
4. 默认输入http://localhost:8080/background/login进入后台
5. 默认输入http://localhost:8081/front/login进入前台

## 在线体验

- 前台：http://cau1i.cn/front/login

- 后台：http://cau1i.cn/background/login

|   角色   | 账号 | 密码 |
| :------: | :--: | :--: |
|  管理员  |  1   |  1   |
| 总护士长 | lux  | lux  |
|  护士长  | ashe | ashe |

## 演示图

![登录](https://raw.githubusercontent.com/HeYiCheng1X/PicGo/main/images%E7%99%BB%E5%BD%95.png)

![注册](https://raw.githubusercontent.com/HeYiCheng1X/PicGo/main/images%E6%B3%A8%E5%86%8C.png)

![管理员主页](https://raw.githubusercontent.com/HeYiCheng1X/PicGo/main/images%E7%AE%A1%E7%90%86%E5%91%98%E4%B8%BB%E9%A1%B5.png)

![](https://raw.githubusercontent.com/HeYiCheng1X/PicGo/main/images%E7%AE%A1%E7%90%86%E5%91%98%E4%B8%BB%E9%A1%B5.png)

![前台主页](https://raw.githubusercontent.com/HeYiCheng1X/PicGo/main/images%E5%89%8D%E5%8F%B0%E4%B8%BB%E9%A1%B5.png)

![个人中心](https://raw.githubusercontent.com/HeYiCheng1X/PicGo/main/images%E4%B8%AA%E4%BA%BA%E4%B8%AD%E5%BF%83.png)

![用户管理](https://raw.githubusercontent.com/HeYiCheng1X/PicGo/main/images%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86.png)

![菜单管理](https://raw.githubusercontent.com/HeYiCheng1X/PicGo/main/images%E8%8F%9C%E5%8D%95%E7%AE%A1%E7%90%86.png)

![](https://raw.githubusercontent.com/HeYiCheng1X/PicGo/main/images%E8%8F%9C%E5%8D%95%E7%AE%A1%E7%90%86.png)

![排班管理](https://raw.githubusercontent.com/HeYiCheng1X/PicGo/main/images%E6%8E%92%E7%8F%AD%E7%AE%A1%E7%90%86.png)

## 注意

- **本项目有许多已知或未知BUG！本项目有许多已知或未知BUG！本项目有许多已知或未知BUG！**
- 技术很差，经供参考。

