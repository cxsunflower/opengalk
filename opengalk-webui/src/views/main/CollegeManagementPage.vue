<template>
    <div class="college">
        <div class="college-top">
            <div class="top-left">
                <el-select class="cascader" v-model="condition">
                    <el-option label="id" value="id"/>
                    <el-option label="学校名称" value="college_name"/>
                </el-select>
                <el-input class="search" v-model="keyword" placeholder="请输入关键字词"/>
                <el-button type="primary" :icon="Search as string" @click="getCollegeList(1)">查询</el-button>
                <el-button :icon="Refresh as string" @click="refresh">重置</el-button>
            </div>
            <div class="top-right">
                <el-button class="add" type="success" @click="toAddCollege">添加学校</el-button>
            </div>
        </div>
        <!-- 学院表格-->
        <el-table class="college-table" :data="tableData" :row-style="{height:'100px'}">
            <template #empty>
                没有数据
            </template>
            <el-table-column width="140" label="id" prop="id"/>
            <el-table-column width="250" label="学校名称" prop="collegeName"/>
            <el-table-column width="350" label="描述" prop="remark"/>

            <el-table-column
                    align="center"
                    width="95"
                    label="创建时间"
                    prop="createTime"
                    :formatter="formatTimeInRow"
            />

            <el-table-column label="操作" align="center" width="270px">
                <!--          操作-->
                <template #default="scope">
                    <el-button size="small" type="primary" @click="showCollegeInfo(scope)">查看和修改</el-button>

                    <el-popconfirm
                            title="确定删除该学校吗？"
                            @confirm="deleteCollege(scope.row.id)"
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

        <!--    添加学校对话框-->
        <el-dialog title="添加学校页面" v-model="addDialogVisible" width="50%" @close="cancel" center align-center>
            <el-form
                    ref="collegeFormRef"
                    :model="addCollegeForm"
                    label-width="80px"
                    :rules="addRules"
            >
                <el-form-item label="学校名称" prop="collegeName">
                    <el-input v-model="addCollegeForm.collegeName" clearable/>
                </el-form-item>

                <el-form-item label="描述" prop="remark">
                    <el-input :rows="8" type="textarea" v-model="addCollegeForm.remark" clearable/>
                </el-form-item>

                <div style="width: 100%;text-align: center">
                    <el-button type="primary" @click="addCollege(collegeFormRef)">提交</el-button>
                    <el-button @click="cancel">取消</el-button>
                </div>
            </el-form>
        </el-dialog>

        <!--    学校信息对话框-->
        <el-dialog v-model="collegeInfoDialogVisible" title="学校信息页面" @close="cancel" width="50%" align-center
                   center>

            <el-descriptions
                    style="padding: 0 20px 0 20px"
                    title="学校信息"
                    column="1"
                    border
                    direction="vertical"
            >
                <el-descriptions-item>
                    <template #label>
                        <div>
                            <el-icon>
                                <School/>
                            </el-icon>
                            学校名称
                        </div>
                    </template>
                    <el-input :disabled="isDisabled" v-model="collegeInfoForm.collegeName"></el-input>
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <div>
                            <el-icon>
                                <Edit/>
                            </el-icon>
                            学校描述
                        </div>
                    </template>
                    <el-input :rows="8" type="textarea" :disabled="isDisabled"
                              v-model="collegeInfoForm.remark"></el-input>
                </el-descriptions-item>
            </el-descriptions>

            <template #footer>
                <div class="dialog-footer">
                    <el-button type="success" @click="toEditCollege">{{ editButtonName }}</el-button>
                    <el-button type="primary" @click="editCollege">
                        提交修改
                    </el-button>
                </div>
            </template>

        </el-dialog>

    </div>
</template>
<script lang="ts" setup>

import {onMounted, reactive, ref, watch} from "vue";
import {ElMessageBox, FormInstance, FormRules} from "element-plus";
import request from "../../utils/RequestUtil";
import {Refresh, Search, Edit, School} from "@element-plus/icons-vue";
import {formatTime} from "../../utils/FormatterUtil";
import {showMessage} from "../../utils/MessageUtil";

