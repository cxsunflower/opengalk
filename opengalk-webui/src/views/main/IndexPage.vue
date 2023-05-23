<template>
  <div class="div-back">
    <el-carousel :interval="4000" height="200px" type="card">
      <el-carousel-item v-for="item in 3" :key="item">
        <h3 justify="center" text="2xl">{{ item }}</h3>
      </el-carousel-item>
    </el-carousel>

    <el-row :gutter="10">
      <el-col
          v-for="(value,key) in form"
          :key="key"
          :span="8"
          style="margin-bottom: 10px"
      >
        <el-card :body-style="{ padding: '3px'}">
          <img
              id="image"
              alt="" src=""/>
          <div style="padding: 14px;text-align: center">
            <div style="padding: 0 0 15px 0">{{ value.name }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import request from "../../utils/RequestUtil";

onMounted(() => {
  request.get("/index").then();
});

const form = ref([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]);
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>