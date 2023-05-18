<template>
  <div class="user">
    <div class="user-top">
      <div class="top-left">
        <el-select v-model="condition" class="cascader">
          <el-option label="id" value="id"/>
          <el-option label="账号" value="account"/>
          <el-option label="名字" value="name"/>
        </el-select>
        <el-input v-model="keyword" class="search" placeholder="请输入关键字词"/>
        <el-button :icon="Search as string" type="primary" @click="getUserList(1)">查询</el-button>
        <el-button :icon="Refresh as string" @click="refresh">重置</el-button>
      </div>
      <div class="top-right">
        <el-button class="add" type="success" @click="toAddUser">添加用户</el-button>
      </div>
    </div>

    <!--    添加用户对话框-->
    <el-dialog v-model="addDialogVisible" center title="添加用户页面" width="35%" @close="cancel">
      <el-form
          ref="userFormRef"
          :model="addUserForm"
          :rules="addRules"
          label-width="80px"
      >
        <el-form-item label="账号" prop="account">
          <el-input v-model="addUserForm.account" clearable/>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="addUserForm.password" clearable show-password/>
        </el-form-item>

        <el-form-item label="权限" prop="authority">
          <el-select v-model="addUserForm.authority" placeholder="请选择职位">
            <el-option label="教师" value="1"/>
            <el-option label="学生" value="2"/>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="addUser(userFormRef)">提交</el-button>
          <el-button @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!--个人信息页面-->
    <el-dialog v-model="userInfoDialogVisible" align-center center title="个人信息页面" width="80%" @close="cancel">
      <div style="display: flex;flex-direction: row">
        <el-form
            :model="userInfoForm"
            label-width="80px"
            style="width: 50%;"
        >
          <el-form-item label="账号" prop="account">
            {{ userInfoForm.account }}
          </el-form-item>

          <el-form-item label="账户状态" prop="isLocked" @click="changeIsLockedValue">
            <el-switch
                v-model="isLockedValue"
                :disabled="isDisabled"
                active-text="未锁定"
                inactive-text="已锁定"
                inline-prompt
                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                @change="changeIsLockedValue"
            />
          </el-form-item>

          <p style="color: red;padding-left: 75px">*修改密码将会直接覆盖该用户旧密码*</p>
          <el-form-item label="密码">
            <el-button :disabled="isDisabled" type="danger" @click="toEditPassword">修改密码</el-button>
          </el-form-item>

          <el-form-item label="权限" prop="authority">
            <el-select v-model="authorityValue" :disabled="isDisabled" placeholder="请选择你的职位">
              <el-option label="教师" value="教师"/>
              <el-option label="学生" value="学生"/>
            </el-select>
          </el-form-item>
        </el-form>

        <el-descriptions
            border
            column="1"
            direction="vertical"
            style="width: 50%"
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
                  <Male/>
                </el-icon>
                <el-icon>
                  <Female/>
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
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="success" @click="toEditUser">{{ editButtonName }}</el-button>
          <el-button type="primary" @click="editUser">
            提交修改
          </el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="editPasswordDialogVisible" align-center center title="修改用户密码页面" width="35%">
      <p style="color: red;padding-left: 75px">*密码长度必须在6-20位*</p>
      <el-form-item label="新的密码">
        <el-input v-model="newPassword" clearable show-password type="password"/>
      </el-form-item>
      <el-button :disabled="isDisabled" style="margin-left: 150px" type="primary" @click="editPassword">设置新的密码
      </el-button>
    </el-dialog>

    <!--  用户表格-->
    <el-table :data="tableData" class="user-table">
      <template #empty>
        没有数据
      </template>
      <el-table-column label="id" prop="id" width="150"/>
      <el-table-column label="账号" prop="account" width="150"/>
      <el-table-column label="姓名" prop="name" width="150"/>
      <el-table-column
          :formatter="formatAuthorityInRow"
          label="权限"
          prop="authority"
          width="70"
      />

      <el-table-column
          :formatter="formatIsLocked"
          label="账户状态"
          prop="isLocked"
          width="80"
      />

      <el-table-column
          label="由谁创建(id)"
          prop="createBy"
          width="150"
      />

      <el-table-column
          :formatter="formatTimeInRow"
          align="center"
          label="创建时间"
          prop="createTime"
          width="95"
      />

      <el-table-column align="center" label="操作" width="250px">
        <!--          操作-->
        <template #default="scope">
          <el-button size="small" type="primary" @click="showUserInfo(scope)">查看和修改</el-button>

          <el-popconfirm
              title="确定删除该用户吗？"
              @confirm="deleteUser(scope.row.id)"
          >
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>

    </el-table>
    <!--        分页-->
    <div id="page">
      <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :background="background"
          :page-count="totalPage"
          :total="total"
          style="margin-top: 10px"
      />
    </div>

  </div>
