<template>
  <div style="display: flex; flex-direction: row">
    <div class="left">
      <div class="left-top">
        <img :src="'data:image/png;base64,'+avatarUrl" alt="" class="avatar"/>
        <div class="account">
          {{ account }}
        </div>

        <div class="remark">
          暂无简介
        </div>

        <div class="button">
          <el-button :icon="Setting as string" size="large" @click="showUserInfo">个人设置</el-button>
        </div>

        <div class="button">
          <el-button :icon="Lock as string" size="large" type="danger" @click="toUpdatePassword">修改密码
          </el-button>
        </div>

        <div class="button">
          <el-upload
              v-model:file-list="avatarList"
              :action="baseURL+requestUrl+'/uploadAvatar'"
              :before-upload="beforeAvatarUpload"
              :headers="header"
              :on-success="avatarUploadSuccess"
              :show-file-list="false"
              limit="1"
          >
            <el-button :icon="UserFilled as string" size="large" type="success">上传头像</el-button>
            <template #tip>
              <div class="el-upload__tip">
                图片大小不能超过5MB！
              </div>
            </template>
          </el-upload>
        </div>
      </div>


    </div>

    <div class="right">
      <el-tabs
          v-model="activeName"
          class="demo-tabs"
          type="card"
          @tab-change="tabChange"
      >
        <el-tab-pane name="first" style="flex-direction: column">
          <template #label>
            <div class="tab-pane">
              <el-icon size="large">
                <Postcard/>
              </el-icon>
              <span class="tab-pane-title">概览</span>
            </div>
          </template>

          <div class="record-title">每日记录</div>
          <div style="float: left">
            <div v-for="i in 356" class="block"></div>
          </div>
          <div class="today">今日动态</div>
          <div> 提交了一份</div>
        </el-tab-pane>

        <el-tab-pane label="收藏" name="second">
          <template #label>
            <div class="tab-pane">
              <el-icon size="large">
                <Star/>
              </el-icon>
              <span class="tab-pane-title">收藏</span>
            </div>
          </template>

          <el-scrollbar>
            <div style="width:100%;height:560px">
              <el-row :gutter="10">
                <el-col
                    v-for="(value,key) in collectList"
                    :key="key"
                    style="margin-bottom: 10px"
                >

                  <div class="card">
                    <div style="display: flex;flex-direction: row;width:800px">

                      <div style="padding-left: 20px;width: 20%;display:flex;align-items: center;">
                        试卷： {{ value.name }}
                      </div>
                      <div style="width: 10%;display:flex;align-items: center;">
                        题号：{{ value.subjectId }}
                      </div>
                      <div
                          style="max-width: 55%;height:60px;line-height:62px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                        题目：{{ value.subject }}
                      </div>
                      <div style="width: 10%;display:flex;align-items: center;">
                        <el-button type="success">查看题目</el-button>
                      </div>

                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-scrollbar>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog
        v-model="userInfoDialogVisible"
        align-center
        center
        width="50%"
        @close="userInfoClose"
    >
      <el-descriptions
          id="user-info"
          border
          column="1"
          direction="vertical"
          title="个人信息"
      >
        <el-descriptions-item>
          <template #label>
            <div>
              <el-icon>
                <User/>
              </el-icon>
              姓名
            </div>
          </template>
          <el-input v-model="userInfoForm.name" :disabled="isDisabled"></el-input>
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div>
              <el-icon>
                <School/>
              </el-icon>
              学校
            </div>
          </template>
          <el-select v-model="userInfoForm.collegeName" :disabled="isDisabled" @click="getCollegeList">
            <el-option v-for="college in collegeList" :key="college" :label="college.collegeName"
                       :value="college.collegeName"></el-option>
          </el-select>
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div>
              <el-icon>
                <male/>
              </el-icon>
              <el-icon>
                <female/>
              </el-icon>
              性别
            </div>
          </template>
          <el-radio-group v-model="userInfoForm.gender" :disabled="isDisabled">
            <el-radio label="男" size="large">男</el-radio>
            <el-radio label="女" size="large">女</el-radio>
          </el-radio-group>

        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div>
              <el-icon>
                <iphone/>
              </el-icon>
              手机号
            </div>
          </template>
          <el-input v-model="userInfoForm.phoneNumber" :disabled="isDisabled"></el-input>
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div>
              <el-icon>
                <Calendar/>
              </el-icon>
              出生日期
            </div>
          </template>
          <el-date-picker
              v-model="userInfoForm.birthday"
              :disabled="isDisabled"
              type="date"
          />
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div>
              <el-icon>
                <Message/>
              </el-icon>
              电子邮箱
            </div>
          </template>
          <el-input v-model="userInfoForm.email" :disabled="isDisabled"></el-input>
        </el-descriptions-item>
      </el-descriptions>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="success" @click="toEditUserInfo">{{ editButtonName }}</el-button>
          <el-button type="primary" @click="editUserInfo">
            提交修改
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!--    修改密码-->
    <el-dialog v-model="updatePasswordDialogVisible" align-center center title="修改密码页面" width="50%">
      <el-form
          ref="updatePasswordFormRef"
          :model="updatePasswordForm"
          :rules="updatePasswordRules"
          class="password"
          label-width="150px"
      >
        <el-form-item label="旧的密码" prop="oldPassword">
          <el-input v-model="updatePasswordForm.oldPassword" clearable show-password type="password"/>
        </el-form-item>

        <el-form-item label="新的密码" prop="newPassword">
          <el-input v-model="updatePasswordForm.newPassword" clearable show-password type="password"/>
        </el-form-item>

        <el-form-item label="再次确认新的密码" prop="reNewPassword">
          <el-input v-model="updatePasswordForm.reNewPassword" clearable show-password type="password"/>
        </el-form-item>

        <div style="width: 100%;text-align: center">
          <el-button type="primary" @click="updatePassword">设置新的密码</el-button>
        </div>

      </el-form>
    </el-dialog>

    <el-dialog
        v-model="toLoginDialogVisible"
        :show-close="false"
        align-center
        center
        title="跳转提示"
        width="30%"
    >
      <div style="display: flex;flex-direction: column;width: 100%;text-align: center">
        <div style="width: 100%;margin-bottom: 30px;font-size: 16px">更新密码成功，请重新登陆</div>
        <div>
          <el-button size="large" style="width: 30%;" type="success" @click="toLogin">确定</el-button>
        </div>
      </div>
    </el-dialog>


  </div>
