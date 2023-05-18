<template>
  <el-menu
      id="menu"
      :collapse="isCollapse"
      :default-active="'location.pathname'"
      router
  >
    <el-button id="toggle-button" :icon="arrow as string" text @click="handleCollapse"/>

    <el-menu-item index="/index">
      <el-icon>
        <LocationInformation/>
      </el-icon>
      <template #title>首页导航</template>
    </el-menu-item>

    <el-menu-item index="/paperList">
      <el-icon>
        <Document/>
      </el-icon>
      <template #title>模拟考试</template>
    </el-menu-item>

    <el-menu-item v-show="showZero" index="/paperManagement">
      <el-icon>
        <DocumentCopy/>
      </el-icon>
      <template #title>试卷管理</template>
    </el-menu-item>

    <el-menu-item v-show="showZero" index="/userManagement">
      <el-icon>
        <Avatar/>
      </el-icon>
      <template #title>用户管理</template>
    </el-menu-item>

    <el-menu-item v-show="showZero" index="/collegeManagement">
      <el-icon>
        <School/>
      </el-icon>
      <template #title>学校管理</template>
    </el-menu-item>

    <el-menu-item index="/userCenter">
      <el-icon>
        <Setting/>
      </el-icon>
      <template #title>个人中心</template>
    </el-menu-item>

    <el-menu-item index="" @click="toLogout">
      <el-icon>
        <Back/>
      </el-icon>
      <template #title>退出</template>
    </el-menu-item>
  </el-menu>

  <el-dialog v-model="logoutDialogVisible" align-center center title="退出提示" width="30%" @close="cancel">
    <div style="width:100%;font-size:19px;text-align: center">确定要退出吗</div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="success" @click="logout">确定</el-button>
        <el-button type="primary" @click="cancel">
          取消
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script lang="ts" setup>
import {markRaw, onMounted, ref} from 'vue';
import {
  Avatar,
  Back,
  Document,
  DocumentCopy,
  Expand,
  Fold,
  LocationInformation,
  School,
  Setting,
} from "@element-plus/icons-vue";
import {getAuthorityByToken, removeToken} from "../../utils/TokenUtil";
import request from "../../utils/RequestUtil";
import {showMessage} from "../../utils/MessageUtil";
import {ElMessageBox} from "element-plus";

const showZero = ref(false);
const showOne = ref(false);
const showTwo = ref(false);
const showZeroOne = ref(false);
const isCollapse = ref(false);

const arrow = markRaw(Fold);
const logoutDialogVisible = ref(false);

onMounted(() => {
  show();
});

const handleCollapse = () => {
  isCollapse.value = !isCollapse.value;
  if (isCollapse.value === true) {
    arrow.value = Expand;
  } else {
    arrow.value = Fold;
  }
};

const show = () => {
  const authority = getAuthorityByToken();
  switch (authority) {
    case 0:
      showZero.value = true;
      showZeroOne.value = true;
      break;
    case 1:
      showOne.value = true;
      showZeroOne.value = true;
      break;
    case 2:
      showTwo.value = true;
      break;
    default:
      break;
  }
};

const toLogout = () => {
  logoutDialogVisible.value = true;
};
const logout = async () => {
  logoutDialogVisible.value = false;
  await request.post('/logout').then(result => {
    removeToken();
    if (result.data.响应状态 === 1) {
      showMessage(result);
    } else {
      ElMessageBox.alert("注销失败，已移除本地token，请尝试重新登陆");
    }
  });
  location.reload();
};

const cancel = () => {
  logoutDialogVisible.value = false;
};


</script>

<style lang="scss" scoped>
#menu {
  border: 0;
  background-color: #f2f2f2;
  height: calc(100vh - 60px);

  #toggle-button {
    width: 100%;
    border-radius: 0;
    color: black;
    background-color: #67c23a;
  }
}

#menu:not(.el-menu--collapse) {
  width: 150px;
}

</style>

