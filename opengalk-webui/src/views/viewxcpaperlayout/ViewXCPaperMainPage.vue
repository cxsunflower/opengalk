<template>
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
                    </div>
                </div>

                <div class="常识判断" style="position: relative;">
                    <div class="题块">常识判断</div>
                    <div v-for="i in 80" v-show="i > 60"
                         class="题号"
                         @click="alterSubject(i)">
                        <div>{{ i }}</div>
                    </div>
                </div>

                <div class="言语理解与表达" style="position: relative;">
                    <div class="题块">言语理解与表达</div>
                    <div v-for="i in 110" v-show="i > 80"
                         class="题号"
                         @click="alterSubject(i)">
                        <div>{{ i }}</div>
                    </div>
                </div>

                <div class="数量关系">
                    <div class="题块">数量关系</div>
                    <div v-for="i in 120" v-show="i > 110"
                         class="题号"
                         @click="alterSubject(i)">{{ i }}
                    </div>
                </div>

                <div class="判断推理">
                    <div class="题块">判断推理</div>
                    <div v-for="i in 180" v-show="i > 120"
                         class="题号"
                         @click="alterSubject(i)">{{ i }}
                    </div>
                </div>

                <div class="资料分析">
                    <div class="题块">资料分析</div>
                    <div v-for="i in 180" v-show="i > 160"
                         class="题号"
                         @click="alterSubject(i)">{{ i }}
                    </div>
                </div>
            </div>
        </el-scrollbar>
    </div>

    <div class="right">
        <div class="题型">
            <div style="font-size: 16px">单选题（共30题）</div>
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
                <el-button :disabled="true" :icon="ZoomIn as string" class="top-right-button">纠错</el-button>
                <el-button :disabled="true" :icon="Star as string" class="top-right-button">收藏本题</el-button>
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
                            v-model="answerArray"
                            size="large"
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

                    <el-radio-group v-show="currentType === '单'" v-model="answer" size="large" style="display: table">
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
                    </el-radio-group>
                </div>
            </el-scrollbar>
        </div>
        <div class="right-bottom">
            <div style="width: 50%">
                <el-button :disabled="perviousDisable" class="bottom-button" size="large" @click="previous">上一题
                </el-button>
                <el-button :disabled="nextDisable" class="bottom-button" size="large" type="primary" @click="next">下一题
                </el-button>
            </div>

            <div style="width: 50%;float: right">
                <el-button :disabled="true" :icon="Star as string" class="mark" size="large">标记</el-button>
            </div>
        </div>

    </div>
</template>

<script lang="ts" setup>
import {Star, ZoomIn} from "@element-plus/icons-vue";
import {onMounted, ref, watch} from "vue";
import {formatSubjectType} from "../../utils/FormatterUtil";

const answer = ref('');
const answerArray = ref([]);
let paperSubjects = <any>[];
const subject = ref('');
const currentIndex = ref(0);
const currentType = ref('');
const optionA = ref('');
const optionB = ref('');
const optionC = ref('');
const optionD = ref('');
const perviousDisable = ref(false);
const nextDisable = ref(false);

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

onMounted(() => {
    paperSubjects = JSON.parse(localStorage.getItem('viewGZPaperData') as string)._value.subjectArray;
    console.log(paperSubjects.value);
    alterSubject(1);
});

const alterSubject = (i: number) => {
    currentIndex.value = i;
    const currentSubject = paperSubjects[i - 1];
    console.log(paperSubjects[i - 1]);
    if (currentSubject != undefined) {
        subject.value = currentSubject.subject;
        currentType.value = formatSubjectType(currentSubject.type);
        optionA.value = currentSubject.items[0].text;
        optionB.value = currentSubject.items[1].text;
        optionC.value = currentSubject.items[2].text;
        optionD.value = currentSubject.items[3].text;
    } else {
        clean();
    }
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
      width: 100px;
      float: right;
      color: coral;
      border: coral 1px solid;
    }
  }
}
</style>
