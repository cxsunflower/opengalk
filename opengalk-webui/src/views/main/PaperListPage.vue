<template>
    <div>
        <el-tabs
                v-model="activeName"
                class="demo-tabs"
                type="card"
        >
            <el-tab-pane name="公安专业科目" style="flex-direction: column">
                <template #label>
                    <div class="tab-pane">
                        <el-icon size="large">
                            <Postcard/>
                        </el-icon>
                        <span class="tab-pane-title">公安专业科目</span>
                    </div>
                </template>

                <el-row :gutter="10">
                    <el-col
                            v-for="(value,key) in paperList"
                            :key="key"
                            style="margin-bottom: 10px"
                    >
                        <div class="card">
                            <img
                                    id="image"
                                    alt="" src=""
                            />
                            <div style="display: flex;flex-direction: row;width: 100%">
                                <div style="padding-left: 20px;width: 63%;display:flex;align-items: center;">
                                    {{ value.name }}
                                </div>
                                <div style="width: 5%;display: flex;align-items: center;">
                                    <div v-show="value.score!=null">分数:</div>
                                </div>
                                <div class="score"> {{ value.score }}</div>
                                <div class="button">
                                    <el-button @click="showRank">查看排名</el-button>
                                    <el-button type="primary" @click="enterPaper(value.id)">进入考试</el-button>
                                    <el-button type="success">查看答案</el-button>
                                </div>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </el-tab-pane>
            <el-tab-pane name="行政职业能力测试" style="flex-direction: column">
                <template #label>
                    <div class="tab-pane">
                        <el-icon size="large">
                            <Star/>
                        </el-icon>
                        <span class="tab-pane-title">行政职业能力测试</span>
                    </div>
                </template>
            </el-tab-pane>

        </el-tabs>
        <el-dialog v-model="rankDialogVisible" :show-close="false" align-center center title="提示" width="30%">
            <div style="width:100%;font-size:19px;text-align: center">别卷了，好吗</div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="success" @click="cancel">确定</el-button>
                    <el-button type="primary" @click="cancel">
                        好的
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>

</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {Postcard, Star} from "@element-plus/icons-vue";
import request from "../../utils/RequestUtil";
import router from "../../router";

const requestUrl = '/paper';
const paperList = ref<any[]>([]);
const activeName = ref('公安专业科目');
const rankDialogVisible = ref(false);

onMounted(() => {
    getPaperList(0);
});

const getPaperList = (type: number) => {
    request.get(requestUrl + '/getPaperList', {
        params: {
            type: type,
        }
    }).then((result: any) => {
        paperList.value = result.data.响应数据;
    });
};

const enterPaper = (id: number) => {
    const routeData = router.resolve({
        path: '/GZPaper',
        query: {
            uuid: id
        }
    });
    window.open(routeData.href);
};

const showRank = () => {
    rankDialogVisible.value = true;
};

const cancel = () => {
    rankDialogVisible.value = false;
};
</script>

<style lang="scss" scoped>
.tab-pane {
  display: flex;
  flex-direction: row;
  align-items: center;
  text-align: center;
  width: 160px;


  .tab-pane-title {
    font-size: 16px;
    margin-left: 5px;
  }
}

.card {
  height: 60px;
  border: 1px solid #f2f2f2;
  border-radius: 3px 3px 3px 3px;
  margin-bottom: 5px;
  display: flex;
  align-items: center;
  justify-content: center;

  .score {
    width: 5%;
    display: flex;
    align-items: center;
    color: #409eff;
  }

  .button {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 27%;
  }

}


</style>