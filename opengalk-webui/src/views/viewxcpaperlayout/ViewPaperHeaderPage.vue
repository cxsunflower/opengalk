<template>
    <div id="header">
        <div class="left">
            <div id="avatar">
                <el-avatar v-show="false" :icon="UserFilled as string" :size="40"/>
            </div>
            <div id="title">
                <div style="font-size: 18px;font-weight: bolder;">
                    {{ name }}（预览模式）
                </div>
                <div style="font-size: 10px;margin-left: 2px">{{ type }}</div>
            </div>
        </div>

        <div class="right">

            <div id="fullscreen" @click="fullScreen">
                <el-icon size="24">
                    <FullScreen/>
                </el-icon>
                <span>{{ screenValue }}</span>
            </div>

            <div class="info">
                <div>机位号: 100</div>
                <div>剩余时间: 02:00:00</div>
            </div>

            <div class="button">
                <el-button :disabled="true"
                           style="width:90px;background-color: coral;border:0;color: white;font-size: 16px">
                    交卷
                </el-button>
            </div>

        </div>
    </div>
</template>

<script lang="ts" setup>
import {FullScreen, UserFilled} from "@element-plus/icons-vue";
import {ElMessageBox} from "element-plus/es";
import screenfull from "screenfull";
import {onMounted, ref} from "vue";
import {formatPaperType} from "../../utils/FormatterUtil";

const screenValue = ref('进入全屏')
const name = ref('')
const type = ref('')

onMounted(() => {
    let paperData = localStorage.getItem('paperData') as any
    if (paperData != null) {
        paperData = JSON.parse(paperData)
        name.value = paperData._value.name
        type.value = formatPaperType(paperData._value.type)
    }
})

const fullScreen = () => {
    if (!screenfull.isEnabled) {
        ElMessageBox.alert("当前浏览器不支持全屏")
    } else {
        if (!screenfull.isFullscreen) {
            screenValue.value = '退出全屏'
        } else {
            screenValue.value = '进入全屏'
        }
        screenfull.toggle()
    }
}

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
  width: 70%;
}

#avatar {
  margin-left: 10px;
  display: flex;
  height: 60px;
  justify-content: center;
  align-items: center;
  text-align: center;
}

#title {
  display: flex;
  flex-direction: column;
  margin: 0 0 0 10px;
  color: white;
  justify-content: center;
}

.right {
  display: flex;
  flex-direction: row;

  .info {
    margin-right: 30px;
    display: flex;
    flex-direction: column;
    color: white;
    justify-content: center;
  }
}

#fullscreen {

  margin-right: 30px;
  height: 60px;
  display: flex;
  color: white;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
}

#fullscreen:hover {
  cursor: pointer;
}

.button {
  display: flex;
  height: 60px;
  text-align: center;
  justify-content: center;
  align-items: center;
}

</style>