</template>
<script lang="ts" setup>
import {Refresh, Search} from '@element-plus/icons-vue';
import {onMounted, reactive, ref, watch} from 'vue';
import {ElMessageBox, FormInstance, FormRules} from "element-plus/es";
import {showMessage} from "../../utils/MessageUtil";
import {formatAuthority, formatBirthday, formatGender, formatIsLocked, formatTime} from "../../utils/FormatterUtil";
import {ElMessage} from "element-plus";
import request from "../../utils/RequestUtil";

const requestUrl = "/userManagement";
const condition = ref('account');
const keyword = ref('');
const userFormRef = ref<FormInstance>();

//获取第一页用户
const tableData = ref<any[]>([]);
const pageSize = ref(8);
const totalPage = ref(0);
const currentPage = ref(1);
const background = ref(true);
const total = ref(0);

//新增用户
const addUserForm = ref({
  account: '',
  password: '',
  authority: ''
});

// 查看用户信息表
const userInfoForm = ref({
  id: undefined,
  account: '',
  authority: <string | number>'',
  isLocked: -1,
  name: '',
  gender: -1,
  phoneNumber: '',
  birthday: '',
  email: '',
  collegeId: undefined,
  collegeName: '',
});

const collegeList = ref([{
  id: undefined,
  collegeName: ''
}]);

const isDisabled = ref(true);
const isLockedValue = ref(false);
const editButtonName = ref('编辑个人信息');

// 修改用户密码
const editPasswordDialogVisible = ref(false);
const newPassword = ref('');
const addDialogVisible = ref(false);

// 查看用户信息
const userInfoDialogVisible = ref(false);
const authorityValue = ref();

const addRules = reactive<FormRules>({
  account: [
    {required: true, message: '不能为空', trigger: 'blur'},
    {min: 3, max: 20, message: '长度必须3-20', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '不能为空', trigger: 'blur'},
    {min: 6, max: 20, message: '长度必须3-20', trigger: 'blur'},
  ],
  authority: [
    {required: true, message: '不能为空', trigger: 'blur'},
    {min: 1, max: 2},
  ]
});

onMounted(() => {
  getUserList(1);
});

const clean = () => {
  addUserForm.value = {
    account: '',
    password: '',
    authority: ''
  };

  userInfoForm.value = {
    id: undefined,
    account: '',
    authority: -1,
    isLocked: -1,
    name: '',
    gender: -1,
    phoneNumber: '',
    birthday: '',
    email: '',
    collegeId: undefined,
    collegeName: '',
  };

  authorityValue.value = ref();
  collegeList.value = [];
};

const refresh = () => {
  keyword.value = '';
  getUserList(currentPage.value);
};

const getUserList = async (currentPage: number) => {
  await request
      .get(requestUrl + '/getUserList', {
        params: {
          currentPage: currentPage,
          pageSize: pageSize.value,
          condition: condition.value,
          keyword: keyword.value,
        }
      })
      .then((result: any) => {
        tableData.value = result.data.响应数据.records;
        totalPage.value = result.data.响应数据.pages;
        total.value = result.data.响应数据.total;
      });
};

// 分页
watch(currentPage, () => {
  getUserList(currentPage.value);
});

watch(isDisabled, () => {
  if (isDisabled.value === true) {
    editButtonName.value = '编辑个人信息';
  } else {
    editButtonName.value = '取消编辑';
  }
});

const toAddUser = () => {
  addDialogVisible.value = true;
  clean();
};