onMounted(() => {
    getCollegeList(1)
})

const requestUrl = "/collegeManagement"
const condition = ref('college_name')
const keyword = ref('')
const collegeFormRef = ref<FormInstance>();

//获取第一页学校
const tableData = ref<any>([])
const pageSize = ref(4)
const totalPage = ref(0)
const currentPage = ref(1)
const background = ref(true)
const total = ref(0)

const addDialogVisible = ref(false)
const collegeInfoDialogVisible = ref(false)
const isDisabled = ref(true)
const editButtonName = ref('编辑学校信息')

const addCollegeForm = ref({
    collegeName: '',
    remark: '',
})

const collegeInfoForm = ref({
    collegeName: '',
    remark: '',
})

const addRules = reactive<FormRules>({
    collegeName: [
        {required: true, message: '不能为空', trigger: 'blur'},
        {min: 1, max: 20, message: '长度必须1-20', trigger: 'blur'},
    ],
    remark: [
        {min: 0, max: 100, message: '描述字数必须小于100', trigger: 'blur'},
    ],
})

watch(isDisabled, () => {
    if (isDisabled.value === true) {
        editButtonName.value = '编辑学校信息'
    } else {
        editButtonName.value = '取消编辑'
    }
})

const getCollegeList = async (currentPage: number) => {
    await request
        .get(requestUrl + '/getCollegeList', {
            params: {
                currentPage: currentPage,
                pageSize: pageSize.value,
                condition: condition.value,
                keyword: keyword.value,
            }
        })
        .then((result: any) => {
            tableData.value = result.data.响应数据.records
            totalPage.value = result.data.响应数据.pages
            total.value = result.data.响应数据.total
        })
}

const showCollegeInfo = async (scope: any) => {
    collegeInfoDialogVisible.value = true;
    const id = tableData.value[scope.$index].id
    await request.get(requestUrl + '/getCollegeInfo/' + id).then(result => {
        if (result.data.响应数据 != null) {
            collegeInfoForm.value = result.data.响应数据
        }
    })
}

const toAddCollege = () => {
    addDialogVisible.value = true
    clean()
}

const addCollege = async (collegeFormRef: FormInstance | undefined) => {
    if (!collegeFormRef) {
        return;
    }

    await collegeFormRef.validate((valid) => {
        if (valid) {
            request.post(requestUrl + '/addCollege', addCollegeForm.value).then((result: any) => {
                showMessage(result)
                if (result.data.响应状态 === 1) {
                    addDialogVisible.value = false
                }
            })
        } else {
            ElMessageBox.alert('请检查表单内容是否符合规范')
        }
        clean()
        getCollegeList(currentPage.value)
    })
}

const toEditCollege = () => {
    isDisabled.value = !isDisabled.value
}

const editCollege = async () => {
    await request.put(requestUrl + '/updateCollegeInfo', collegeInfoForm.value).then((result: any) => {
        if (result.data.响应状态 === 1) {
            collegeInfoDialogVisible.value = false
            clean()
        }
        showMessage(result)
    })
}

const clean = () => {
    addCollegeForm.value = {
        collegeName: '',
        remark: '',
    }

    collegeInfoForm.value = {
        collegeName: '',
        remark: '',
    }
}

const cancel = () => {
    addDialogVisible.value = false
    collegeInfoDialogVisible.value = false
    isDisabled.value = true
    clean()
    getCollegeList(currentPage.value)
}

const refresh = () => {
    keyword.value = ''
    getCollegeList(currentPage.value)
}

const deleteCollege = (id: number) => {
    request.delete(requestUrl + '/deleteCollege/' + id).then((result: any) => {
        showMessage(result)
        if (result.data.响应状态 === 1) {
            getCollegeList(currentPage.value)
        }
    })
}

const formatTimeInRow = (row: any, column: any) => {
    return formatTime(row[column.property])
}

</script>

<style lang="scss" scoped>
.college {
  .college-top {
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

  .college-table {
    width: 100%;
  }
}

</style>
