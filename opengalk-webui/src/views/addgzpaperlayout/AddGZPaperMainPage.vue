<template>
    <div class="main">
        <div class="left">
            <div style="display: flex;flex-direction: row">
                <el-button style="margin: 5px 5px 0 5px" type="primary" @click="toRequirement">查看输入题目要求
                </el-button>
                <el-upload
                        action="#"
                        v-model:file-list="currentImage as UploadUserFile[]"
                        :http-request="uploadImage"
                        :on-change="onChange"
                        :show-file-list="false"
                        :limit="1"
                        accept='image/jpeg,image/gif,image/png'
                >
                    <el-button style="margin: 5px 5px 0 5px">
                        <el-icon :size="25">
                            <Upload/>
                        </el-icon>
                        <div>插入图片</div>
                    </el-button>
                </el-upload>
            </div>
            <div style="border: 1px solid #ccc;margin: 5px 5px 0 5px">
                <Editor
                        style="height: 605px; overflow-y: hidden;"
                        v-model="allSubjects"
                        :defaultConfig="editorConfig"
                        :mode="mode"
                        @onCreated="handleCreated"
                />
            </div>
        </div>

        <div class="right">
            <div style="margin: 5px 5px 0 5px">
                <el-button type="primary" @click="viewPaper">整卷预览</el-button>
                <el-button type="success" @click="toUploadPaper">{{ buttonName }}</el-button>
            </div>

            <el-scrollbar>
                <div v-for="subject in uploadForm.subjectArray" style="margin: 5px 5px 5px 5px;padding-left: 5px">
                    {{ subject.subject }}
                    <el-radio-group v-show="subject.type !== 1" v-model="subject.answer[0]"
                                    size="large" style="display: table"
                    >
                        <div v-for="item in subject.items" v-show="subject.type !== 1" class="radio">
                            <el-radio :label="item.value">{{ item.value }}、 {{ item.text }}</el-radio>
                        </div>
                    </el-radio-group>

                    <el-checkbox-group
                            v-show="subject.type === 1"
                            v-model="subject.answer"
                    >
                        <div v-for="item in subject.items" class="radio">
                            <el-checkbox :label="item.value">{{ item.value }}、 {{ item.text }}</el-checkbox>
                        </div>
                    </el-checkbox-group>
                    <div v-show="subject.error" style="color:red">{{ subject.error }}</div>

                </div>
            </el-scrollbar>
        </div>

        <el-dialog v-model="uploadVisible" align-center center title="上传试卷页面" width="50%">
            <el-form
                    ref="uploadFormRef"
                    :model="uploadForm"
                    :rules="uploadRules"
                    style="padding: 0 40px 0 40px"
            >

                <el-form-item label="试卷名称" prop="name">
                    <el-input v-model="uploadForm.name"/>
                </el-form-item>

                <el-form-item label="试卷描述" prop="remark">
                    <el-input v-model="uploadForm.remark" :rows="4" type="textarea"/>
                </el-form-item>
            </el-form>

            <template #footer>
                <div>
                    <el-button type="success" @click="uploadPaper">
                        {{ buttonName }}
                    </el-button>
                    <el-button @click="close">
                        取消
                    </el-button>
                </div>
            </template>
        </el-dialog>

        <el-dialog v-model="requirementVisible" align-center center title="上传试卷要求页面" width="70%">
            <img alt="" src="../../assets/images/gz-requirement.png"/>
            <template #footer>
                <div>
                    <el-button type="primary" @click="close">
                        确定
                    </el-button>
                </div>
            </template>
        </el-dialog>

    </div>
</template>
<script lang="ts" setup>
import {onBeforeUnmount, onMounted, reactive, ref, shallowRef, watch} from "vue";
import {arrayToSubject, subjectToArray} from "../../utils/SubjectUtil";
import {FormInstance, FormRules, UploadProps, UploadUserFile} from "element-plus";
import request from "../../utils/RequestUtil";
import {showMessage} from "../../utils/MessageUtil";
import {useRoute} from "vue-router";
import {Editor} from "@wangeditor/editor-for-vue";