const addUser = async (userFormRef: FormInstance | undefined) => {
  if (!userFormRef) {
    return;
  }

  await userFormRef.validate((valid) => {
    if (valid) {
      request.post(requestUrl + '/addUser', addUserForm.value).then((result: any) => {
        showMessage(result);
        if (result.data.响应状态 === 1) {
          addDialogVisible.value = false;
        }
      });
    } else {
      ElMessageBox.alert('请检查表单内容是否符合规范');
    }
    clean();
    getUserList(currentPage.value);
  });
};

const toEditUser = () => {
  isDisabled.value = !isDisabled.value;
};

const showUserInfo = async (scope: any) => {
  isLockedValue.value = formatIsLocked(scope.row) == "未锁定";
  const id = tableData.value[scope.$index].id;
  await request.get(requestUrl + '/getUserInfo/' + id).then(result => {
    if (result.data.响应数据 != null) {
      userInfoForm.value = result.data.响应数据;
      authorityValue.value = formatAuthority(userInfoForm.value.authority as number);
      userInfoForm.value.gender = formatGender(userInfoForm.value.gender);
      userInfoForm.value.birthday = formatBirthday(userInfoForm.value.birthday, 0);
    }
  });
  userInfoDialogVisible.value = true;
};

const editUser = async () => {
  userInfoForm.value.authority = formatAuthority(authorityValue.value);
  userInfoForm.value.gender = formatGender(userInfoForm.value.gender);
  userInfoForm.value.birthday = formatBirthday(userInfoForm.value.birthday, 0);

  for (let i = 0; i < collegeList.value.length; i++) {
    if (collegeList.value[i].collegeName === userInfoForm.value.collegeName) {
      userInfoForm.value.collegeId = collegeList.value[i].id;
    }
  }

  await request.put(requestUrl + '/updateUserInfo', userInfoForm.value).then((result: any) => {
    if (result.data.响应状态 === 1) {
      userInfoDialogVisible.value = false;
      clean();
    } else {
      userInfoForm.value.gender = formatGender(userInfoForm.value.gender);
      userInfoForm.value.authority = formatAuthority(userInfoForm.value.authority);
    }
    showMessage(result);
  });
};

const cancel = () => {
  addDialogVisible.value = false;
  userInfoDialogVisible.value = false;
  isDisabled.value = true;
  clean();
  getUserList(currentPage.value);
};

const toEditPassword = () => {
  newPassword.value = '';
  editPasswordDialogVisible.value = true;
};

const editPassword = async () => {
  if (newPassword.value.length < 6 || newPassword.value.length > 20) {
    ElMessage.error('密码长度在6-20之间');
    return;
  }
  await request.put(requestUrl + '/updateUserPassword/' + userInfoForm.value.id, newPassword.value).then(result => {
    if (result.data.响应状态 === 1) {
      editPasswordDialogVisible.value = false;
    }
    showMessage(result);
  });
};

//删除用户
const deleteUser = (id: number) => {
  request.delete(requestUrl + '/deleteUser/' + id).then((result: any) => {
    showMessage(result);
    if (result.data.响应状态 === 1) {
      getUserList(currentPage.value);
    }
  });
};

const getCollegeList = async () => {
  await request.get(requestUrl + "/getCollegeList").then(result => {
    collegeList.value = result.data.响应数据;
  });
};

const changeIsLockedValue = () => {
  if (isLockedValue.value) {
    userInfoForm.value.isLocked = 0;
  } else {
    userInfoForm.value.isLocked = 1;
  }
};

const formatTimeInRow = (row: any, column: any) => {
  return formatTime(row[column.property]);
};

const formatAuthorityInRow = (row: any) => {
  return formatAuthority(row.authority);
};

</script>

<style lang="scss" scoped>

.user {
  .user-top {
    display: flex;
    flex-direction: row;
    max-width: 100%;

    .cascader {
      width: 150px;
    }

    .search {
      width: 150px;
      margin: 0 20px 0 20px
    }

    .top-left {
      width: 50%;
      display: flex;
      flex-direction: row;
      overflow: hidden;
    }

    .top-right {
      width: 50%;
      float: right;

      .add {
        float: right;
      }
    }

  }

  .user-table {
    width: 100%;
  }

}
</style>