<template>
    <div class="paper">
        <div class="paper-top">
            <div class="top-left">
                <el-select v-model="condition" class="cascader">
                    <el-option label="试卷名称" value="name"/>
                    <el-option label="教师" value="teacher_name"/>
                </el-select>
                <el-input v-model="keyword" class="search" placeholder="请输入关键字词"/>
                <el-button :icon="Search as string" type="primary" @click="getPaperList(1)">查询</el-button>
                <el-button :icon="Refresh as string" @click="refresh">重置</el-button>
            </div>
            <div class="top-right">
                <el-button class="add" type="success" @click="toAddPaper">添加试卷</el-button>
            </div>
        </div>
        <!--  试卷表格-->
        <el-table :data="tableData" :row-style="{height:'100px'}" class="user-table">
            <template #empty>
                没有数据
            </template>
            <el-table-column label="试卷名称" prop="name" width="180"/>
            <el-table-column :formatter="formatPaperTypeInRow" label="试卷类型" prop="type" width="150"/>
            <el-table-column label="试卷描述" prop="remark" width="300"/>
            <el-table-column
                    label="由谁创建(id)"
                    prop="createBy"
                    width="130"
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
                    <el-button size="small" type="primary" @click="showPaperInfo(scope)">查看和修改</el-button>

                    <el-popconfirm
                            title="确定删除该试卷吗？"
                            @confirm="deletePaper(scope.row.id)"
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

        <el-dialog
                v-model="toAddPaperDialogVisible"
                align-center
                center
                title="前往添加试卷页面"
                width="40%"
        >
            <div style="display: flex;flex-direction: column;width:100%;align-items: center">
                <div style="margin-bottom: 20px">
                    试卷类型
                    <el-select v-model="selectedValue" placeholder="请选择试卷类型">
                        <el-option label="行政职业能力测试" value="0">行政职业能力测试</el-option>
                        <el-option label="公安专业科目" value="1">公安专业科目</el-option>
                    </el-select>
                </div>

                <el-button size="large" style="width: 100px" type="primary" @click="addPaper">
                    前往添加试卷
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script lang="ts" setup>
import {Refresh, Search} from "@element-plus/icons-vue";
import {onMounted, ref} from "vue";
import request from "../../utils/RequestUtil";
import {formatPaperType, formatTime} from "../../utils/FormatterUtil";
import {showMessage} from "../../utils/MessageUtil";
import router from "../../router";
import {ElMessage} from "element-plus";

onMounted(() => {
    getPaperList(1);
});

const requestUrl = "/paperManagement";
const condition = ref('name');
const keyword = ref('');
const tableData = ref<any[]>([]);
const pageSize = ref(4);
const totalPage = ref(0);
const currentPage = ref(1);
const background = ref(true);
const total = ref(0);
const toAddPaperDialogVisible = ref(false);
const selectedValue = ref('');

const getPaperList = async (currentPage: number) => {
    await request
        .get(requestUrl + '/getPaperList', {
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

const showPaperInfo = async (scope: any) => {
    const routeData = router.resolve({
        path: '/addGZPaper',
        query: {
            uuid: scope.row.id
        }
    });
    window.open(routeData.href);
};

const toAddPaper = () => {
    toAddPaperDialogVisible.value = true;
};

const addPaper = () => {
    switch (selectedValue.value) {
        case '0':
            window.open('/addXCpaper');
            break;
        case '1':
            window.open('/addGZpaper');
            break;
        default:
            ElMessage({
                message: "请选择试卷类型",
                grouping: true,
                type: 'error',
                center: true,
            });
            break;
    }
};

const deletePaper = async (id: number) => {
    await request.delete(requestUrl + '/deletePaper/' + id).then((result: any) => {
        showMessage(result);
        if (result.data.响应状态 === 1) {
            getPaperList(currentPage.value);
        }
    });
};

const refresh = () => {
    keyword.value = '';
    getPaperList(currentPage.value);
};
const formatTimeInRow = (row: any, column: any) => {
    return formatTime(row[column.property]);
};

const formatPaperTypeInRow = (row: any) => {
    return formatPaperType(row.type);
};

</script>
<style lang="scss" scoped>
.paper {
  .paper-top {
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