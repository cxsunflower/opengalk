<template>
    <div>
        <ViewPaperHeaderPage/>
        <div class="main">
            <ViewPaperMainPage/>
        </div>
    </div>
</template>
<script lang="ts" setup>
import ViewPaperHeaderPage from "./ViewXCPaperHeaderPage.vue";
import ViewPaperMainPage from "./ViewXCPaperMainPage.vue";
</script>

<style lang="scss" scoped>
.main {
  background-color: #edfade;
  overflow: hidden;
  display: flex;
  flex-direction: row;
}

</style>