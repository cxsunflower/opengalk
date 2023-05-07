<template>
    <div>
        <ViewPaperHeaderPage/>
        <div class="main">
            <ViewPaperMainPage/>
        </div>
    </div>
</template>
<script setup lang="ts">
import ViewPaperHeaderPage from "./ViewPaperHeaderPage.vue";
import ViewPaperMainPage from "./ViewPaperMainPage.vue";
</script>

<style lang="scss" scoped>
.main {
  background-color: #edfade;
  overflow: hidden;
  display: flex;
  flex-direction: row;
}

</style>