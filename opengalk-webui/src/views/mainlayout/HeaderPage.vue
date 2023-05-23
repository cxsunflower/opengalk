<template>
  <div id="header">
    <div class="left">
      <div id="title" @click="toIndex">
        OpenGALK
      </div>
    </div>

    <div class="right">
      <div id="avatar">
        <img v-show="avatarUrl" :src="'data:image/png;base64,'+avatarUrl" alt="" class="avatar"/>
        <el-avatar v-show="!avatarUrl" :icon="UserFilled as string" :size="37"/>
      </div>

      <div id="info">
        &nbsp;{{ account }}
      </div>

      <div id="fullscreen" @click="fullScreen">
        <el-icon size="24">
          <FullScreen/>
        </el-icon>
        <span>{{ screenValue }}</span>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {UserFilled} from "@element-plus/icons-vue";
import {getAccountByToken} from "../../utils/TokenUtil";
import {ElMessageBox} from "element-plus/es";
import screenfull from "screenfull";
import {onMounted, ref} from "vue";
import router from "../../router";
import request from "../../utils/RequestUtil";

const account = getAccountByToken();
const screenValue = ref('进入全屏');
const requestUrl = '/header';
const avatarUrl = ref('');

onMounted(() => {
  request.get(requestUrl + '/getAvatar').then((result: any) => {
    avatarUrl.value = result.data.响应数据;
  });
});

const toIndex = () => {
  router.push('/index');
};
const fullScreen = () => {
  if (!screenfull.isEnabled) {
    ElMessageBox.alert("当前浏览器不支持全屏");
  } else {
    if (!screenfull.isFullscreen) {
      screenValue.value = '退出全屏';
    } else {
      screenValue.value = '进入全屏';
    }
    screenfull.toggle();
  }
};

</script>

<style lang="scss" scoped>
#header {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 60px;
  background-color: #1d7ecc;
}

.left {
  display: flex;
  width: 50%;
}

#title {
  margin: 0 0 0 20px;
  width: 140px;
  color: white;
  font-size: 22px;
  font-weight: bolder;
  line-height: 60px;
  float: left;
}

#title:hover {
  cursor: pointer;
}

.right {
  float: right;
  width: 50%;
}

#fullscreen {
  margin-right: 10px;
  height: 60px;
  display: flex;
  float: right;
  color: white;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
}

#fullscreen:hover {
  cursor: pointer;
}

#avatar {
  display: flex;
  height: 60px;
  padding: 0 10px 0 10px;
  justify-content: center;
  align-items: center;
  text-align: center;
  float: right;
}

.avatar {
  border: 1px solid #f2f2f2;
  width: 35px;
  height: 35px;
  border-radius: 50%;
}

#info {
  color: white;
  float: right;
  font-size: larger;
  font-weight: bolder;
  text-align: center;
  line-height: 60px;
}

</style>