<template>
  <div>
    <div id="header">
      <div class="header-left">
        <div id="avatar">
          <!--                <el-avatar :size="40" :icon="UserFilled"/>-->
        </div>
        <div id="title">
          <div style="font-size: 18px;font-weight: bolder;">
            {{ name }}
          </div>
          <div style="font-size: 10px;margin-left: 2px">行政职业能力测试</div>
        </div>
      </div>

      <div class="header-right">
        <div id="fullscreen" @click="fullScreen">
          <el-icon size="24">
            <FullScreen/>
          </el-icon>
          <span>{{ screenValue }}</span>
        </div>

        <div class="info">
          <div>机位号: {{ number }}</div>
          <div>剩余时间: {{ time }}</div>
        </div>

        <div class="button">
          <el-button style="width:90px;background-color: coral;border:0;color: white;font-size: 16px"
                     @click="toSubmit">
            交卷
          </el-button>
        </div>
      </div>
    </div>

    <div class="main">
      <div class="aside">
        <el-scrollbar :always="true" class="scrollbar">
          <div style="display: flex;flex-direction: column;padding: 10px 15px 10px 10px">
            <div class="知觉与速度" style="position: relative;">
              <div class="题块">知觉速度与准确性</div>
              <div v-for="i in 60"
                   :class="{changeColor:currentIndex == i || answerArray[i - 1] != ''}"
                   class="题号"
                   @click="alterSubject(i)">
                <div>{{ i }}</div>
                <div v-show="isMark(i)" class="mark_div"></div>
              </div>
            </div>

            <div class="常识判断" style="position: relative;">
              <div class="题块">常识判断</div>
              <div v-for="i in 80" v-show="i > 60"
                   :class="{changeColor:currentIndex == i || answerArray[i - 1] != ''}"
                   class="题号"
                   @click="alterSubject(i)">
                <div>{{ i }}</div>
                <div v-show="isMark(i)" class="mark_div"></div>
              </div>
            </div>

            <div class="言语理解与表达" style="position: relative;">
              <div class="题块">言语理解与表达</div>
              <div v-for="i in 110" v-show="i > 80"
                   :class="{changeColor:currentIndex == i || answerArray[i - 1] != ''}"
                   class="题号"
                   @click="alterSubject(i)">
                <div>{{ i }}</div>
                <div v-show="isMark(i)" class="mark_div"></div>
              </div>
            </div>

            <div class="数量关系">
              <div class="题块">数量关系</div>
              <div v-for="i in 120" v-show="i > 110"
                   :class="{changeColor:currentIndex == i || answerArray[i - 1] != ''}"
                   class="题号"
                   @click="alterSubject(i)">{{ i }}
              </div>
            </div>

            <div class="判断推理">
              <div class="题块">判断推理</div>
              <div v-for="i in 180" v-show="i > 120"
                   :class="{changeColor:currentIndex == i || answerArray[i - 1] != ''}"
                   class="题号"
                   @click="alterSubject(i)">{{ i }}
              </div>
            </div>

            <div class="资料分析">
              <div class="题块">资料分析</div>
              <div v-for="i in 180" v-show="i > 160"
                   :class="{changeColor:currentIndex == i || answerArray[i - 1] != ''}"
                   class="题号"
                   @click="alterSubject(i)">{{ i }}
              </div>
            </div>
          </div>
        </el-scrollbar>
      </div>

      <div class="right">
        <div class="题型">
          <div style="font-size: 18px">请按每道题的答题要求作答</div>
        </div>
        <div class="top">
          <div class="top-left">
            <div style="width: 45px;height: 30px;background-color: #b0caf6;line-height: 30px;text-align: center">
              {{ currentIndex }}
            </div>
            <div style="margin-left: 10px;font-size: 18px">({{ currentType }}选)</div>
          </div>
          <div class="top-right">
            <el-button :icon="ZoomIn as string" class="top-right-button" @click="toCorrect">纠错</el-button>
            <el-button :icon="Star as string" class="top-right-button" @click="collect">收藏本题</el-button>
          </div>

        </div>

        <div style="flex: 1;display: block;border-bottom: #d3dce6 1px solid;">
          <el-scrollbar :always="true">
            <div class="题目">
              <div>
                {{ subject }}
              </div>
            </div>
            <div style="margin:25px 0 0 70px">
              <el-checkbox-group
                  v-show="currentType === '多'"
                  v-model="checkboxAnswer"
                  size="large"
                  @change="checkboxChange"
              >
                <div class="checkbox" style="margin-top: 45px">
                  <el-checkbox :label="'A'">A、{{ optionA }}</el-checkbox>
                </div>
                <div class="checkbox">
                  <el-checkbox :label="'B'">B、{{ optionB }}</el-checkbox>
                </div>
                <div class="checkbox">
                  <el-checkbox :label="'C'">C、{{ optionC }}</el-checkbox>
                </div>
                <div class="checkbox">
                  <el-checkbox :label="'D'">D、{{ optionD }}</el-checkbox>
                </div>
              </el-checkbox-group>

              <el-radio-group v-show="currentType === '单'" v-model="radioAnswer"
                              size="large"
                              style="display: table"
                              @change="radioChange"
              >
                <div class="radio">
                  <el-radio v-show="currentType === '单'" :label="'A'">A、{{ optionA }}</el-radio>
                </div>
                <div class="radio">
                  <el-radio v-show="currentType === '单'" :label="'B'">B、{{ optionB }}</el-radio>
                </div>
                <div class="radio">
                  <el-radio v-show="currentType === '单'" :label="'C'">C、{{ optionC }}</el-radio>
                </div>
                <div class="radio">
                  <el-radio v-show="currentType === '单'" :label="'D'">D、{{ optionD }}</el-radio>
                </div>
                {{ answerArray }}
              </el-radio-group>
            </div>
          </el-scrollbar>
        </div>
        <div class="right-bottom">
          <div style="width: 50%">
            <el-button :disabled="perviousDisable" class="bottom-button" size="large" @click="previous">上一题
            </el-button>
            <el-button :disabled="nextDisable" class="bottom-button" size="large" type="primary"
                       @click="next">
              下一题
            </el-button>
          </div>

          <div style="width: 50%;float: right">
            <el-button v-show="markArray[currentIndex] === 1" :icon="Star as string" class="mark"
                       size="large"
                       @click="mark">
              取消标记
            </el-button>
            <el-button v-show="markArray[currentIndex] === 0" :icon="Star as string" class="mark"
                       size="large"
                       @click="mark">
              标记
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <el-dialog v-model="submitDialogVisible" :show-close="false" align-center center title="提交提示" width="30%">
    <div style="width:100%;font-size:19px;text-align: center">确定提交试卷？</div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="success" @click="submit">确定</el-button>
        <el-button type="primary" @click="cancel">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="correctDialogVisible" :show-close="false" align-center center title="纠错对话框" width="60%">
    <div>纠错内容</div>
    <el-input v-model="correctText" :rows="8" clearable type="textarea"/>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="success" @click="correct">提交</el-button>
        <el-button type="primary" @click="cancel">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {Star, ZoomIn} from "@element-plus/icons-vue";
