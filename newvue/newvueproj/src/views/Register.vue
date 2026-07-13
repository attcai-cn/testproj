<script setup>
import {reactive,ref} from "vue";
import request from "@/utils/request"; //注意引用格式
import {ElMessage} from "element-plus";
import router from "@/router/router.js";

const validatePass = () =>{
  return (rule, value, callback) => {
    if (value === '') {
      callback(new Error('请再次确认密码'));
    } else {
      if (data.form.password !== value) {
        callback(new Error('两次输入的密码不一致!'));
      } else {
        callback();
      }
    }
  }
}


const data = reactive({
  form: {
    name: null,
    username: null,
    phone: null,
    email: null,
    password: null,
    confirmPassword: null,
  },
  rules: {
    name: [
      { required: true, message: '请输入姓名', trigger: 'blur' },
      { min: 2, max: 12, message: '长度在 2 到 12 个字符', trigger: 'blur' }
    ],
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { min: 3, max: 14, message: '长度在 3 到 14 个字符', trigger: 'blur' }
    ],
    phone: [
      { required: true, message: '请输入电话', trigger: 'blur' },
    ],
    email: [
      {message: '请输入邮箱', trigger: 'blur' },
    ],
    password: [
      { message: '请输入密码', trigger: 'blur' },
      { min: 8, max: 14, message: '长度在 8 到 14 个字符', trigger: 'blur' }
    ],
    confirmPassword: [
      { validator: validatePass(), triger: 'blur' },
    ]
  },

})

const formRef = ref()
const submit = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success("注册成功")
          router.push('/login')
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
      <h2 style="text-align: center;margin-bottom: 20px">注册新的账号</h2>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="username">
          <el-input placeholder="输入账号" style="margin-left: 10px" prefix-icon="User" v-model="data.form.username" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="输入密码" show-password style="margin-left: 10px" prefix-icon="Lock" v-model="data.form.password" type="password" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="再次输入密码" show-password style="margin-left: 10px" prefix-icon="Lock" v-model="data.form.confirmPassword" type="password" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="name">
          <el-input placeholder="输入姓名" style="margin-left: 10px" prefix-icon="User" v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="phone">
          <el-input placeholder="输入电话" style="margin-left: 10px" prefix-icon="User" v-model="data.form.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="email">
          <el-input placeholder="输入邮箱" style="margin-left: 10px" prefix-icon="User" v-model="data.form.email" autocomplete="off" />
        </el-form-item>

        <div style="text-align: center;">
          <el-button type="primary" @click="submit">提 交</el-button>
        </div>
        <div style="text-align: right;margin-top: 20px">
          已有账号？请<a style="color: #ba0f56" href="/login">返回登录</a>
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
  background-image: url('@/assets/bg_register.jpg');
  background-size: cover;

}
</style>