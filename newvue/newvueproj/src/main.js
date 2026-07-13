
import { createApp } from 'vue'
import App from './App.vue'
import router from './router/router.js'
import './assets/css/global.css'

// 导入element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

//注册所有图标
import * as ElementPlusIcons from '@element-plus/icons-vue'


const app = createApp(App)
app.use(router)
app.use(ElementPlus, { locale: zhCn })

for (const [key,component] of Object.entries(ElementPlusIcons)) {
    app.component(key,component)
}

app.mount('#app')