</template>

<script lang="ts" setup>
import {getAccountByToken, getToken, removeToken} from "../../utils/TokenUtil";
import {
  Calendar,
  Female,
  Iphone,
  Lock,
  Male,
  Message,
  Postcard,
  School,
  Setting,
  Star,
  User,
  UserFilled
} from '@element-plus/icons-vue';
import {onMounted, reactive, ref, watch} from "vue";
import {ElMessage, ElMessageBox, FormInstance, FormRules, UploadProps, UploadUserFile} from "element-plus";
import {formatBirthday, formatGender} from "../../utils/FormatterUtil";
import request, {baseURL} from "../../utils/RequestUtil";
import {showMessage} from "../../utils/MessageUtil";
import router from "../../router";

const requestUrl = '/userCenter';
const account = getAccountByToken();
const activeName = ref('first');
const userInfoDialogVisible = ref(false);
const updatePasswordDialogVisible = ref(false);
const toLoginDialogVisible = ref(false);
const isDisabled = ref(true);
const editButtonName = ref('编辑个人信息');
const updatePasswordFormRef = ref<FormInstance>();
const avatarUrl = ref('');
const header = {token: getToken()};

const userInfoForm = ref({
  name: '',
  collegeName: '',
  email: '',
  phoneNumber: '',
  gender: -1,
  birthday: '',
  collegeId: undefined,
});

const collectList = ref([{
  uuid: '',
  subjectId: '',
  name: '',
  subject: '',
}]);

const avatarList = ref<UploadUserFile[]>([]);

const updatePasswordForm = ref({
  oldPassword: '',
  newPassword: '',
  reNewPassword: '',
});

const collegeList = ref([{
  id: undefined,
  collegeName: ''
}]);

const clean = () => {
  userInfoForm.value = {
    name: '',
    collegeName: '',
    email: '',
    phoneNumber: '',
    gender: -1,
    birthday: '',
    collegeId: undefined,
  };

  updatePasswordForm.value = {
    oldPassword: '',
    newPassword: '',
    reNewPassword: '',
  };

  collegeList.value = [];
};

onMounted(() => {
  request.get(requestUrl + '/getAvatar').then((result: any) => {
    avatarUrl.value = result.data.响应数据;
  });
});

watch(isDisabled, () => {
  if (isDisabled.value === true) {
    editButtonName.value = '编辑个人信息';
  } else {
    editButtonName.value = '取消编辑';
  }
});

const validateNewPassword = (rule: any, value: any, callback: any) => {
  if (value === updatePasswordForm.value.oldPassword) {
    return callback(new Error('不能与原密码相同'));
  } else {
    callback();
  }
};

const validateReNewPassword = (rule: any, value: any, callback: any) => {
  console.log(value);
  if (value !== updatePasswordForm.value.newPassword) {
    return callback(new Error('两次密码输入不一致'));
  } else {
    callback();
  }
};

