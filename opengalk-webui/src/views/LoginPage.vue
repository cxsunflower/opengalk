<template>
    <div class="main">
        <Left/>
        <div class="login">
            <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
                <h3 class="title">登 陆</h3>
                <el-form-item prop="account">
                    <el-input
                            v-model="loginForm.account"
                            auto-complete="off"
                            placeholder="账号"
                            type="text"
                    >
                        <template #prefix>
                            <el-icon>
                                <User/>
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                            v-model="loginForm.password"
                            auto-complete="off"
                            placeholder="密码"
                            type="password"
                    >
                        <template #prefix>
                            <el-icon>
                                <Lock/>
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item v-if="verifyEnable" prop="verificationCode">
                    <el-input
                            v-model="loginForm.verificationCode"
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
                    <div class="login-code" @click="flushVerificationCode">
                        <img :src="verificationCodeUrl" alt="" class="login-code-img"/>
                    </div>
                </el-form-item>

                <div style="display: flex;flex-direction: row">
                    <el-checkbox style="width: 50%;">记住密码</el-checkbox>
                    <div style="width: 50%;text-align: right;margin: auto">
                        <router-link class="link-type" to="/register">忘记密码？</router-link>
                    </div>
                </div>

                <el-form-item style="width:100%;">
                    <el-button
                            :loading="loading"
                            style="width:100%;"
                            type="primary"
                            @click="login(loginFormRef)"
                    >
                        <span v-if="!loading">登 录</span>
                        <span v-else>登 录 中...</span>
                    </el-button>
                    <div v-if="register" class="register">
                        <router-link class="link-type" style="text-align: right;" to="/register">立即注册</router-link>
                    </div>
                    <el-divider>
                        <span class="login-footor">其他登陆方式</span>
                    </el-divider>
                    <div style="display: flex;width: 400px">
                        <div class="logo">
                            <img alt="" class="login-img" src="../assets/logo/wx_logo.png" @click="login"/>
                        </div>
                        <div class="logo">
                            <img alt="" class="login-img" src="../assets/logo/qq_logo.png" @click="login"/>
                        </div>
                    </div>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import {FormInstance, FormRules} from "element-plus/es";
import Left from "../views/components/LeftPage.vue";
import axios from "axios";
import {removeToken, setToken} from "../utils/TokenUtil";
import router from "../router";
import {showMessage} from "../utils/MessageUtil";
import {Lock, Postcard, User} from "@element-plus/icons-vue";
import {baseURL} from "../utils/RequestUtil";
import {getVerificationCode} from "../utils/VerificationCodeUtil";

const loginRules = reactive<FormRules>({
    account: [
        {required: true, message: "不能为空", trigger: "blur"},
        {min: 3, max: 20, message: "长度必须3-12", trigger: "blur"},
    ],
    password: [
        {required: true, message: "不能为空", trigger: "blur"},
        {min: 6, max: 20, message: "长度必须6-20", trigger: "blur"},
    ],
    verificationCode: [{required: true, message: "请输入验证码", trigger: "blur"}],
});

const loginFormRef = ref<FormInstance>();

const loginForm = ref({
    account: '',
    password: '',
    verificationCode: '',
});

const register = ref(true);
const verificationCodeUrl = ref('');
const verifyEnable = ref(true);
const loading = ref(false);
const uuid = ref('');

onMounted(() => {
    flushVerificationCode();
});

const flushVerificationCode = async () => {
    loginForm.value.verificationCode = '';
    getVerificationCode().then(result => {
        verificationCodeUrl.value = result[0];
        uuid.value = result[1];
    });
};

const login = async (loginFormRef: FormInstance | undefined) => {
    if (!loginFormRef) {
        return;
    }

    await loginFormRef.validate((valid) => {
        if (valid) {
            const form =
                "account=" + loginForm.value.account +
                "&password=" + loginForm.value.password +
                "&uuid=" + uuid.value +
                "&verificationCode=" + loginForm.value.verificationCode;

            axios.post(baseURL + "/login", form, {
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(result => {
                if (result.data.响应状态 === 2) {
                    flushVerificationCode();
                } else if (result.data.响应状态 === 1) {
                    router.push("/index");
                    setToken(result.data.响应数据);
                } else {
                    flushVerificationCode();
                    removeToken();
                }
                showMessage(result);
            });
        }
    });
};

</script>

<style lang="scss" scoped>
.login {
  overflow: hidden;
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

.link-type {
  font-size: 16px;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 45px 60px 15px 60px;
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

  .register {
    width: 100vw;
  }

  .login-footor {
    font-size: 16px;
    text-align: center;
  }

  .login-img {;
    padding: 2px 2px 2px 2px;
    margin-top: 15px;
    width: 45px;
    height: 45px;
    border-radius: 50%;
  }

  .logo {
    text-align: center;
    width: 100vw;
  }

  .login-img:hover {
    cursor: pointer;
    background-color: #97a8be;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.login-code-img {
  height: 38px;
}
</style>
