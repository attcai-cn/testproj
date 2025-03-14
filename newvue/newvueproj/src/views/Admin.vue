<template>
  <div class="card" style="margin-bottom: 5px">
    <el-input clearable @clear="load" style="width: 260px;margin-right: 5px;" placeholder="请输入姓名查询"
      v-model="tableData.name" :prefix-icon="Search"></el-input>
    <el-input clearable @clear="load" style="width: 260px;margin-right: 5px;" placeholder="请输入账号查询"
      v-model="tableData.username" :prefix-icon="Search"></el-input>

    <el-button type="primary" @click="load">查询</el-button>
    <el-button @click="reset">重置</el-button>
  </div>
  <div class="card" style="margin-bottom: 5px">
    <el-button type="primary" @click="handleAdd">添加</el-button>
    <el-button type="danger" @click="deleteBatch">批量删除</el-button>
    <el-upload style="display: inline-block;margin-left: 10px;margin-right: 10px"
               action="http://localhost:8080/admin/import"
               :show-file-list="false"
               :auto-upload="true"
               :on-success="handleImportSuccess"
               :on-error="handleImportError">
      <el-button type="success">批量导入</el-button>
    </el-upload>
    <el-button type="info" @click="exportData">批量导出</el-button>
  </div>
  <div class="card" style="margin-bottom: 5px">
    <el-table @selection-change="handleSelectionChange" :data=tableData.tabeldata style="width: 100%" :header-cell-style="{ background: '#f7f7f7' }">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="username" label="账号" width="180" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column label="操作" width="100">
        <template #default="scope">
          <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
          <el-button type="danger" icon="Delete" circle @click="deleteItem(scope.row.id)"></el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <div class="card" style="margin-bottom: 5px">
    <el-pagination v-model:current-page="tableData.pageNum" v-model:page-size="tableData.pageSize"
      :page-size="tableData.pageSize" layout="total,sizes, prev, pager, next" :total="tableData.total"
      :page-sizes="[10, 20, 30, 40, 80]" @current-change="load" @size-change="load" />
  </div>
  <div>
    <el-dialog v-model="tableData.formVisible" title="管理员信息" width="500">
      <el-form ref="formRef" :model="tableData.form" :rules="tableData.rules" label-width="80px"
        style="padding: 20px 40px 20px 0">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="tableData.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input v-model="tableData.form.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="tableData.form.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="tableData.form.email" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="tableData.form.password" autocomplete="off" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="tableData.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">
            保 存
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref} from 'vue'

const tableData = reactive({
  name: null,
  username: null,
  pageNum: 1,
  pageSize: 10,
  total: 6,
  formVisible: false,
  form: {
    name: null,
    username: null,
    phone: null,
    email: null,
    password: null
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
    ]
  },
  rows: [],
  ids: [],
})

const formRef = ref()

const load = () => {
  request.get('/admin/selectPage',
    {
      params: {
        pageNum: tableData.pageNum,
        pageSize: tableData.pageSize,
        name: tableData.name,
        username: tableData.username
      }
    }
  ).then(res => {
    tableData.total = res.data.total
    tableData.tabeldata = res.data.list

  })
}

load()

const reset = () => {
  tableData.name = null
  tableData.username = null
  load()
}

const handleAdd = () => {
  tableData.formVisible = true
}

const handleSelectionChange = (rows) => {
  tableData.rows = rows
  tableData.ids = tableData.rows.map(item => item.id) //[1,2,3]
}

// Add and Update
const addItem = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/admin/add', tableData.form).then(res => {
        if (res.code === '200') {
          ElMessage.success("添加成功")
          tableData.formVisible = false
          load()
        } else {
          ElMessage.error("添加失败")
        }
      })
    } else {
      ElMessage.error("请填写完整信息")
    }
  })
}

const updateItem = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('/admin/update', tableData.form).then(res => {
        if (res.code === '200') {
          ElMessage.success("修改成功")
          tableData.formVisible = false
          load()
        } else {
          ElMessage.error("修改失败")
        }
      })
    } else {
      ElMessage.error("请填写完整信息")
    }
  })
}

const handleEdit = (row) => {
  tableData.form = JSON.parse(JSON.stringify(row))
  tableData.formVisible = true
  tableData.form = row
}

// Delete
const deleteItem = (id) => {
  ElMessageBox.confirm('确认删除？删除后无法恢复。',{
    type: 'warning',
    confirmButtonText : '确定',
    cancelButtonText : '取消',
  }).then(res=>{
    request.delete('/admin/delete/'+id).then(res => {
      if(res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error("删除失败")
      }
    })
  }).catch(err=>{})
}

const deleteBatch = () => {
  ElMessageBox.confirm('确认删除？删除后无法恢复。',{
    type: 'warning',
    confirmButtonText : '确定',
    cancelButtonText : '取消',
  }).then(res=>{
    request.delete('/admin/deleteBatch',{ data: tableData.rows}).then(res => {
      if(res.code === '200') {
        ElMessage.success("批量删除成功")
        load()
      } else {
        ElMessage.error("批量删除失败")
      }
    })
  }).catch(err=>{})
}

const save = () => {
  if (tableData.form.id) {
    updateItem()
  } else {
    addItem()
  }
}

const exportData = () =>{
  let idStr = tableData.ids.join(",");
  let expUrl = `http://localhost:8080/admin/export?name=${tableData.name === null ? '':tableData.name}`
      + `&username=${tableData.username === null?'':tableData.username}`
      + `&ids=${idStr}`;
  window.open(expUrl);
}

const handleImportSuccess = () => {
    ElMessage.success("导入成功")
    load()
}

const handleImportError = () => {
    ElMessage.error("导入失败")
}

import { Delete, Edit, Search, Share, Upload } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
</script>