import "@wangeditor/editor/dist/css/style.css"
import {Upload} from "@element-plus/icons-vue";

const requestUrl = '/paperManagement'
const uploadFormRef = ref<FormInstance>();
const allSubjects = ref('')
const uploadVisible = ref(false)
const requirementVisible = ref(false)
const buttonName = ref('上传试卷')
const mode = ref('simple')
const editorConfig = {placeholder: '请输入内容...'}
const editorRef = shallowRef()
const currentImage = ref<UploadUserFile[]>()
const uploadForm = ref({
    name: '',
    type: 0,
    remark: '',
    subjectArray: <any[]>[],
})
const uploadRules = reactive<FormRules>({
    name: [
        {required: true, message: '不能为空', trigger: 'blur'},
        {min: 3, max: 20, message: '长度必须3-20', trigger: 'blur'},
    ],
    remark: [
        {required: true, message: '不能为空', trigger: 'blur'},
        {min: 3, max: 200, message: '长度必须3-200', trigger: 'blur'},
    ],
})

let addOrUpdate = 0
let uuid = <any>''

onMounted(async () => {
    const route = useRoute()
    uuid = route.query.uuid
    if (uuid != null) {
        addOrUpdate = 1
        buttonName.value = '更新试卷'
        await request.get(requestUrl + '/getPaper/' + uuid).then((result: any) => {
            uploadForm.value.name = result.data.响应数据.name
            uploadForm.value.remark = result.data.响应数据.remark
            allSubjects.value = arrayToSubject(result.data.响应数据.subjectArray)
        })
    }
})

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
    const editor = editorRef.value
    if (editor == null) return
    editor.destroy()
})

watch(allSubjects, () => {
    uploadForm.value.subjectArray = subjectToArray(allSubjects.value.replace(/<[^>]+>/g, '').replace(/&nbsp;/ig, ''));
})

const handleCreated = (editor: any) => {
    // 记录 editor 实例，重要！
    editorRef.value = editor
}

const uploadImage = () => {

}

const onChange: UploadProps['onChange'] = (uploadFile) :void=> {
    let reader = new FileReader()
    reader.readAsDataURL(uploadFile.raw as Blob)
    reader.onload = function () {
        let img_base64 = this.result
        allSubjects.value += `<img src="`+img_base64+`" alt="">`
    }
    currentImage.value = []
}

const toUploadPaper = () => {
    uploadVisible.value = true
}

const toRequirement = () => {
    requirementVisible.value = true
}

const viewPaper = () => {
    console.log(JSON.stringify(uploadForm))
    localStorage.setItem('viewGZPaperData', JSON.stringify(uploadForm))
    window.open('/viewGZPaper')
}

const uploadPaper = () => {
    if (addOrUpdate == 0) {
        request.post(requestUrl + '/addGZPaper', uploadForm.value).then((result: any) => {
            showMessage(result)
            if (result.data.响应状态 === 1) {
                uploadVisible.value = false
            }
        })
    } else {
        request.put(requestUrl + '/updateGZPaper/' + uuid, uploadForm.value).then((result: any) => {
            showMessage(result)
            if (result.data.响应状态 === 1) {
                uploadVisible.value = false
            }
        })
    }
}

const close = () => {
    uploadVisible.value = false
    requirementVisible.value = false
    uploadForm.value.name = ''
    uploadForm.value.remark = ''
}
</script>
<style lang="scss" scoped>
.main {
  display: flex;
  flex-direction: row;

  .left {
    width: 50%;
    padding-right: 5px;
    height: calc(100vh - 102px);

  }

  .right {
    width: 50%;
    height: calc(100vh - 102px);

    .radio {
      font-size: 16px;
    }
  }
}
</style>