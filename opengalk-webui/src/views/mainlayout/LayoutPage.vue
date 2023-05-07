<template>
  <div>
    <Header/>
    <div class="main">
      <Aside class="aside"/>
      <div class="right">
        <router-view class="router"/>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import Header from './/HeaderPage.vue'
import Aside from './/AsidePage.vue'</script>

<style lang="scss" scoped>
.main {
  width: calc(100vw - 5px);
  overflow: hidden;
  display: flex;
  flex-direction: row;

  .right {
    width: 100vw;

    display: flex;
    flex-direction: column;

    .router {
      height: calc(100vh - 100px);
      -webkit-box-shadow: #666 0 0 1px;
      -moz-box-shadow: #666 0 0 1px;
      box-shadow: #666 0 0 2px;
      margin: 10px 5px 10px 10px;
      padding: 10px 10px 10px 10px;
      border-radius: 3px;

    }
  }
}


</style>