import {onMounted, ref, watch} from "vue";
import {formatSubjectType} from "../../utils/FormatterUtil";
import request from "../../utils/RequestUtil";
import {useRoute} from "vue-router";
import screenfull from "screenfull";
import {ElMessageBox} from "element-plus";
import {showMessage} from "../../utils/MessageUtil";
import {Countdown, CountdownEventName, fillZero} from "../../utils/TimeUtil";

const screenValue = ref('进入全屏');
const name = ref('');
const number = ref(0);
const requestUrl = '/paper';
const radioAnswer = ref('');
const checkboxAnswer = ref([]);
const subject = ref('');
const currentIndex = ref(0);
const currentType = ref('');
const optionA = ref('');
const optionB = ref('');
const optionC = ref('');
const optionD = ref('');
const perviousDisable = ref(false);
const nextDisable = ref(false);
const answerArray = ref(new Array(100).fill(''));
const submitDialogVisible = ref(false);
const correctDialogVisible = ref(false);
const correctText = ref('');
const time = ref('');
let markArray = ref(new Array(100).fill(0));
let uuid = '';
let paperSubjects = <any>[];

watch(currentIndex, () => {
  if (currentIndex.value == 1) {
    perviousDisable.value = true;
  } else if (currentIndex.value == 100) {
    nextDisable.value = true;
  } else {
    nextDisable.value = false;
    perviousDisable.value = false;
  }
  alterSubject(currentIndex.value);
});

watch(time, () => {
  if (time.value === '0') {
    submit();
  }
});

onMounted(async () => {
  await getPaper();
  alterSubject(1);
  number.value = Math.floor(Math.random() * 101);

  const countdown = new Countdown(Date.now() + 2 * 60 * 60 * 1000, 1);
  countdown.on(CountdownEventName.RUNNING, (remainTimeData) => {
    const {hours, minutes, seconds} = remainTimeData;
    time.value = [hours, minutes, seconds].map(fillZero).join(':');
  });
});