const updatePasswordRules = reactive<FormRules>({
  oldPassword: [
    {required: true, message: '不能为空', trigger: 'blur'},
    {min: 6, max: 20, message: '长度必须6-20', trigger: 'blur'},
  ],
  newPassword: [
    {required: true, message: '不能为空', trigger: 'blur'},
    {min: 6, max: 20, message: '长度必须6-20', trigger: 'blur'},
    {validator: validateNewPassword, trigger: 'blur'},
  ],
  reNewPassword: [
    {required: true, message: '不能为空', trigger: 'blur'},
    {min: 6, max: 20, message: '长度必须6-20', trigger: 'blur'},
    {validator: validateReNewPassword, trigger: 'blur'},
  ]
});

const avatarUploadSuccess = (response: any) => {
  avatarList.value = [];
  ElMessage({
    message: response.响应消息,
    grouping: true,
    type: response.响应状态 === 1 ? 'success' : 'error',
    center: true,
  });

  if (response.响应状态 === 1) {
    location.reload();
  }
};

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile: any) => {
  console.log(rawFile.type);
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('图片格式必须为jpg或png格式！');
    return false;
  } else if (rawFile.size / 1024 / 1024 > 5) {
    ElMessage.error('图片大小不能超过5MB！');
    return false;
  }
  return true;
};

const showUserInfo = async () => {
  await request.get(requestUrl).then(result => {
    if (result.data.响应数据 != null) {
      userInfoForm.value = result.data.响应数据;
      userInfoForm.value.gender = formatGender(userInfoForm.value.gender);
      userInfoForm.value.birthday = formatBirthday(userInfoForm.value.birthday, 0);
    }
  });

  userInfoDialogVisible.value = true;
};

const userInfoClose = () => {
  isDisabled.value = true;
  clean();
};

const toEditUserInfo = () => {
  isDisabled.value = !isDisabled.value;
};

const editUserInfo = async () => {
  userInfoForm.value.birthday = formatBirthday(userInfoForm.value.birthday, 0);
  userInfoForm.value.gender = formatGender(userInfoForm.value.gender);
  for (let i = 0; i < collegeList.value.length; i++) {
    if (collegeList.value[i].collegeName === userInfoForm.value.collegeName) {
      userInfoForm.value.collegeId = collegeList.value[i].id;
    }
  }

  await request.put(requestUrl + "/updateUserInfo", userInfoForm.value).then(result => {
    userInfoDialogVisible.value = false;
    showMessage(result);
  });
};

const toUpdatePassword = () => {
  updatePasswordDialogVisible.value = true;
};

const updatePassword = async (updatePasswordFormRef: FormInstance | undefined) => {
  if (!updatePasswordFormRef) {
    return;
  }
  await updatePasswordFormRef.validate((valid) => {
    if (valid) {
      request.put(requestUrl + '/updatePersonalPassword', updatePasswordForm.value).then(result => {
        if (result.data.响应状态 === 1) {
          removeToken();
          updatePasswordDialogVisible.value = false;
          toLoginDialogVisible.value = true;
          clean();
        }
        showMessage(result);
      });
    } else {
      ElMessageBox.alert('请检查表单内容是否符合规范');
    }
  });
};

const getCollegeList = async () => {
  await request.get(requestUrl + "/getCollegeList").then(result => {
    collegeList.value = result.data.响应数据;
  });
};

const toLogin = () => {
  router.push('/login');
};

const tabChange = () => {
  if (activeName.value === 'second') {
    getCollectList();
  }
};
const getCollectList = async () => {
  await request.get(requestUrl + '/getCollectList').then((result: any) => {
    collectList.value = result.data.响应数据;
  });
};

</script>

<style lang="scss" scoped>
.left {
  width: 300px;
  text-align: center;

  .left-top {
    width: 300px;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;

    .avatar {
      border: 1px solid #f2f2f2;
      margin: 50px 0 0 0;
      width: 175px;
      height: 175px;
      border-radius: 50%;
    }

    .account {
      margin: 20px 0 0 0;
      font-size: 30px;
    }

    .remark {
      margin: 10px 0 0 0;
      color: #99a9bf;
    }

    .button {
      margin: 20px 0 0 0;
    }
  }

}

.right {
  float: left;


  .demo-tabs > .el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
  }

  .tab-pane {
    display: flex;
    flex-direction: row;
    align-items: center;
    text-align: center;
    width: 60px;
  }

  .card {
    height: 60px;
    border: 1px solid #f2f2f2;
    border-radius: 3px 3px 3px 3px;
    margin-bottom: 5px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .tab-pane-title {
    font-size: 16px;
    margin-left: 5px;
  }

  .record-title {
    margin-bottom: 10px;
    font-size: 24px;
  }

  .block {
    float: left;
    flex-direction: row;
    margin: 0 3px 3px 0;
    background-color: #9be9a8;
    border-radius: 3px;
    width: 11px;
    height: 11px;
  }

  .today {
    font-size: 24px;
  }
}

#user-info {
  padding-left: 10px;
  text-align: center;
}
</style>