<template>
  <Left/>
  <div class="register">
    <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="register-form">
      <h3 class="title">注 册</h3>
      <el-form-item prop="account">
        <el-input v-model="registerForm.account" type="text" auto-complete="off" placeholder="账号">
          <template #prefix>
            <el-icon>
              <User/>
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            v-model="registerForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
        >
          <template #prefix>
            <el-icon>
              <Lock/>
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            auto-complete="off"
            placeholder="确认密码"
        >
          <template #prefix>
            <el-icon>
              <Lock/>
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="verificationCode" v-if="verifyEnabled">
        <el-input
            v-model="registerForm.verificationCode"
            auto-complete="off"
            placeholder="验证码"
            style="width: 60%;margin-right: 3%"
        >
          <template #prefix>
            <el-icon>
              <Postcard/>
            </el-icon>
          </template>
        </el-input>
        <div class="register-code">
          <img :src="verificationCodeUrl" class="register-code-img" @click="flushVerificationCode" alt=""/>
        </div>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
            :loading="loading"
            type="primary"
            style="width:100%;"
            @click="handleRegister(registerFormRef)"
        >
          <span v-if="!loading">注 册</span>
          <span v-else>注 册 中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import {Lock, Postcard, User} from "@element-plus/icons-vue";
import Left from "../views/components/LeftPage.vue";
import {onMounted, reactive, ref} from "vue";
import {FormInstance, FormRules} from "element-plus/es";
import {showMessage} from "../utils/MessageUtil";
import router from "../router";
import request from "../utils/RequestUtil";
import {getVerificationCode} from "../utils/VerificationCodeUtil";

const registerForm = ref({
  account: ref(),
  password: ref(),
  confirmPassword: ref(),
  verificationCode: ref(),
  uuid: ref()
})

const registerFormRef = ref<FormInstance>();

const verificationCodeUrl = ref('')
const verifyEnabled = ref(true)
const loading = ref(false)

onMounted(() => {
  flushVerificationCode()
})

const flushVerificationCode = () => {
  registerForm.value.verificationCode = ref()
  getVerificationCode().then(result => {
    verificationCodeUrl.value = result[0]
    registerForm.value.uuid = result[1]
  })
}

const clean = () => {
  registerForm.value = {
    account: ref(),
    password: ref(),
    confirmPassword: ref(),
    verificationCode: ref(),
    uuid: ref()
  }
}
//自定义验证
const validator = (rule: any, value: any, callback: any) => {
  console.log(value + "," + registerForm.value.password)
  if (value !== registerForm.value.password) {
    callback(new Error())
  } else {
    callback()
  }
}

const registerRules = reactive<FormRules>({
  account: [
    {required: true, message: "不能为空", trigger: "blur"},
    {min: 3, max: 20, message: "长度必须3-12", trigger: "blur"},
  ],
  password: [
    {required: true, message: "不能为空", trigger: "blur"},
    {min: 6, max: 20, message: "长度必须6-20", trigger: "blur"},
  ],
  confirmPassword: [
    {required: true, message: "不能为空", trigger: "blur"},
    {validator: validator, message: "两次密码输入不一致"},
  ],
  verificationCode: [{required: true, message: "请输入验证码", trigger: "blur"}],
})

const handleRegister = async (registerFormRef: FormInstance | undefined) => {
  if (!registerFormRef) {
    return
  }

  await registerFormRef.validate((valid) => {
    if (valid) {
      request.post("/register", registerForm.value).then(result => {
        showMessage(result)
        if (result.data.响应状态 === 1) {
          router.push("/login")
        } else if (result.data.响应状态 === 2) {
          flushVerificationCode()
        } else {
          clean()
          flushVerificationCode()
        }
      })
    }
  })
}

</script>

<style lang="scss" scoped>
.left {
  background-color: #1890ff;
}

.register {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-size: cover;
}

.title {
  margin: 0 auto 30px auto;
  text-align: center;
}

.register-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 35px 60px 60px 60px;
  -webkit-box-shadow: #666 0 0 10px;
  -moz-box-shadow: #666 0 0 10px;
  box-shadow: #666 0 0 10px;

  .el-input {
    height: 38px;

    input {
      height: 38px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.register-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.register-code-img {
  height: 38px;
}
</style>