const mark = () => {
  if (markArray.value[currentIndex.value] === 1) {
    markArray.value[currentIndex.value] = 0;
  } else {
    markArray.value[currentIndex.value] = 1;
  }
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

const getPaper = async () => {
  const route = useRoute();
  uuid = route.query.uuid as string;
  if (uuid != null) {
    await request.get(requestUrl + '/getPaper', {
      params: {
        uuid: uuid,
        type: 0
      }
    }).then((result: any) => {
      paperSubjects = result.data.响应数据.subjectArray;
      name.value = result.data.响应数据.name;
    });
  }
};

const toSubmit = () => {
  submitDialogVisible.value = true;
};

const collect = async () => {
  const collectForm = {
    uuid: uuid,
    subjectId: currentIndex.value
  };
  await request.post(requestUrl + '/collect', collectForm).then((result: any) => {
    showMessage(result);
  });
};

const toCorrect = () => {
  correctDialogVisible.value = true;
};
const correct = async () => {
  const correctForm = {
    uuid: uuid,
    subjectId: currentIndex.value,
    correctText: correctText.value,
  };

  await request.post(requestUrl + '/correct', correctForm).then((result: any) => {
    showMessage(result);
    if (result.data.响应状态 === 1) {
      correctDialogVisible.value = false;
    }
  });
};

const submit = async () => {
  const submitForm = {
    id: uuid,
    answer: answerArray.value.join(',')
  };
  await request.post(requestUrl + '/submitGZPaper', submitForm).then((result: any) => {
    showMessage(result);
    if (result.data.响应状态 === 1) {
      submitDialogVisible.value = false;
    }
  });
};

const cancel = () => {
  submitDialogVisible.value = false;
  correctDialogVisible.value = false;
};

const alterSubject = (i: number) => {
  if (currentType.value === '单') {
    radioAnswer.value = answerArray.value[i - 1];
  } else if (currentType.value === '多') {
    checkboxAnswer.value = answerArray.value[i - 1].split('');
  }
  currentIndex.value = i;
  const currentSubject = paperSubjects[i - 1];

  if (currentSubject != undefined) {
    subject.value = currentSubject.subject;
    currentType.value = formatSubjectType(currentSubject.type);
    optionA.value = currentSubject.optionA;
    optionB.value = currentSubject.optionB;
    optionC.value = currentSubject.optionC;
    optionD.value = currentSubject.optionD;
  } else {
    clean();
  }
};

const radioChange = () => {
  if (answerArray.value[currentIndex.value - 1] === radioAnswer.value) {
    answerArray.value[currentIndex.value - 1] = '';
  } else {
    answerArray.value[currentIndex.value - 1] = radioAnswer.value;
  }
};

const checkboxChange = () => {
  answerArray.value[currentIndex.value - 1] = checkboxAnswer.value.join('');
};

const isMark = (i: number) => {
  return markArray.value[i] === 1;
};

const previous = () => {
  currentIndex.value -= 1;
};

const next = () => {
  currentIndex.value += 1;
};

const clean = () => {
  subject.value = '';
  currentType.value = '';
  optionA.value = '';
  optionB.value = '';
  optionC.value = '';
  optionD.value = '';
};

</script>
<style lang="scss" scoped>
#header {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 60px;
  background-color: #1d7ecc;

  .header-left {
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
}

#title {
  display: flex;
  flex-direction: column;
  margin: 0 0 0 10px;
  color: white;
  justify-content: center;
}

.header-right {
  display: flex;
  flex-direction: row;

  .info {
    margin-right: 30px;
    display: flex;
    flex-direction: column;
    color: white;
    justify-content: center;
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
}

.main {
  background-color: #edfade;
  overflow: hidden;
  display: flex;
  flex-direction: row;

  .aside {
    width: 260px;
    height: calc(100vh - 60px);

    .题号 {
      font-size: 13px;
      color: #1d7ecc;
      border: 1px #1d7ecc solid;
      float: left;
      flex-direction: row;
      width: 27px;
      height: 27px;
      line-height: 29px;
      background-color: white;
      margin: 4px 4px 4px 4px;
      align-items: center;
      text-align: center;
      align-content: center;
      position: relative;
    }

    .mark_div {
      position: absolute;
      top: 22px;
      left: 19px;
      width: 0;
      height: 0;
      border-top: 6px solid #ff7f50;
      border-right: 6px solid transparent;
      border-left: 6px solid transparent;
      transform: rotate(-45deg);
      z-index: 2;
    }

    .题号:hover {
      cursor: pointer;
    }

    .changeColor {
      background-color: #1d7ecc;
      color: white;
    }

    .题块 {
      color: #1d7ecc;
      margin: 10px 0 10px 5px;
    }
  }

  .right {
    width: 100%;
    display: flex;
    flex-flow: column;

    .题型 {
      width: 100%;
      border-bottom: #d3dce6 1px solid;
      padding: 10px 10px 10px 10px
    }

    .top {
      display: flex;
      flex-direction: row;

      .top-left {
        display: flex;
        flex-direction: row;
        width: 50%;
      }

      .top-right {
        float: right;
        width: 50%;

        .top-right-button {
          float: right;
          margin-right: 10px;
        }
      }
    }

    .题目 {
      margin: 10px 10px 10px 50px;
      font-size: 17px;
      display: flex;
      flex-direction: row;
    }

    .radio {
      font-size: 16px;
      margin: 20px 0 0 10px;
    }

    :deep(.el-radio__label) {
      font-size: 17px;
      color: black;
      font-weight: normal;
    }

    .checkbox {
      font-size: 16px;
      margin: 20px 0 0 10px;
    }

    :deep(.el-checkbox__label) {
      font-size: 17px;
      color: black;
      font-weight: normal;
    }

    .right-bottom {
      height: 70px;
      display: flex;
      align-items: center;
      align-content: center;

      .bottom-button {
        font-size: 18px;
        margin-left: 20px;
        width: 130px;
      }

      .mark {
        font-size: 18px;
        margin-right: 30px;
        width: 120px;
        float: right;
        color: coral;
        border: coral 1px solid;
      }
    }
  }
}
</style>
