<template>
  <div>
    <!--  头部-->
    <div style="height: 60px;display: flex;align-items: center;border-bottom: 1px solid #eee">
      <div style="display: flex;align-items: center;padding-left: 20px;">
        <img src="../assets/logoko.png" alt="logo" style="width: 100px;height: 50px">
        <div style="color: darkslategray; font-size: 20px;margin-left: 20px">后台管理系统</div>
      </div>
      <div style="display:flex; color: white;font-size: 20px;margin-left: auto;margin-right: 20px">
        <div style="margin-left: 10px;display: flex;align-items: center">
          <el-dropdown>
            <div style="display: flex;align-items: center">
              <el-icon class="el-icon--left">
                <Avatar />
              </el-icon>
              管理员
              <el-icon class="el-icon--right">
                <arrow-down />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人信息</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!--  头部结束-->
  </div>


  <div style="display: flex">
    <!--  左侧菜单-->
    <div style="width: 240px">
      <el-menu router :default-active="router.currentRoute.value.path" class="el-menu-vertical-demo"  style="min-height:calc(100vh - 60px)">

        <el-menu-item index="/manager/Welcome">
          <el-icon>
            <House />
          </el-icon>
          <span>首页</span>
        </el-menu-item>

        <el-sub-menu index="2">
          <template #title>
            <el-icon>
              <User />
            </el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
          <el-menu-item index="/manager/user">用户信息</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/manager/about">关于</el-menu-item>

      </el-menu>
    </div>
    <!--  左侧菜单结束-->

    <!--  中间数据渲染-->
    <div style="flex:1;margin:10px">
      <div class="card">
        <span @click="router.push('/manager/Welcome')" style="cursor: pointer">首页</span> / {{router.currentRoute.value.meta.name}}
      </div>
      <RouterView />
    </div>
    <!--  数据区域结束-->

    <!--底部区域-->
    <div>

    </div>
  </div>
  <!--  底部区域结束-->
</template>

<script setup>
import {ElMenu, ElMenuItem, ElMenuItemGroup, ElMessage} from "element-plus";
import {ArrowDown, Avatar, House, User} from "@element-plus/icons-vue";
import router from "@/router/router.js";

const logout = () => {
  localStorage.removeItem('user_code')
  ElMessage.info("退出登录成功")
  location.href = '/login'
}

let userStr = localStorage.getItem('user_code')
if (userStr) {
  let user = JSON.parse(userStr)
  if (!user.id){
    ElMessage.error("请先登录")
    location.href = '/login'
  }
} else {
  ElMessage.error("请先登录")
  location.href = '/login'
}

</script>


<style>
.el-menu {
  background-color: #eeeeee;
}

.el-menu-item {
  height: 50px;
}

.el-submenu__title:hover {
  background-color: cadetblue;
}
</style>