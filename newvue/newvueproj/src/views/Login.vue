<script setup>
  import {reactive, ref} from "vue"; //ref也要引入
  import request from "@/utils/request"; //注意引用格式
  import {ElMessage} from "element-plus";
  import router from "@/router/router.js";

  const formRef = ref()

  const data = reactive({
    form:{
      username: null,
      password: null,
      role: null,
    },
    rules: {
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
        { min: 3, max: 14, message: '长度在 3 到 14 个字符', trigger: 'blur' }
      ],
      password: [
        { message: '请输入密码', trigger: 'blur' },
        { min: 4, max: 14, message: '长度在 4 到 14 个字符', trigger: 'blur' }
      ],

    },
  })

  const login = () => {
    formRef.value.validate((valid) => {
      if (valid) {
        request.post('/login', data.form).then(res => {
          if (res.code === '200') {
            localStorage.setItem('user_code',JSON.stringify(res.data|| {}) )
            ElMessage.success("登录成功")
            if (res.data.role === 'ADMIN') {
              router.push('/manager/Welcome')
            } else {
              router.push('guest/Welcome')
            }
          } else {
            ElMessage.error(res.msg)
          }
        })
      } else {
        console.log('error submit!!');
        return false;
      }
    })
  }


  const userLogin = () => {
    formRef.value.validate((valid) => {
      if (valid) {
        request.post('/userLogin', data.form).then(res => {
          if (res.code === '200') {
            localStorage.setItem('user_code',JSON.stringify(res.data|| {}) )
            ElMessage.success("登录成功")
            router.push('/guest/Welcome')
          } else {
            ElMessage.error(res.msg)
          }
        })
      } else {
        console.log('error submit!!');
        return false;
      }
    })
  }
</script>

<template>
  <div class="bg">
    <div style="width: 350px;background-color: #eeeeee; opacity: 0.85 ;border-radius: 5px;
    box-shadow: 0 0 10px rgba(0,0,0,0.1); padding:40px;">
      <h2 style="text-align: center;margin-bottom: 20px">请 登 录</h2>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="username">
          <el-input placeholder="输入账号" style="margin-left: 10px" prefix-icon="User" v-model="data.form.username" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="输入密码" show-password style="margin-left: 10px" prefix-icon="Lock" v-model="data.form.password" type="password" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="role">
          <el-select style="margin-left: 10px" v-model="data.form.role" placeholder="选择登录的身份">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="用户" value="USER"></el-option>
          </el-select>
        </el-form-item>
        <div style="text-align: center;">
          <el-button type="primary" @click="login">登 录</el-button>
<!--          <el-button type="primary" @click="userLogin">用户登录</el-button>-->
        </div>
        <div style="text-align: right;margin-top: 20px">
          还没有账号？请<a style="color: #ba0f56" href="/register">注册</a>
        </div>
      </el-form>
    </div>

  </div>
</template>

<style scoped>
.bg {
  width: 100%;
  height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url('@/assets/bg_login.jpg');
  background-size: cover;

}
</style>